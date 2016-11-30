package tech.bubbl.tourologist.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.apache.commons.lang3.ArrayUtils;
import tech.bubbl.tourologist.domain.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        tourRepository.delete(id);
    }

    @Override
    @Transactional
    public TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO) {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Tour tour = tourRepository.save(tourDTO.createTour(user));

        List<LatLng> bubblsLatLngList = tourDTO.getBubbls().stream()
            .sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
            .map(createTourBubblDTO -> {
                TourBubbl tourBubbl = new TourBubbl();
                tourBubbl.setOrderNumber(createTourBubblDTO.getOrderNumber());
                tourBubbl.setTour(tour);
                Bubbl bubbl = bubblRepository.findOne(createTourBubblDTO.getBubblId());
                tourBubbl.setBubbl(bubbl);
                tourBubblRepository.save(tourBubbl);
                return new LatLng(bubbl.getLat(), bubbl.getLng());
            }).collect(Collectors.toList());

        tour.setLat(bubblsLatLngList.get(0).lat);
        tour.setLng(bubblsLatLngList.get(0).lng);


        // TODO: 29.11.2016   calc Route Lenght!!!!!


        Tour finalTour = tourRepository.save(tour);

        if (bubblsLatLngList.size() < 2) {
            // do not calc route
            return new TourFullDTO(finalTour, tourImageMapper, tourRoutePointMapper);
        }

        LatLng origin = bubblsLatLngList.get(0);
        LatLng destination = bubblsLatLngList.get(bubblsLatLngList.size() - 1);
        LatLng [] wayPoints = ArrayUtils.subarray(bubblsLatLngList.toArray(new LatLng[bubblsLatLngList.size()]), 1, bubblsLatLngList.size() - 2);

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
            log.error("Error occurred while creating route for tour {}, Error Message {} ", finalTour.getName(), e.getMessage());
        }


            DirectionsRoute route = result.routes[0];
            List<LatLng> path = route.overviewPolyline.decodePath();
            AtomicInteger i = new AtomicInteger(0);

            Set<TourRoutePoint> routePoints = path.stream().map(latLng -> new TourRoutePoint()
                .tour(finalTour)
                .lat(latLng.lat)
                .lng(latLng.lng)
                .orderNumber(i.getAndIncrement()))
                .collect(Collectors.toSet());

//            Set<TourRoutePoint> routePoints = Stream.of(route.legs).map(directionsLeg ->
//                 new TourRoutePoint()
//                    .tour(finalTour)
//                    .orderNumber(i.getAndIncrement())
//                    .lat(directionsLeg.endLocation.lat)
//                    .lng(directionsLeg.endLocation.lng)
//            ).collect(Collectors.toSet());

            List<TourRoutePoint> tourRoutePointsSaved = tourRoutePointRepository.save(routePoints);
            finalTour.setTourRoutePoints(new HashSet<>(tourRoutePointsSaved));

            // TODO: 28.11.2016 save duration and distance

        return new TourFullDTO(finalTour, tourImageMapper, tourRoutePointMapper);
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
