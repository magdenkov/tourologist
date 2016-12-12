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
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.repository.*;
import tech.bubbl.tourologist.security.AuthoritiesConstants;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.service.dto.tour.CreateFixedTourDTO;
import tech.bubbl.tourologist.service.dto.tour.CreateTourBubblDTO;
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
import tech.bubbl.tourologist.service.util.SortBubbls;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
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

    @Inject
    private BubblService bubblService;




    @Transactional
    public TourDTO save(TourDTO tourDTO) {
        log.debug("Request to save Tour : {}", tourDTO);
        Tour tour = tourMapper.tourDTOToTour(tourDTO);
        tour = tourRepository.save(tour);
        TourDTO result = tourMapper.tourToTourDTO(tour);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<GetAllToursDTO> findAllTours(Pageable pageable, TourType type, Status status, Long userId) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", type, status, userId);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        Specification<Tour> specification = getSearchTourSpecification(type, status, userId);


        Page<Tour> result = tourRepository.findAll(specification, pageable);
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

        Set<TourBubbl> tourBubbls = tourBubblRepository.findByTour(tour);

        tour.getTourBubbls().removeAll(tourBubbls);

        tourRepository.save(tour);

        tourBubblRepository.deleteByTour(tour);

        tourRepository.delete(id);
    }

    @Override
    @Transactional
    public TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO, LatLng origin, LatLng destination, TourType type) {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Tour tourFromDto;
        if (tourDTO.getId() == null) {
            // creating new tour
             tourFromDto = tourRepository.save(tourDTO.createTour(user, type));
        } else {
            // updating existing tour
            tourFromDto = tourRepository.findOneWithEagerRelationships(tourDTO.getId());
            Optional.ofNullable(tourFromDto)
                .orElseThrow(() -> new EntityNotFoundException("tour was not found by id" + tourDTO.getId()));
            tourFromDto.getTourBubbls().clear();
            tourFromDto.setName(tourDTO.getName());
            tourFromDto.setDescription(tourDTO.getDescription());
            tourFromDto.setTourType(type);
            tourFromDto.setUser(user);
            tourFromDto.setLastModified(ZonedDateTime.now());
            tourFromDto.setInterests(tourDTO.getInterests().stream()
                .map(interestDTO -> new Interest().id(interestDTO.getId()))
                .collect(Collectors.toSet()));
            tourBubblRepository.deleteByTour(tourFromDto);
//            tourRoutePointRepository.deleteByTour(tourFromDto);
//            tourFromDto.getTourRoutePoints().clear();
//            tourRepository.save(tourFromDto);
        }

        final Tour tour = tourFromDto;
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
                tour.getTourBubbls().add(tourBubbl);
                return new LatLng(bubbl.getLat(), bubbl.getLng());
            }).collect(Collectors.toList());

        tour.setLat(bubblsLatLngList.get(0).lat);
        tour.setLng(bubblsLatLngList.get(0).lng);

        LatLng[] wayPoints = getWayPoints(origin, destination, bubblsLatLngList);

        if (origin == null) {
            origin = bubblsLatLngList.get(0);
        }

        if (destination == null) {
            destination = bubblsLatLngList.get(bubblsLatLngList.size() - 1);
        }

        DirectionsResult result = getDirectionsFromGoogle(origin, destination, tour, wayPoints);

        if (result == null || result.routes.length == 0) {
            tourRepository.save(tour);
            return new TourFullDTO(tour, tourImageMapper, tourRoutePointMapper);
        }

        DirectionsRoute route = calcLengthOfTour(tour, result.routes[0]);

        saveRoutePoints(route, tour);
        tourRepository.save(tour);

        return new TourFullDTO(tour, tourImageMapper, tourRoutePointMapper);
    }



    private LatLng[] getWayPoints(LatLng origin, LatLng destination, List<LatLng> bubblsLatLngList) {
        LatLng [] wayPoints = null;
        if (origin == null && destination == null && bubblsLatLngList.size() > 2) {
            // exclude first and last bubbls
            wayPoints = ArrayUtils.subarray(bubblsLatLngList.toArray(new LatLng[bubblsLatLngList.size()]), 1, bubblsLatLngList.size() - 2);
        } else {
            wayPoints = bubblsLatLngList.toArray(new LatLng[bubblsLatLngList.size()]);
        }
        return wayPoints;
    }

    private void saveRoutePoints(DirectionsRoute route, Tour finalTour) {
        List<LatLng> path = route.overviewPolyline.decodePath();
        AtomicInteger i = new AtomicInteger(0);
        Set<TourRoutePoint> routePoints = path.stream().map(latLng -> new TourRoutePoint()
            .tour(finalTour)
            .lat(latLng.lat)
            .lng(latLng.lng)
            .orderNumber(i.getAndIncrement()))
            .collect(Collectors.toSet());

        List<TourRoutePoint> tourRoutePointsSaved = tourRoutePointRepository.save(routePoints);
        finalTour.getTourRoutePoints().clear();
        finalTour.getTourRoutePoints().addAll(new HashSet<>(tourRoutePointsSaved));
    }

    private DirectionsResult getDirectionsFromGoogle(LatLng origin, LatLng destination, Tour tour, LatLng[] wayPoints) {
        DirectionsResult result = null;
        try {
             result = DirectionsApi.newRequest(geoApiContext)
                .origin(origin)
                .destination(destination)
                .mode(TravelMode.WALKING)
                .units(Unit.METRIC)
                .waypoints(convertLatLngToString(wayPoints))
                .optimizeWaypoints(false)   // otherwise it will change original order of Bubbls!
                .await();
        } catch (Exception e) {
            log.error("Error occurred while creating route for tour {}, Error Message {} ", tour.getName(), e.getMessage());
        }
        return result;
    }


    public <Entity> Predicate getDistancePredicate(Path<Entity> root, CriteriaBuilder cb) {
        try {
            double x = Double.valueOf("23.45");
            double y = Double.valueOf("23.45");
            int r = (int) Math.round(Double.valueOf("23.45") * 1000d);
            return cb.isTrue(cb.function("IN_RADIUS", Boolean.class, root.get("x"), root.get("y"), cb.literal(x), cb.literal(y), cb.literal(r)));
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    @Transactional(readOnly = true)
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
                if (tour.getLng() == null || tour.getLat() == null) {
                    return false;
                }
                GlobalPosition tourLocation = new GlobalPosition(tour.getLat(), tour.getLng(), 0.0);
                Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, tourLocation).getEllipsoidalDistance();
                return distance < radius;
            }).collect(Collectors.toList());
        }

        return tours.stream().map(GetAllToursDTO::new)
            .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<TourFullDTO> getDIYTours(Double curLat, Double curLng, Double tarLat, Double tarLng) {

        GlobalPosition userLocation = new GlobalPosition(curLat, curLng, 0.0);
        GlobalPosition targetLocation = new GlobalPosition(tarLat, tarLng, 0.0);
        Double radius = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, targetLocation).getEllipsoidalDistance();

        List<Bubbl> bubblsAround = bubblService.findBubblsSurprise(curLat, curLng, radius);
        if (bubblsAround.isEmpty()) {
            return new ArrayList<>();
        }

        AtomicInteger i = new AtomicInteger(0);
        List<CreateTourBubblDTO> bubblsAroundOrdered = bubblsAround.stream()
            .sorted(new SortBubbls(new Bubbl().lat(curLat).lng(curLng)))
            .map(bubbl -> new CreateTourBubblDTO(i.getAndIncrement(), bubbl.getId()))
            .collect(Collectors.toList());

        // TODO: 08.12.2016 implement more complicated algoritm to pick up bubbls
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        CreateFixedTourDTO createFixedTourDTO = new CreateFixedTourDTO();
        createFixedTourDTO.setName("DIY tour by " + user.getFullName());
        List<String> origin = bubblService.reverseGeocode(curLat, curLng);
        List<String> destination = bubblService.reverseGeocode(tarLat, tarLng);
        createFixedTourDTO.setDescription("DIY tour from " + origin.get(0) + " to destination " + destination.get(0));


