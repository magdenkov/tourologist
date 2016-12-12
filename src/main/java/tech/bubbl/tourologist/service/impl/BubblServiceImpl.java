package tech.bubbl.tourologist.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.User;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.repository.BubblRepository;
import tech.bubbl.tourologist.repository.TourBubblRepository;
import tech.bubbl.tourologist.repository.TourRepository;
import tech.bubbl.tourologist.repository.UserRepository;
import tech.bubbl.tourologist.security.AuthoritiesConstants;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.mapper.BubblMapper;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static tech.bubbl.tourologist.web.rest.TourResource.GEODETIC_CALCULATOR;

/**
 * Service Implementation for managing Bubbl.
 */
@Service
public class BubblServiceImpl implements BubblService{

    private final Logger log = LoggerFactory.getLogger(BubblServiceImpl.class);

    @Inject
    private BubblRepository bubblRepository;

    @Inject
    private BubblMapper bubblMapper;

    @Inject
    private PayloadService payloadService;

    @Inject
    private TourBubblRepository tourBubblRepository;

    @Inject
    private TourRepository tourRepository;

    @Inject
    private GeoApiContext geoApiContext;
    @Inject
    private UserRepository userRepository;

    /**
     * Save a bubbl.
     *
     * @param bubblDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public BubblDTO save(BubblDTO bubblDTO) {
        log.debug("Request to save Bubbl : {}", bubblDTO);
        Bubbl bubbl = bubblMapper.bubblDTOToBubbl(bubblDTO);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        bubbl.setUser(user);
        bubbl.setStatus(Status.DRAFT);
        bubbl = bubblRepository.save(bubbl);
        BubblDTO result = bubblMapper.bubblToBubblDTO(bubbl);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<FullTourBubblNumberedDTO> findAll(Pageable pageable, Status status, Long userId) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", status, userId);
        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        Specification<Bubbl> specification = Specifications.where(new Specification<Bubbl>() {
            @Override
            public Predicate toPredicate(Root<Bubbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (userId != null && isAdmin) { // only admins can see other users tours
                    predicates.add(cb.equal(root.get("user").get("id"), userId));
                }
                if (!isAdmin) {   // simple users can see only their own tours
                    predicates.add(cb.equal(root.get("user").get("login"), SecurityUtils.getCurrentUserLogin()));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        Page<Bubbl> result = bubblRepository.findAll(specification, pageable);
        return result.map(bubbl -> new FullTourBubblNumberedDTO(bubbl, 0));
    }

    /**
     *  Get one bubbl by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FullTourBubblNumberedDTO findOne(Long id) {
        log.debug("Request to get Bubbl : {}", id);
        Bubbl bubbl = bubblRepository.findOneWithEagerRelationships(id);
        return new FullTourBubblNumberedDTO(bubbl,0);
    }

    /**
     *  Delete the  bubbl by id.
     *
     *  @param id the id of the entity
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Bubbl : {}", id);
        Bubbl bubbl = bubblRepository.findOne(id);
        Optional.ofNullable(bubbl)
            .orElseThrow(() -> new EntityNotFoundException("Bubbls w/ id was not found" + id));
         // delete all payloads

        bubbl.getPayloads().stream().forEach(payload -> {
                payloadService.deleteWithoutTransaction(payload);});

//        bubbl.getTourBubbls().stream().forEach(tourBubbl -> {
//            tourBubblRepository.delete(tourBubbl);
//        });

        // TODO: 07.12.2016  1) handle bubbl order number changing in all tours (minus one)
        //  2) recalculate all routes in these tours

        bubblRepository.delete(id);

//        Set<TourBubbl> tourBubbls = tourBubblRepository.findByBubbl(bubbl);
//
//        tourBubbls.stream().forEach(tourBubbl -> {
//            // refresh cache
//            tourBubbl.getTour().getTourBubbls().remove(tourBubbl);
//            tourRepository.save(tourBubbl.getTour());
//        });

    }

    @Transactional(readOnly = true)
    @Override
    public List<Bubbl> findBubblsSurprise(Double curLat, Double curLng, Double radius) {

        Specification<Bubbl> specification = Specifications.where(new Specification<Bubbl>() {
            @Override
            public Predicate toPredicate(Root<Bubbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // write fetch join for interests here
                return cb.and(cb.equal(root.get("status"), Status.APPROVED));
            }
        });


        List<Bubbl> bubbls = bubblRepository.findAll();

        // TODO: 08.12.2016 PERFORM SEARCH and order with hibernate search and spatial

        if (curLat != null && curLng != null && radius != null) {
            GlobalPosition userLocation = new GlobalPosition(curLat, curLng, 0.0);
            bubbls = bubbls.stream()
            .filter(bubbl -> {
                if (bubbl.getLng() == null || bubbl.getLat() == null) {
                    return false;
                }
                GlobalPosition tourLocation = new GlobalPosition(bubbl.getLat(), bubbl.getLng(), 0.0);
                Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, tourLocation).getEllipsoidalDistance();
                return distance < radius;
            })
            .map(bubbl -> {
                bubbl.getInterests().size(); // initialise
                return bubbl;
            })
            .collect(Collectors.toList());
        }

        return bubbls;

//        AtomicInteger i = new AtomicInteger(0);
//        return bubbls.stream()
//            .sorted(new SortBubbls(new Bubbl().lat(curLat).lng(curLng)))
//            .map(bubbl -> new FullTourBubblNumberedDTO(bubbl, i.getAndIncrement()))
//            .collect(Collectors.toList());


    }

    @Override
    public List<String> reverseGeocode(Double lat, Double lng) {
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.newRequest(geoApiContext).latlng(new LatLng(lat, lng)).await();
        } catch (Exception e) {
            log.error("failed to decode location from google services");
        }

        if (results == null || results.length == 0) {
            return  new ArrayList<>();
        }

        List<String> address = Arrays.stream(results)
            .map(geocodingResult -> geocodingResult.formattedAddress)
            .collect(Collectors.toList());

        return address;
    }


}
