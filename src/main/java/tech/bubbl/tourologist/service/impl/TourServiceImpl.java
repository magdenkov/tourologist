package tech.bubbl.tourologist.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.apache.commons.lang3.ArrayUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.repository.*;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.service.dto.tour.CreateFixedTourDTO;
import tech.bubbl.tourologist.service.dto.tour.GetAllToursDTO;
import tech.bubbl.tourologist.service.dto.TourDTO;
import tech.bubbl.tourologist.service.dto.tour.TourFullDTO;
import tech.bubbl.tourologist.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static tech.bubbl.tourologist.web.rest.TourResource.GEODETIC_CALCULATOR;

/**
 * Service Implementation for managing Tour.
 */
@Service
public class TourServiceImpl implements TourService{

    private final Logger log = LoggerFactory.getLogger(TourServiceImpl.class);

    @Inject
    private TourRepository tourRepository;

    @Inject
    private TourMapper tourMapper;

    @Inject
    private InterestMapper interestMapper;

    @Inject
    private BubblMapper bubblMapper;

    @Inject
    private TourImageMapper tourImageMapper;

    @Inject
    private TourRoutePointMapper tourRoutePointMapper;

    @Inject
    private TourBubblMapper tourBubblMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TourBubblRepository tourBubblRepository;

    @Inject
    private GeoApiContext geoApiContext;

    @Inject
    private BubblRepository bubblRepository;

    @Inject
    private TourRoutePointRepository tourRoutePointRepository;


    @Transactional
    public TourDTO save(TourDTO tourDTO) {
        log.debug("Request to save Tour : {}", tourDTO);
        Tour tour = tourMapper.tourDTOToTour(tourDTO);
        tour = tourRepository.save(tour);
        TourDTO result = tourMapper.tourToTourDTO(tour);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<GetAllToursDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tours");
        Page<Tour> result = tourRepository.findAll(pageable);
        return result.map(GetAllToursDTO::new);
    }

    @Transactional(readOnly = true)
    public TourFullDTO findOne(Long id) {
        log.debug("Request to get Tour : {}", id);
        Tour tour = tourRepository.findOneWithEagerRelationships(id);
        return new TourFullDTO(tour, tourImageMapper, tourRoutePointMapper);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Tour : {}", id);
        Tour tour = tourRepository.findOne(id);
        Optional.ofNullable(tour)
            .orElseThrow(() -> new EntityNotFoundException("Tour with id was not found " + id));

        tourBubblRepository.deleteByTour(tour);

        tourRepository.delete(id);
    }

    @Override
    @Transactional
    public TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO) {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        final Tour tour = tourRepository.save(tourDTO.createTour(user));

        List<LatLng> bubblsLatLngList = tourDTO.getBubbls().stream()
            .sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
            .map(createTourBubblDTO -> {
                Bubbl bubbl = bubblRepository.findOne(createTourBubblDTO.getBubblId());
                if (bubbl == null) {
                    log.error("Bubbl w/ id {} was not found while creating tour ", createTourBubblDTO.getBubblId());
                    return null;
                }
                TourBubbl tourBubbl = new TourBubbl();
                tourBubbl.setOrderNumber(createTourBubblDTO.getOrderNumber());
                tourBubbl.setTour(tour);
                tourBubbl.setBubbl(bubbl);
                tourBubblRepository.save(tourBubbl);
                return new LatLng(bubbl.getLat(), bubbl.getLng());
            }).collect(Collectors.toList());

        tour.setLat(bubblsLatLngList.get(0).lat);
        tour.setLng(bubblsLatLngList.get(0).lng);

        LatLng origin = bubblsLatLngList.get(0);
        LatLng destination = bubblsLatLngList.get(bubblsLatLngList.size() - 1);

        LatLng [] wayPoints = null;
        if (bubblsLatLngList.size() > 2) {
            wayPoints = ArrayUtils.subarray(bubblsLatLngList.toArray(new LatLng[bubblsLatLngList.size()]), 1, bubblsLatLngList.size() - 2);
        }

        DirectionsResult result = null;
        try {
             result = DirectionsApi.newRequest(geoApiContext)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.WALKING)
                .units(Unit.METRIC).waypoints()
                .waypoints(convertLatLngToString(wayPoints))
                .optimizeWaypoints(false)   // otherwise it will change original order of Bubbls!
                .await();
        } catch (Exception e) {
            log.error("Error occurred while creating route for tour {}, Error Message {} ", tour.getName(), e.getMessage());
        }

        if (result == null) {
            tourRepository.save(tour);
            return new TourFullDTO(tour, tourImageMapper, tourRoutePointMapper);
        }

        DirectionsRoute route = calcLengthOfTour(tour, result.routes[0]);
        Tour finalTour = tourRepository.save(tour);

        List<LatLng> path = route.overviewPolyline.decodePath();
        AtomicInteger i = new AtomicInteger(0);
        Set<TourRoutePoint> routePoints = path.stream().map(latLng -> new TourRoutePoint()
            .tour(finalTour)
            .lat(latLng.lat)
            .lng(latLng.lng)
            .orderNumber(i.getAndIncrement()))
            .collect(Collectors.toSet());

        List<TourRoutePoint> tourRoutePointsSaved = tourRoutePointRepository.save(routePoints);
        finalTour.setTourRoutePoints(new HashSet<>(tourRoutePointsSaved));

        return new TourFullDTO(finalTour, tourImageMapper, tourRoutePointMapper);
    }

    @Override
    public List<GetAllToursDTO> findAllFixed(Double curLat, Double curLng, Double radius) {
        Specification<Tour> specification = Specifications.where(new Specification<Tour>() {
            @Override
            public Predicate toPredicate(Root<Tour> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
               return cb.and(cb.equal(root.get("tourType"), TourType.FIXED ));
            }
        });
        List<Tour> tours = tourRepository.findAll(specification);

        // TODO: 08.12.2016  Filter by hibernate search and spatial in criteria
        // see build gradle compile group: 'org.hibernate', name: 'hibernate-search-orm', version: '5.3.0.Final' downgrade hibernate core to 4.3.11

        if (curLat != null && curLng != null && radius != null) {
            GlobalPosition userLocation = new GlobalPosition(curLat, curLng, 0.0);
            tours = tours.stream().filter(tour -> {
                GlobalPosition tourLocation = new GlobalPosition(tour.getLat(), tour.getLng(), 0.0);
                Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, tourLocation).getEllipsoidalDistance();
                return distance < radius;
            }).collect(Collectors.toList());
        }

        return tours.stream().map(GetAllToursDTO::new)
            .collect(Collectors.toList());

    }

    private DirectionsRoute calcLengthOfTour(Tour tour, DirectionsRoute route1) {
        DirectionsRoute route = route1;
        long routeLength = Arrays.stream(route.legs)
            .mapToLong(value -> value.distance.inMeters)
            .sum();
        tour.setRouteLength((int) routeLength);
        return route;
    }

    private String[] convertLatLngToString(LatLng... waypoints) {
        if (waypoints == null || waypoints.length == 0) {
            return null;
        }
        int length = waypoints.length;
        String[] waypointStrings = new String[length];
        for (int i = 0; i < length; i++) {
            waypointStrings[i] = waypoints[i].toString();
        }
        return waypointStrings;
    }




}