//        final Tour tour = tourRepository.save(createFixedTourDTO.createTour(user, TourType.DIY));
        LatLng originLL = new LatLng(curLat, curLng);
        LatLng destinationLL = new LatLng(tarLat, tarLng);

        List<TourFullDTO> resp = new ArrayList<>();
        if (bubblsAroundOrdered.size() <= 2) {
            createFixedTourDTO.setBubbls(bubblsAroundOrdered);
            resp.add(saveFixedTour(createFixedTourDTO, originLL, destinationLL, TourType.DIY));
        } else {
            String tourName = createFixedTourDTO.getName();
            AtomicInteger i2 = new AtomicInteger(1);
            createFixedTourDTO.setBubbls(bubblsAroundOrdered.stream()
                .filter(createTourBubblDTO -> createTourBubblDTO.getOrderNumber() % 2 == 0 )
                .map(createTourBubblDTO -> createTourBubblDTO.orderNumber(i2.getAndIncrement()))
                .collect(Collectors.toList()));
            createFixedTourDTO.setName(tourName + " V1 " + ZonedDateTime.now().toString());
            resp.add(saveFixedTour(createFixedTourDTO, originLL, destinationLL, TourType.DIY));

            AtomicInteger i3 = new AtomicInteger(1);
            createFixedTourDTO.setBubbls(bubblsAroundOrdered.stream()
                .filter(createTourBubblDTO -> createTourBubblDTO.getOrderNumber() % 2 != 0 )
                .map(createTourBubblDTO -> createTourBubblDTO.orderNumber(i3.getAndIncrement()))
                .collect(Collectors.toList()));
            createFixedTourDTO.setName(tourName + " V2 " + ZonedDateTime.now().toString());
            resp.add(saveFixedTour(createFixedTourDTO, originLL, destinationLL, TourType.DIY));
        }

        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllToursDTO> findAllTours(Pageable pageable, TourType type, Status status) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", type, status);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Long userId = user.getId();
        Specification<Tour> specification = getSearchTourSpecification(type, status, userId);


        Page<Tour> result = tourRepository.findAll(specification, pageable);
        return result.map(GetAllToursDTO::new);
    }

    private Specifications<Tour> getSearchTourSpecification(final TourType type, final Status status, final Long userId) {
        return Specifications.where(new Specification<Tour>() {
            @Override
            public Predicate toPredicate(Root<Tour> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (type != null) {
                    predicates.add(cb.equal(root.get("tourType"), type));
                }
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

    private DirectionsRoute calcLengthOfTour(Tour tour, DirectionsRoute route1) {
        long routeLength = Arrays.stream(route1.legs)
            .mapToLong(value -> value.distance.inMeters)
            .sum();
        tour.setRouteLength((int) routeLength);
        return route1;
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
