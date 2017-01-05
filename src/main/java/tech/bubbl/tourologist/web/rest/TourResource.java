package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourCompleted;
import tech.bubbl.tourologist.domain.TourDownload;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.TourDownloadService;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.service.dto.ErrorDTO;
import tech.bubbl.tourologist.service.dto.SuccessTransportObject;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.dto.tour.*;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * REST controller for managing Tour.
 */
@RestController
@RequestMapping("/api")
public class TourResource {

    private final Logger log = LoggerFactory.getLogger(TourResource.class);

    @Inject
    private TourService tourService;

    @Inject
    private BubblService bubblService;

    public static final GeodeticCalculator GEODETIC_CALCULATOR = new GeodeticCalculator();

    @Inject
    private TourDownloadService tourDownloadService;

    @PostMapping("/tours")
    @Timed
    public ResponseEntity<TourDTO> createTour(@Valid @RequestBody TourDTO tourDTO) throws URISyntaxException {
        log.debug("REST request to save Tour : {}", tourDTO);
        if (tourDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tour", "idexists", "A new tour cannot already have an ID")).body(null);
        }
        TourDTO result = tourService.save(tourDTO);
        return ResponseEntity.created(new URI("/api/tours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tour", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/tours")
    @Timed
    public ResponseEntity<TourDTO> updateTour(@Valid @RequestBody TourDTO tourDTO) throws URISyntaxException {
        log.debug("REST request to update Tour : {}", tourDTO);
        if (tourDTO.getId() == null) {
            return createTour(tourDTO);
        }
        TourDTO result = tourService.save(tourDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tour", tourDTO.getId().toString()))
            .body(result);
    }


    @GetMapping("/my/tours")
    public ResponseEntity<List<GetAllToursDTO>> getOnyMyTours(@RequestParam(value = "type", required = false) TourType type,
                                                            @RequestParam(value = "status", required = false) Status status,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        Page<GetAllToursDTO> page = tourService.findAllMyTours(pageable, type, status, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tours");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/tours")
    @Timed
    public ResponseEntity<List<GetAllToursDTO>> getAllTours(@RequestParam(value = "type", required = false) TourType type,
                                                            @RequestParam(value = "status", required = false) Status status,
                                                            @RequestParam(value = "userId", required = false) Long userId,
                                                            @RequestParam(value = "name", required = false) String name,
                                                                                           Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        Page<GetAllToursDTO> page = tourService.findAllToursByUSerId(pageable,type, status, userId, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tours");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @PostMapping("/tours/fixed")
    @Timed
    public ResponseEntity<TourFullDTO> createFixedTour(@Valid @RequestBody CreateFixedTourDTO tourDTO) throws URISyntaxException {
        log.debug("REST request to save Fixed  Tour : {}", tourDTO);
        if (tourDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tour", "idexists", "A new tour cannot already have an ID")).body(null);
        }
        TourFullDTO result = tourService.saveFixedTour(tourDTO, null, null, TourType.FIXED);
        return ResponseEntity.created(new URI("/api/tours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tour", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/tours/fixed")
    @Timed
    public ResponseEntity<TourFullDTO> updateFixedTour(@Valid @RequestBody CreateFixedTourDTO tourDTO) throws URISyntaxException {
        log.debug("REST request to save Fixed  Tour : {}", tourDTO);
        if (tourDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tour", "idexists", "A tour should have id to be updated")).body(null);
        }
        TourFullDTO result = tourService.saveFixedTour(tourDTO, null, null, TourType.FIXED);
        return ResponseEntity.created(new URI("/api/tours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tour", result.getId().toString()))
            .body(result);
    }

    @GetMapping("/tours/fixed")
    @Timed
    public ResponseEntity<List<GetAllToursDTO>> getClosestToCurrentLocationFixedTours(@RequestParam(value = "currentLat", required = false) Double curLat,
                                                                                 @RequestParam(value = "currentLng", required = false) Double curLng,
                                                                                 @RequestParam(value = "name", required = false) String name,
                                                                                 @RequestParam(value = "exceptTourIds", required = false) List<Long> exceptTourIds,
                                                                                      Pageable pageable
                                                                                 )
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        Page<Tour> page = tourService.findAllFixed(curLat, curLng, pageable, name, exceptTourIds);

        List<GetAllToursDTO> response = page.getContent().stream()
            .map(GetAllToursDTO::new)
            .collect(Collectors.toList());

        HttpHeaders httpHeaders = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tours/fixed");

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/tours/surprise")
    @Timed
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getClosestToCurrentLocationSurpriseMeTours(@RequestParam(value = "currentLat", required = true) Double curLat,
                                                                                                 @RequestParam(value = "currentLng", required = true) Double curLng,
//                                                                                                 @RequestParam(value = "radiusMeters", required = true) Double radius,
                                                                                                 @RequestParam(value = "exceptBubblIds", required = false) List<Long> exceptBubblIds,
                                                                                                     Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SURPRISE  Tours");

        Page<Bubbl> bubblPage = bubblService.findBubblsSurprise(curLat, curLng, pageable, exceptBubblIds);
        List<Bubbl> bubblsSurprise = bubblPage.getContent();

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(bubblPage, "/api/tours/surprise");

        AtomicInteger i = new AtomicInteger(1);
        List<FullTourBubblNumberedDTO> resp = bubblsSurprise.stream()
            .map(bubbl -> new FullTourBubblNumberedDTO(bubbl, i.getAndIncrement(), null))
            .collect(Collectors.toList());

        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }


    @GetMapping("/tours/diy")
    @Timed
    public ResponseEntity<List<TourFullDTO>> generateClosestCurrentLocationDIYTours(@RequestParam(value = "currentLat", required = true) Double curLat,
                                                                                    @RequestParam(value = "currentLng", required = true) Double curLng,
                                                                                    @RequestParam(value = "targetLat", required = true) Double tarLat,
                                                                                    @RequestParam(value = "targetLng", required = true) Double tarLng,
                                                                                    @RequestParam(value = "maxDelta", required = false) Double maxDelta
                                                                                    )
        throws URISyntaxException {
        log.debug("REST request to get a page of DIY Tours");

        List<TourFullDTO> diyTours = tourService.getDIYTours(curLat, curLng, tarLat, tarLng, maxDelta);

        return new ResponseEntity<>(diyTours,  HttpStatus.OK);
    }


    @GetMapping("/tours/{id}")
    @Timed
    public ResponseEntity<TourFullDTO> getTour(@PathVariable Long id,
                                               @RequestParam(value = "currentLat", required = false) Double curLat,
                                               @RequestParam(value = "currentLng", required = false) Double curLng) {
        log.debug("REST request to get Tour : {}", id);
        TourFullDTO tourDTO = tourService.findOne(id);

        if (curLat != null && curLng != null && tourDTO.getLat() != null && tourDTO.getLng() != null) {
            GlobalPosition userLocation = new GlobalPosition(curLat, curLng, 0.0);
            GlobalPosition tourLocation = new GlobalPosition(tourDTO.getLat(), tourDTO.getLng(), 0.0);
            Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, tourLocation).getEllipsoidalDistance();
            tourDTO.setDistanceToRouteStart(distance);
        }

        return Optional.ofNullable(tourDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/tours/{id}")
    @Timed
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        log.debug("REST request to delete Tour : {}", id);
        tourService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tour", id.toString())).build();
    }


    @PostMapping("/tours/{tourId}/downloads")
    public ResponseEntity<SuccessTransportObject> addTourToFavorites(@PathVariable("tourId") Long tourId) {
        if (tourDownloadService.addTourToFavorites(tourId)){
            return ResponseEntity.ok(new SuccessTransportObject());
        }else{
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has downloaded this tour w/ id " + tourId));
        }
    }

    @DeleteMapping("/tours/{tourId}/downloads")
    public ResponseEntity<SuccessTransportObject> removeTourFromFavorites(@PathVariable Long tourId) {
        if (tourDownloadService.removeTourFromFavorites(tourId)) {
            return ResponseEntity.ok(new SuccessTransportObject());
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has not yet downloaded or removed from downloads tour w/ id " + tourId));
        }
    }

    @PostMapping("/tours/{tourId}/complete")
    public ResponseEntity<SuccessTransportObject> markTourAsCompleted(@PathVariable("tourId") Long tourId) {
        if (tourDownloadService.markTourAsCompleted(tourId)){
            return ResponseEntity.ok(new SuccessTransportObject());
        }else{
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has completed this tour w/ id " + tourId));
        }
    }

    @DeleteMapping("/tours/{tourId}/complete")
    public ResponseEntity<SuccessTransportObject>  removeToursFromCompleted(@PathVariable Long tourId) {
        if (tourDownloadService.removeTourFromCompleted(tourId)) {
            return ResponseEntity.ok(new SuccessTransportObject());
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has not yet comleted  tour w/ id " + tourId));
        }
    }

    @GetMapping("/my/completed/tours")
    public ResponseEntity<List<GetAllToursDTO>> getUserCompletedTours(@RequestParam(value = "type", required = false) TourType type,
                                                                            @RequestParam(value = "status", required = false) Status status,
                                                                            @RequestParam(value = "name", required = false) String name,
                                                                            Pageable pageable)
        throws URISyntaxException {

        log.debug("REST request to get a page of Tours");

        Page<TourCompleted> page = tourService.findMyCompletedTours(pageable, type, status, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/users/completed/tours");

        List<GetAllToursDTO> resp = page.getContent().stream()
            .map(tourDownload -> new GetAllToursDTO(tourDownload.getTour()))
            .collect(Collectors.toList());

        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }



//    @GetMapping("/users/{userId}/downloads/tours")
//    public ResponseEntity<List<GetAllToursDTO>> getFavoriteToursByUserId(@RequestParam(value = "type", required = false) TourType type,
//                                                   @RequestParam(value = "status", required = false) Status status,
//                                                   @PathVariable(value = "userId") Long userId,
//                                                   Pageable pageable)
//        throws URISyntaxException {
//
//        log.debug("REST request to get a page of Tours");
//        Page<TourDownload> page = tourService.findFavoritesToursByUserID(pageable, type, status);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/users/" + userId + "/tours/downloads");
//
//        List<GetAllToursDTO> resp = page.getContent().stream()
//            .map(tourDownload -> new GetAllToursDTO(tourDownload.getTour()))
//            .collect(Collectors.toList());
//
//        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
//    }

    @GetMapping("/my/downloads/tours")
    public ResponseEntity<List<GetAllToursDTO>> getCurrentUserFavoriteTours(@RequestParam(value = "type", required = false) TourType type,
                                                                         @RequestParam(value = "status", required = false) Status status,
                                                                            @RequestParam(value = "name", required = false) String name,
                                                                         Pageable pageable)
        throws URISyntaxException {

        log.debug("REST request to get a page of Tours");
        Page<TourDownload> page = tourService.findMyFavoritesTours(pageable, type, status, name);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/users/downloads/tours");

        List<GetAllToursDTO> resp = page.getContent().stream()
            .map(tourDownload -> new GetAllToursDTO(tourDownload.getTour()))
            .collect(Collectors.toList());

        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }

    @PostMapping("/tours/recreate-route")
    public ResponseEntity<List<RoutePointDTO>> recalculateRoute(@RequestBody RecalculateRoutePointsDTO recalculateRoutePointsDTO) {


        return new ResponseEntity<List<RoutePointDTO>>(tourService.recalculateRoute(recalculateRoutePointsDTO), HttpStatus.OK);

    }


}
