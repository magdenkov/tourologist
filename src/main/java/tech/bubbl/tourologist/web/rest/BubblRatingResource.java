package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.BubblRatingService;
import tech.bubbl.tourologist.service.dto.ErrorDTO;
import tech.bubbl.tourologist.service.dto.SuccessTransportObject;
import tech.bubbl.tourologist.service.dto.rating.CreateTourRatingCTO;
import tech.bubbl.tourologist.service.dto.tour.CreateTourBubblDTO;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblRatingDTO;
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
 * REST controller for managing BubblRating.
 */
@RestController
@RequestMapping("/api")
public class BubblRatingResource {

    private final Logger log = LoggerFactory.getLogger(BubblRatingResource.class);

    @Inject
    private BubblRatingService bubblRatingService;



    @PostMapping("/bubbls/{bubblId}/ratings")
    @Timed
    public ResponseEntity<SuccessTransportObject> rateBubblById(@PathVariable(value = "bubblId") Long tourId,
                                                               @Valid @RequestBody CreateTourBubblDTO tourBubblDTO) throws URISyntaxException {
        log.debug("REST request to save TourRating : {}", tourBubblDTO);

        if (bubblRatingService.createRatingForBubbl(tourBubblDTO, tourId)) {
            return ResponseEntity.ok().body(new SuccessTransportObject());
        } else {
            return ResponseEntity.badRequest().body(new ErrorDTO("user has laredy left feedback for that tour"));
        }

    }





    /**
     * POST  /bubbl-ratings : Create a new bubblRating.
     *
     * @param bubblRatingDTO the bubblRatingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bubblRatingDTO, or with status 400 (Bad Request) if the bubblRating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bubbl-ratings")
    @Timed
    public ResponseEntity<BubblRatingDTO> createBubblRating(@Valid @RequestBody BubblRatingDTO bubblRatingDTO) throws URISyntaxException {
        log.debug("REST request to save BubblRating : {}", bubblRatingDTO);
        if (bubblRatingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubblRating", "idexists", "A new bubblRating cannot already have an ID")).body(null);
        }
        BubblRatingDTO result = bubblRatingService.save(bubblRatingDTO);
        return ResponseEntity.created(new URI("/api/bubbl-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubblRating", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bubbl-ratings : Updates an existing bubblRating.
     *
     * @param bubblRatingDTO the bubblRatingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bubblRatingDTO,
     * or with status 400 (Bad Request) if the bubblRatingDTO is not valid,
     * or with status 500 (Internal Server Error) if the bubblRatingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bubbl-ratings")
    @Timed
    public ResponseEntity<BubblRatingDTO> updateBubblRating(@Valid @RequestBody BubblRatingDTO bubblRatingDTO) throws URISyntaxException {
        log.debug("REST request to update BubblRating : {}", bubblRatingDTO);
        if (bubblRatingDTO.getId() == null) {
            return createBubblRating(bubblRatingDTO);
        }
        BubblRatingDTO result = bubblRatingService.save(bubblRatingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubblRating", bubblRatingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bubbl-ratings : get all the bubblRatings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bubblRatings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bubbl-ratings")
    @Timed
    public ResponseEntity<List<BubblRatingDTO>> getAllBubblRatings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BubblRatings");
        Page<BubblRatingDTO> page = bubblRatingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbl-ratings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bubbl-ratings/:id : get the "id" bubblRating.
     *
     * @param id the id of the bubblRatingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bubblRatingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bubbl-ratings/{id}")
    @Timed
    public ResponseEntity<BubblRatingDTO> getBubblRating(@PathVariable Long id) {
        log.debug("REST request to get BubblRating : {}", id);
        BubblRatingDTO bubblRatingDTO = bubblRatingService.findOne(id);
        return Optional.ofNullable(bubblRatingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bubbl-ratings/:id : delete the "id" bubblRating.
     *
     * @param id the id of the bubblRatingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bubbl-ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBubblRating(@PathVariable Long id) {
        log.debug("REST request to delete BubblRating : {}", id);
        bubblRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubblRating", id.toString())).build();
    }

}
