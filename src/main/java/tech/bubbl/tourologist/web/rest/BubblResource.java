package tech.bubbl.tourologist.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.service.BubblDownloadService;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.dto.ErrorDTO;
import tech.bubbl.tourologist.service.dto.SuccessTransportObject;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Bubbl.
 */
@RestController
@RequestMapping("/api")
public class BubblResource {

    private final Logger log = LoggerFactory.getLogger(BubblResource.class);

    @Inject
    private BubblService bubblService;

    @Inject
    private GeoApiContext geoApiContext;

    @Inject
    private BubblDownloadService bubblDownloadService;


    @PostMapping("/bubbls")
    public ResponseEntity<BubblDTO> createBubbl(@Valid @RequestBody BubblDTO bubblDTO) throws URISyntaxException {
        log.debug("REST request to save Bubbl : {}", bubblDTO);
        if (bubblDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubbl", "idexists", "A new bubbl cannot already have an ID")).body(null);
        }
        BubblDTO result = bubblService.save(bubblDTO);
        return ResponseEntity.created(new URI("/api/bubbls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubbl", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/bubbls")
    public ResponseEntity<BubblDTO> updateBubbl(@Valid @RequestBody BubblDTO bubblDTO) throws URISyntaxException {
        log.debug("REST request to update Bubbl : {}", bubblDTO);
        if (bubblDTO.getId() == null) {
            return createBubbl(bubblDTO);
        }
        BubblDTO result = bubblService.save(bubblDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubbl", bubblDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/bubbls")
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getAllBubblsByParams(@RequestParam(value = "status", required = false) Status status,
                                                       @RequestParam(value = "userId", required = false) Long userId,
                                                       Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bubbls");
        Page<FullTourBubblNumberedDTO> page = bubblService.findAll(pageable, status, userId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/bubbls/in_radius")
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getAllBubblsByParamsInRadius(
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "centerLat", required = false) Double centerLat,
            @RequestParam(value = "centerLng", required = false) Double centerLng,
            @RequestParam(value = "radius", required = false) Double radius, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Bubbls");
        Page<FullTourBubblNumberedDTO> page = bubblService.findAllInRadius(pageable, status, userId, centerLat,
                centerLng, radius);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/my/bubbls")
    @Timed
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getOnlyMy(@RequestParam(value = "status", required = false) Status status,
                                                                               @RequestParam(value = "userId", required = false) Long userId,
                                                                               Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bubbls");
        Page<FullTourBubblNumberedDTO> page = bubblService.findOnlyMyBubbls(pageable, status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/bubbls/{id}")
    @Timed
    public ResponseEntity<FullTourBubblNumberedDTO> getBubbl(@PathVariable Long id) {
        log.debug("REST request to get Bubbl : {}", id);
        FullTourBubblNumberedDTO bubblDTO = bubblService.findOne(id);
        return Optional.ofNullable(bubblDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/bubbls/{id}")
    @Timed
    public ResponseEntity<Void> deleteBubbl(@PathVariable Long id) {
        log.debug("REST request to delete Bubbl : {}", id);
        bubblService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubbl", id.toString())).build();
    }


    @GetMapping("/bubbls/decode_location")
    public ResponseEntity<List<LatLng>> decodeGeolocationByName(@RequestParam(required = true) String name) throws Exception {
        log.debug("REST request to decode geolocation : {}", name);

        GeocodingResult[] results = GeocodingApi.newRequest(geoApiContext).address(name).await();

        if (results == null || results.length == 0) {
            return  ResponseEntity.ok().body(new ArrayList<LatLng>());
        }

        List<LatLng> latLngs = Arrays.stream(results)
            .map(geocodingResult -> geocodingResult.geometry.location)
            .collect(Collectors.toList());

        return  ResponseEntity.ok().body(latLngs);
    }

    @GetMapping("/bubbls/reverse_geocode")
    @Timed
    public ResponseEntity<List<String>> reverseGeoCode(@RequestParam(required = true) Double lat,
                                                       @RequestParam(required = true) Double lng) throws Exception {

        log.debug("REST request to reverse geocoding : lat {} lng {}", lat, lng);

        List<String> address1 = bubblService.reverseGeocode(lat,lng);
        return  ResponseEntity.ok().body(address1);
    }



    @PostMapping("/bubbls/{bubblId}/downloads")
    public ResponseEntity<SuccessTransportObject> addTourToFavorites(@PathVariable("bubblId") Long bubblId) {
        if (bubblDownloadService.addBubblToFavorites(bubblId)){
            return ResponseEntity.ok(new SuccessTransportObject());
        }else{
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has downloaded this tour w/ id " + bubblId));
        }
    }

    @DeleteMapping("/bubbls/{bubblId}/downloads")
    public ResponseEntity<SuccessTransportObject> removeTourFromFavorites(@PathVariable("bubblId") Long bubblId) {
        if (bubblDownloadService.removeBubblFromFavorites(bubblId)) {
            return ResponseEntity.ok(new SuccessTransportObject());
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("User already has not yet downloaded or removed from downloads tour w/ id " + bubblId));
        }
    }

}
