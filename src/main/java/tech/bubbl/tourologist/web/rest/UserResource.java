package tech.bubbl.tourologist.web.rest;

import com.google.common.base.Strings;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.bubbl.tourologist.config.Constants;
import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.domain.Authority;
import tech.bubbl.tourologist.domain.User;
import tech.bubbl.tourologist.repository.UserRepository;
import tech.bubbl.tourologist.security.AuthoritiesConstants;
import tech.bubbl.tourologist.security.jwt.JWTConfigurer;
import tech.bubbl.tourologist.security.jwt.TokenProvider;
import tech.bubbl.tourologist.service.MailService;
import tech.bubbl.tourologist.service.UserService;
import tech.bubbl.tourologist.service.dto.ErrorDTO;
import tech.bubbl.tourologist.service.dto.SuccessTransportObject;
import tech.bubbl.tourologist.service.dto.UserTokenDTO;
import tech.bubbl.tourologist.web.rest.vm.ManagedUserVM;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import tech.bubbl.tourologist.web.rest.vm.Social;
import tech.bubbl.tourologist.web.rest.vm.SocialLoginDTO;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    public static final String FACEBOOK_PASSWORD = "FACEBOOK_PASSWORD";
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;



    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody ManagedUserVM managedUserVM, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserVM);
            sendEmail(request, newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    @PostMapping("/login/social")
    @Timed
    public ResponseEntity<?> createUserWithFaceBook(@Valid @RequestBody SocialLoginDTO socialLoginDTO,
                                                    @RequestParam(value = "type") Social social,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws URISyntaxException {

        String token = socialLoginDTO.getToken();
        Facebook facebook = new FacebookFactory().getInstance();

//            facebook.setOAuthAppId("1620572758230874", "0805cdcfb1ba6cd6fd2283b5f3a6fe64");  todo also try this!!
//        facebook.setOAuthAppId("655316051314562", "8d5b630547d46cb3269dc69dc3c783bb");
        facebook.setOAuthAppId("353913494977313", "28193f774fd70ab095e0ea700b139e56");
        facebook.setOAuthAccessToken(new AccessToken(token, null));
        facebook4j.User me;
        try {
            me = facebook.getMe(new Reading().fields("id", "email", "first_name", "last_name", "name"));
        } catch (FacebookException e) {
            log.error("Facebook authentication failed", e);
            return new ResponseEntity<Object>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }

        if (me == null || me.getId() == null) {
            return new ResponseEntity<Object>(new ErrorDTO("Facebook token is not valid " + token), HttpStatus.NOT_FOUND);
        }

        String login = me.getId().toLowerCase();
        String email = Optional.ofNullable(me.getEmail())
            .map(String::toLowerCase).orElse("");
        String firstName = me.getFirstName();
        String lastName = me.getLastName();


        if (userRepository.findOneByEmail(email).isPresent()) {
            User user = userService.getUserWithAuthoritiesByEmail(email).get();

            String authorities = user.getAuthorities().stream()
                .map(Authority::getName)
                .collect(Collectors.joining(","));

            String jwt = tokenProvider.createTokenByAuthorities(authorities, true, email);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new UserTokenDTO(user, "Bearer " + jwt), HttpStatus.OK);
        }

        if (userRepository.findOneByLogin(login).isPresent()) {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login, FACEBOOK_PASSWORD);

            try {
                Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.createToken(authentication, true);
                response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return Optional.ofNullable(userService.getUserWithAuthorities())
                    .map(user -> new ResponseEntity<>(new UserTokenDTO(user, "Bearer " + jwt), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            } catch (AuthenticationException exception) {
                return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
            }

        } else {
            User user = userService.createUser(login, FACEBOOK_PASSWORD,
                firstName, lastName, email, "en");

            if (!Strings.isNullOrEmpty(user.getEmail())) {
                sendEmail(request, user);
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login, FACEBOOK_PASSWORD);

            try {
                Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.createToken(authentication, true);
                response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return new ResponseEntity<>(new UserTokenDTO(user, "Bearer " + jwt), HttpStatus.CREATED);
            } catch (AuthenticationException exception) {
                return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
            }
        }
    }

    private void sendEmail(HttpServletRequest request, User user) {
        String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context
        mailService.sendCreationEmail(user, baseUrl);
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserVM> updateUser(@RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
        }
        userService.updateUser(managedUserVM.getId(), managedUserVM.getLogin(), managedUserVM.getFirstName(),
            managedUserVM.getLastName(), managedUserVM.getEmail(), managedUserVM.isActivated(),
            managedUserVM.getLangKey(), managedUserVM.getAuthorities());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()))
            .body(new ManagedUserVM(userService.getUserWithAuthorities(managedUserVM.getId())));
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<ManagedUserVM>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAllWithAuthorities(pageable);
        List<ManagedUserVM> managedUserVMs = page.getContent().stream()
            .map(ManagedUserVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserVMs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<ManagedUserVM> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
                .map(ManagedUserVM::new)
                .map(managedUserVM -> new ResponseEntity<>(managedUserVM, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
}
