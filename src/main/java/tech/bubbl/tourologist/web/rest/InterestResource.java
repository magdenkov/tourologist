package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.InterestService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.InterestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Interest.
 */
@RestController
@RequestMapping("/api")
public class InterestResource {

    private final Logger log = LoggerFactory.getLogger(InterestResource.class);
        
    @Inject
    private InterestService interestService;

    /**
     * POST  /interests : Create a new interest.
     *
     * @param interestDTO the interestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interestDTO, or with status 400 (Bad Request) if the interest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interests")
    @Timed
    public ResponseEntity<InterestDTO> createInterest(@Valid @RequestBody InterestDTO interestDTO) throws URISyntaxException {
        log.debug("REST request to save Interest : {}", interestDTO);
        if (interestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("interest", "idexists", "A new interest cannot already have an ID")).body(null);
        }
        InterestDTO result = interestService.save(interestDTO);
        return ResponseEntity.created(new URI("/api/interests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("interest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interests : Updates an existing interest.
     *
     * @param interestDTO the interestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interestDTO,
     * or with status 400 (Bad Request) if the interestDTO is not valid,
     * or with status 500 (Internal Server Error) if the interestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interests")
    @Timed
    public ResponseEntity<InterestDTO> updateInterest(@Valid @RequestBody InterestDTO interestDTO) throws URISyntaxException {
        log.debug("REST request to update Interest : {}", interestDTO);
        if (interestDTO.getId() == null) {
            return createInterest(interestDTO);
        }
        InterestDTO result = interestService.save(interestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("interest", interestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interests : get all the interests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of interests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/interests")
    @Timed
    public ResponseEntity<List<InterestDTO>> getAllInterests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Interests");
        Page<InterestDTO> page = interestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/interests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /interests/:id : get the "id" interest.
     *
     * @param id the id of the interestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/interests/{id}")
    @Timed
    public ResponseEntity<InterestDTO> getInterest(@PathVariable Long id) {
        log.debug("REST request to get Interest : {}", id);
        InterestDTO interestDTO = interestService.findOne(id);
        return Optional.ofNullable(interestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /interests/:id : delete the "id" interest.
     *
     * @param id the id of the interestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interests/{id}")
    @Timed
    public ResponseEntity<Void> deleteInterest(@PathVariable Long id) {
        log.debug("REST request to delete Interest : {}", id);
        interestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("interest", id.toString())).build();
    }

}
