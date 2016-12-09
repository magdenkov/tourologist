package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.dto.bubbl.TourBubblNumberedDTO;
import tech.bubbl.tourologist.service.dto.tour.CreateFixedTourDTO;
import tech.bubbl.tourologist.service.dto.tour.GetAllToursDTO;
import tech.bubbl.tourologist.service.dto.tour.TourFullDTO;
import tech.bubbl.tourologist.service.util.SortBubbls;
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


    @GetMapping("/tours")
    @Timed
    public ResponseEntity<List<GetAllToursDTO>> getAllTours(@RequestParam(value = "type", required = false) TourType type,
                                                            @RequestParam(value = "status", required = false) Status status,
                                                            @RequestParam(value = "userId", required = false) Long userId,
                                                                                           Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        Page<GetAllToursDTO> page = tourService.findAll(pageable,type, status, userId);
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
                                                                                 @RequestParam(value = "radiusMeters", required = false) Double radius
                                                                                 )
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        List<GetAllToursDTO> resp = tourService.findAllFixed(curLat, curLng, radius);

//        List<GetAllToursDTO> resp = page.getContent();
        if (curLat != null && curLng != null) {
            GlobalPosition userLocation = new GlobalPosition(curLat, curLng, 0.0);
            resp.stream()
                .filter(getAllToursDTO -> getAllToursDTO.getLat() != null && getAllToursDTO.getLng() != null)
                .forEach(getAllToursDTO -> {
                    GlobalPosition tourLocation = new GlobalPosition(getAllToursDTO.getLat(), getAllToursDTO.getLng(), 0.0);
                    Double distance = GEODETIC_CALCULATOR.calculateGeodeticCurve(Ellipsoid.WGS84, userLocation, tourLocation).getEllipsoidalDistance();
                    getAllToursDTO.setDistanceToRouteStart(distance.intValue());
                });
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/tours/surprise")
    @Timed
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getClosestToCurrentLocationSurpriseMeTours(@RequestParam(value = "currentLat", required = true) Double curLat,
                                                                                                 @RequestParam(value = "currentLng", required = true) Double curLng,
                                                                                                 @RequestParam(value = "radiusMeters", required = true) Double radius)
        throws URISyntaxException {
        log.debug("REST request to get a page of SURPRISE  Tours");
        List<Bubbl> bubblsSurprise = bubblService.findBubblsSurprise(curLat, curLng, radius);

        AtomicInteger i = new AtomicInteger(0);
        List<FullTourBubblNumberedDTO> resp = bubblsSurprise.stream()
            .sorted(new SortBubbls(new Bubbl().lat(curLat).lng(curLng)))
            .map(bubbl -> new FullTourBubblNumberedDTO(bubbl, i.getAndIncrement()))
            .collect(Collectors.toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @GetMapping("/tours/diy")
    @Timed
    public ResponseEntity<List<TourFullDTO>> generateClosestCurrentLocationDIYTours(@RequestParam(value = "currentLat", required = true) Double curLat,
                                                                                    @RequestParam(value = "currentLng", required = true) Double curLng,
                                                                                    @RequestParam(value = "targetLat", required = true) Double tarLat,
                                                                                    @RequestParam(value = "targetLng", required = true) Double tarLng)
        throws URISyntaxException {
        log.debug("REST request to get a page of DIY Tours");

        List<TourFullDTO> diyTours = tourService.getDIYTours(curLat, curLng, tarLat , tarLng);

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
            tourDTO.setDistanceToRouteStart(distance.intValue());
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

}
