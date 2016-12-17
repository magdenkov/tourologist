package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourRatingService;
import tech.bubbl.tourologist.service.dto.ErrorDTO;
import tech.bubbl.tourologist.service.dto.SuccessTransportObject;
import tech.bubbl.tourologist.service.dto.rating.CreateTourRatingCTO;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourRatingDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing TourRating.
 */
@RestController
@RequestMapping("/api")
public class TourRatingResource {

    private final Logger log = LoggerFactory.getLogger(TourRatingResource.class);

    @Inject
    private TourRatingService tourRatingService;



    @PostMapping("/tours/{tourId}/ratings")
    @Timed
    public ResponseEntity<SuccessTransportObject> rateTourById(@PathVariable(value = "tourId") Long tourId,
                                                      @Valid @RequestBody CreateTourRatingCTO tourRatingDTO) throws URISyntaxException {
        log.debug("REST request to save TourRating : {}", tourRatingDTO);

        if (tourRatingService.createRatingForTour(tourRatingDTO, tourId)) {
            return ResponseEntity.ok().body(new SuccessTransportObject());
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("user has laredy left feedback for that tour"));
        }

    }



    @PostMapping("/tour-ratings")
    @Timed
    public ResponseEntity<TourRatingDTO> createTourRating(@Valid @RequestBody TourRatingDTO tourRatingDTO) throws URISyntaxException {
        log.debug("REST request to save TourRating : {}", tourRatingDTO);
        if (tourRatingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourRating", "idexists", "A new tourRating cannot already have an ID")).body(null);
        }
        TourRatingDTO result = tourRatingService.save(tourRatingDTO);
        return ResponseEntity.created(new URI("/api/tour-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourRating", result.getId().toString()))
            .body(result);
    }


    @PutMapping("/tour-ratings")
    @Timed
    public ResponseEntity<TourRatingDTO> updateTourRating(@Valid @RequestBody TourRatingDTO tourRatingDTO) throws URISyntaxException {
        log.debug("REST request to update TourRating : {}", tourRatingDTO);
        if (tourRatingDTO.getId() == null) {
            return createTourRating(tourRatingDTO);
        }
        TourRatingDTO result = tourRatingService.save(tourRatingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourRating", tourRatingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-ratings : get all the tourRatings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourRatings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-ratings")
    @Timed
    public ResponseEntity<List<TourRatingDTO>> getAllTourRatings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourRatings");
        Page<TourRatingDTO> page = tourRatingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-ratings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/tour-ratings/{id}")
    @Timed
    public ResponseEntity<TourRatingDTO> getTourRating(@PathVariable Long id) {
        log.debug("REST request to get TourRating : {}", id);
        TourRatingDTO tourRatingDTO = tourRatingService.findOne(id);
        return Optional.ofNullable(tourRatingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/tour-ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteTourRating(@PathVariable Long id) {
        log.debug("REST request to delete TourRating : {}", id);
        tourRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourRating", id.toString())).build();
    }

}
