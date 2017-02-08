package tech.bubbl.tourologist.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import org.gavaghan.geodesy.GlobalPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.mapper.BubblMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Bubbl.
 */
@Service
public class BubblServiceImpl implements BubblService {

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

    @Inject
    private EntityManager entityManager;

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
        log.debug("Request to get all bubbles by params  status {}, userId {}", status, userId);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        Specification<Bubbl> specification = getBubblsSpecification(status, userId);
        Page<Bubbl> result = bubblRepository.findAll(specification, pageable);
        return result.map(bubbl -> new FullTourBubblNumberedDTO(bubbl, null, null));
    }

    @Transactional()
    public Page<FullTourBubblNumberedDTO> findAllInRadius(Pageable pageable, Status status, Long userId,
            Double centerLat, Double centerLng, Double radius) {
        log.debug("Request to get all bubbles by params status {}, userId {}, curLat {}, curLng {}, radius {}", status,
                userId, centerLat, centerLng, radius);

        final double ELEVATION = 0.0;
        GlobalPosition centerLocation = new GlobalPosition(centerLat, centerLng, ELEVATION);

        log.debug("centerLocation {}", centerLocation);

        Sort sort = new Sort(Sort.Direction.ASC, "distanceToBubbl");
        Pageable page = new PageRequest(0, 100, sort);

        setCurrentLatAndLngInDb(centerLat, centerLng);

        log.debug("setCurrentLatAndLngInDb");

        Specification<Bubbl> specification = Specifications.where(new Specification<Bubbl>() {
            @Override
            public Predicate toPredicate(Root<Bubbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (userId != null) {
                    predicates.add(cb.equal(root.get("user").get("id"), userId));
                }

                predicates.add(cb.isTrue(cb.function("IN_RADIUS", Boolean.class, root.get("lat"), root.get("lng"),
                        cb.literal(centerLocation.getLatitude()), cb.literal(centerLocation.getLongitude()),
                        cb.literal(radius))));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        log.debug("specification {}", specification);

        Page<Bubbl> result = bubblRepository.findAll(specification, page);
        return result.map(bubbl -> new FullTourBubblNumberedDTO(bubbl, null, null));
    }

    private Specifications<Bubbl> getBubblsSpecification(final Status status, final Long userId) {
        return Specifications.where(new Specification<Bubbl>() {
            @Override
            public Predicate toPredicate(Root<Bubbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (status != null) {
                    predicates.add(cb.equal(root.get("status"), status));
                }
                if (userId != null) {
                    predicates.add(cb.equal(root.get("user").get("id"), userId));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
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
        return new FullTourBubblNumberedDTO(bubbl, null, null);
    }

    /**
     *  Delete the  bubbl by id.
     *
     * @param ids the id of the entity
     */
    @Transactional
    public void delete(List<Long> ids) {
        log.debug("Request to delete Bubbl : {}", ids);
        for (Long id: ids) {
            Bubbl bubbl = bubblRepository.findOne(id);
//        Optional.ofNullable(bubbl)
//            .orElseThrow(() -> new EntityNotFoundException("Bubbls w/ id was not found" + id));
            if (bubbl == null) {
                continue;
            }

            bubbl.getPayloads().stream().forEach(payload -> {
                payloadService.deleteWithoutTransaction(payload);
            });

            bubblRepository.delete(id);
        }
    }

    public void setCurrentLatAndLngInDb(Double curLat, Double curLng) {
        if (curLat != null && curLng != null) {
            Query setLat = entityManager.createNativeQuery("SET @curLat=:curLat").setParameter("curLat", curLat);
            setLat.executeUpdate();
            Query setLng = entityManager.createNativeQuery("SET @curLng=:curLng").setParameter("curLng", curLng);
            setLng.executeUpdate();
        }
    }

    public void clearCurrentLatAndLngInDb() {
            Query setLat = entityManager.createNativeQuery("SET @curLat=NULL");
            setLat.executeUpdate();
            Query setLng = entityManager.createNativeQuery("SET @curLng=NULL ");
            setLng.executeUpdate();
    }

    @Transactional
    @Override
    public Page<Bubbl> findBubblsSurprise(Double curLat, Double curLng, Pageable pageable, List<Long> exceptBubblIds) {

        setCurrentLatAndLngInDb(curLat, curLng);

        Specification<Bubbl> specification = Specifications.where(new Specification<Bubbl>() {
            @Override
            public Predicate toPredicate(Root<Bubbl> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (exceptBubblIds != null && !exceptBubblIds.isEmpty()) {
                    predicates.add(cb.and(cb.not(root.get("id").in(exceptBubblIds))));
                }
//                predicates.add(cb.and(cb.equal(root.get("status"), Status.APPROVED)));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        });

        Page<Bubbl> bubblPage = bubblRepository.findAll(specification, pageable);
        bubblPage.map(bubbl -> bubbl.getInterests().size());

        return bubblPage;

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
            List<String> address =  new ArrayList<>();
            address.add("unknown adress");
            return address;
        }

        List<String> address = Arrays.stream(results)
            .map(geocodingResult -> geocodingResult.formattedAddress)
            .collect(Collectors.toList());

        return address;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<FullTourBubblNumberedDTO> findOnlyMyBubbls(Pageable pageable, Status status) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", status);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Specification<Bubbl> specification = getBubblsSpecification(status, user.getId());
        Page<Bubbl> result = bubblRepository.findAll(specification, pageable);
        return result.map(bubbl -> new FullTourBubblNumberedDTO(bubbl, null, null));
    }


}
