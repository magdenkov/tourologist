package tech.bubbl.tourologist.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.apache.commons.lang3.ArrayUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.repository.*;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.service.dto.tour.*;
import tech.bubbl.tourologist.service.dto.TourDTO;
import tech.bubbl.tourologist.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import tech.bubbl.tourologist.service.util.SortBubbls;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
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
    /**
     * This is maximum amount of way points that is allowed by google free account
     * see https://developers.google.com/maps/documentation/directions/usage-limits
     */
    public static final int MAX_BUBBLS_ALLOWED_BY_GOOGLE = 20;
    public static final double ELEVATION = 0.0;

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

    @Inject
    private TourDownloadRepository tourDownloadRepository;

    @Inject
    private EntityManager entityManager;



    @Transactional
    public Page<GetAllToursDTO> findAllToursByUSerId(Pageable pageable, TourType type, Status status, Long userId, String name) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", type, status, userId);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        setUserNameInDb();

        Specification<Tour> specification = getSearchTourSpecification(type, status, userId, name);


        Page<Tour> result = tourRepository.findAll(specification, pageable);
        return result.map(GetAllToursDTO::new);
    }

    @Transactional
    public TourFullDTO findOne(Long id) {
        log.debug("Request to get Tour : {}", id);
        setUserNameInDb();

        Tour tour = tourRepository.findOneWithEagerRelationships(id);
        return new TourFullDTO(tour, tourImageMapper, tourRoutePointMapper);
    }

    @Override
    @Transactional
    public List<GetAllToursDTO> findAllFixed(Double curLat, Double curLng, Double radius, String name) {
        Specification<Tour> specification = Specifications.where(new Specification<Tour>() {
            @Override
            public Predicate toPredicate(Root<Tour> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(cb.like(root.get("name"), "%" + name + "%"));
                }
                predicates.add(cb.equal(root.get("tourType"), TourType.FIXED ));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        setUserNameInDb();
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
    public Page<TourDownload> findMyFavoritesTours(Pageable pageable, TourType type, Status status, String name) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", type, status);

        Specification<TourDownload> specification = Specifications.where(new Specification<TourDownload>() {
            @Override
            public Predicate toPredicate(Root<TourDownload> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (type != null) {
                    predicates.add(cb.equal(root.get("tour").get("tourType"), type));
                }
                if (status != null) {
                    predicates.add(cb.equal(root.get("status").get("status"), status));
                }
                if (name != null) {
                    predicates.add(cb.like(root.get("name"), "%" + name + "%"));
                }

                predicates.add(cb.equal(root.get("user").get("login"), SecurityUtils.getCurrentUserLogin()));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

        setUserNameInDb();
        return tourDownloadRepository.findAll(specification, pageable);
    }

    @Override
    public List<RoutePointDTO> recalculateRoute(RecalculateRoutePointsDTO recalculateRoutePointsDTO) {
        DirectionsResult result = getDirectionsFromGoogle(recalculateRoutePointsDTO.getOrigin(),
            recalculateRoutePointsDTO.getDestination(),
            getWayPoints(null, null, recalculateRoutePointsDTO.getBubblsToCover()));

        List<RoutePointDTO> resp =  new ArrayList<>();
        if (result == null || result.routes.length == 0) {
            resp.add(new RoutePointDTO(recalculateRoutePointsDTO.getOrigin(), 0));
            AtomicInteger i = new AtomicInteger(1);
            recalculateRoutePointsDTO.getBubblsToCover().stream().forEach(latLng -> {
                resp.add(new RoutePointDTO(latLng, i.getAndIncrement()));
            });
            resp.add(new RoutePointDTO(recalculateRoutePointsDTO.getDestination(), i.getAndIncrement()));
            return resp;
        }
        AtomicInteger i =  new AtomicInteger(0);
        List<LatLng> latLngs = result.routes[0].overviewPolyline.decodePath();
        latLngs.stream().forEach(latLng -> resp.add(new RoutePointDTO(latLng, i.getAndIncrement())));
        return resp;
    }

    @Override
    @Transactional
    public Page<GetAllToursDTO> findAllMyTours(Pageable pageable, TourType type, Status status, String name) {
        log.debug("Request to get all Tours by params  type {}, status {}, userId {}", type, status);
//        final Boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Long userId = user.getId();

        setUserNameInDb();
        Specification<Tour> specification = getSearchTourSpecification(type, status, userId, name);


        Page<Tour> result = tourRepository.findAll(specification, pageable);
        return result.map(GetAllToursDTO::new);
    }

    @Transactional
    public TourDTO save(TourDTO tourDTO) {
        log.debug("Request to save Tour : {}", tourDTO);
        Tour tour = tourMapper.tourDTOToTour(tourDTO);
        tour = tourRepository.save(tour);
        return tourMapper.tourToTourDTO(tour);
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

        DirectionsResult result = getDirectionsFromGoogle(origin, destination, wayPoints);

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

    private DirectionsResult getDirectionsFromGoogle(LatLng origin, LatLng destination,  LatLng[] wayPoints) {
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
            log.error("Error occurred while creating route , Error Message {} ", e.getMessage());
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

    private GlobalPosition midPoint(double lat1, double lon1, double lat2, double lon2){
        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new GlobalPosition(Math.toDegrees(lat3), Math.toDegrees(lon3), ELEVATION);
    }

    @Override
    @Transactional
    public List<TourFullDTO> getDIYTours(Double curLat, Double curLng, Double tarLat, Double tarLng) {

        Sort sort = new Sort (Sort.Direction.ASC, "distanceToBubbl");
        Pageable page =  new PageRequest(0, MAX_BUBBLS_ALLOWED_BY_GOOGLE * 16 // remove this magic number
            , sort);

        // todo rewrite this with specification is inside radius
        List<Bubbl> bubblsAround = bubblService.findBubblsSurprise(curLat, curLng,  page);

        if (bubblsAround.isEmpty()) {
            return new ArrayList<>();
        }

        GlobalPosition userLocation = new GlobalPosition(curLat, curLng, ELEVATION);
        GlobalPosition targetLocation = new GlobalPosition(tarLat, tarLng, ELEVATION);
        // radius of the internal circle
        Double radius = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, targetLocation).getEllipsoidalDistance() / 2;
        GlobalPosition midPoint = midPoint(curLat, curLng, tarLat, tarLng);

        AtomicInteger i = new AtomicInteger(1);
        List<CreateTourBubblDTO> bubblsAroundOrdered = bubblsAround.stream()
            .filter(bubbl -> {
                // Simplified Algorithm to pick up bubbls created by Anna Miroshnik
                GlobalPosition bubblPosition = new GlobalPosition(bubbl.getLat(), bubbl.getLng(), ELEVATION);
                Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, midPoint, bubblPosition).getEllipsoidalDistance();
                return distance < radius;
            })
            .limit(MAX_BUBBLS_ALLOWED_BY_GOOGLE)
            .map(bubbl -> new CreateTourBubblDTO(i.getAndIncrement(), bubbl.getId()))
            .collect(Collectors.toList());

        if (bubblsAroundOrdered.isEmpty()) {
            return new ArrayList<>();
        }

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        CreateFixedTourDTO createFixedTourDTO = new CreateFixedTourDTO();
        createFixedTourDTO.setName("DIY tour created by " + user.getFullName() + " , creation time " + ZonedDateTime.now());
        List<String> origin = bubblService.reverseGeocode(curLat, curLng);
        List<String> destination = bubblService.reverseGeocode(tarLat, tarLng);
        createFixedTourDTO.setDescription("DIY tour from: " + origin.get(0) + " ,to destination: " + destination.get(0));


        LatLng originLL = new LatLng(curLat, curLng);
        LatLng destinationLL = new LatLng(tarLat, tarLng);

        List<TourFullDTO> resp = new ArrayList<>();
        createFixedTourDTO.setBubbls(bubblsAroundOrdered);
        resp.add(saveFixedTour(createFixedTourDTO, originLL, destinationLL, TourType.DIY));

        return resp;
    }

    private void setUserNameInDb() {
        Query nativeQuery = entityManager.createNativeQuery("SET @userLogin=:userLogin").setParameter("userLogin", SecurityUtils.getCurrentUserLogin());
        nativeQuery.executeUpdate();
    }

    private Specifications<Tour> getSearchTourSpecification(final TourType type, final Status status, final Long userId, String name) {
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

                if (name != null) {
                    predicates.add(cb.like(root.get("name"), "%" + name + "%"));
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
