package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourAdminReviewService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourAdminReviewDTO;
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
 * REST controller for managing TourAdminReview.
 */
@RestController
@RequestMapping("/api")
public class TourAdminReviewResource {

    private final Logger log = LoggerFactory.getLogger(TourAdminReviewResource.class);
        
    @Inject
    private TourAdminReviewService tourAdminReviewService;

    /**
     * POST  /tour-admin-reviews : Create a new tourAdminReview.
     *
     * @param tourAdminReviewDTO the tourAdminReviewDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourAdminReviewDTO, or with status 400 (Bad Request) if the tourAdminReview has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-admin-reviews")
    @Timed
    public ResponseEntity<TourAdminReviewDTO> createTourAdminReview(@Valid @RequestBody TourAdminReviewDTO tourAdminReviewDTO) throws URISyntaxException {
        log.debug("REST request to save TourAdminReview : {}", tourAdminReviewDTO);
        if (tourAdminReviewDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourAdminReview", "idexists", "A new tourAdminReview cannot already have an ID")).body(null);
        }
        TourAdminReviewDTO result = tourAdminReviewService.save(tourAdminReviewDTO);
        return ResponseEntity.created(new URI("/api/tour-admin-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourAdminReview", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-admin-reviews : Updates an existing tourAdminReview.
     *
     * @param tourAdminReviewDTO the tourAdminReviewDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourAdminReviewDTO,
     * or with status 400 (Bad Request) if the tourAdminReviewDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourAdminReviewDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-admin-reviews")
    @Timed
    public ResponseEntity<TourAdminReviewDTO> updateTourAdminReview(@Valid @RequestBody TourAdminReviewDTO tourAdminReviewDTO) throws URISyntaxException {
        log.debug("REST request to update TourAdminReview : {}", tourAdminReviewDTO);
        if (tourAdminReviewDTO.getId() == null) {
            return createTourAdminReview(tourAdminReviewDTO);
        }
        TourAdminReviewDTO result = tourAdminReviewService.save(tourAdminReviewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourAdminReview", tourAdminReviewDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-admin-reviews : get all the tourAdminReviews.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourAdminReviews in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-admin-reviews")
    @Timed
    public ResponseEntity<List<TourAdminReviewDTO>> getAllTourAdminReviews(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourAdminReviews");
        Page<TourAdminReviewDTO> page = tourAdminReviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-admin-reviews");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-admin-reviews/:id : get the "id" tourAdminReview.
     *
     * @param id the id of the tourAdminReviewDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourAdminReviewDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-admin-reviews/{id}")
    @Timed
    public ResponseEntity<TourAdminReviewDTO> getTourAdminReview(@PathVariable Long id) {
        log.debug("REST request to get TourAdminReview : {}", id);
        TourAdminReviewDTO tourAdminReviewDTO = tourAdminReviewService.findOne(id);
        return Optional.ofNullable(tourAdminReviewDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-admin-reviews/:id : delete the "id" tourAdminReview.
     *
     * @param id the id of the tourAdminReviewDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-admin-reviews/{id}")
    @Timed
    public ResponseEntity<Void> deleteTourAdminReview(@PathVariable Long id) {
        log.debug("REST request to delete TourAdminReview : {}", id);
        tourAdminReviewService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourAdminReview", id.toString())).build();
    }

}
