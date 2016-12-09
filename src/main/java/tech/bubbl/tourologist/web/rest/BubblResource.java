package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblDTO;
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
import java.util.*;
import java.util.stream.Collectors;

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


    @PostMapping("/bubbls")
    @Timed
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
    @Timed
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
    @Timed
    public ResponseEntity<List<FullTourBubblNumberedDTO>> getAllBubblsByParams(@RequestParam(value = "status", required = false) Status status,
                                                       @RequestParam(value = "userId", required = false) Long userId,
                                                       Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bubbls");
        Page<FullTourBubblNumberedDTO> page = bubblService.findAll(pageable, status, userId);
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
    @Timed
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

}
