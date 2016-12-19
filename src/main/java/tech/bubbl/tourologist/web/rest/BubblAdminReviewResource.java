package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.BubblAdminReviewService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblAdminReviewDTO;
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
 * REST controller for managing BubblAdminReview.
 */
@RestController
@RequestMapping("/api")
public class BubblAdminReviewResource {

    private final Logger log = LoggerFactory.getLogger(BubblAdminReviewResource.class);

    @Inject
    private BubblAdminReviewService bubblAdminReviewService;

    /**
     * POST  /bubbl-admin-reviews : Create a new bubblAdminReview.
     *
     * @param bubblAdminReviewDTO the bubblAdminReviewDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bubblAdminReviewDTO, or with status 400 (Bad Request) if the bubblAdminReview has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bubbl-admin-reviews")
    public ResponseEntity<BubblAdminReviewDTO> createBubblAdminReview(@Valid @RequestBody BubblAdminReviewDTO bubblAdminReviewDTO) throws URISyntaxException {
        log.debug("REST request to save BubblAdminReview : {}", bubblAdminReviewDTO);
        if (bubblAdminReviewDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubblAdminReview", "idexists", "A new bubblAdminReview cannot already have an ID")).body(null);
        }
        BubblAdminReviewDTO result = bubblAdminReviewService.save(bubblAdminReviewDTO);
        return ResponseEntity.created(new URI("/api/bubbl-admin-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubblAdminReview", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bubbl-admin-reviews : Updates an existing bubblAdminReview.
     *
     * @param bubblAdminReviewDTO the bubblAdminReviewDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bubblAdminReviewDTO,
     * or with status 400 (Bad Request) if the bubblAdminReviewDTO is not valid,
     * or with status 500 (Internal Server Error) if the bubblAdminReviewDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bubbl-admin-reviews")
    @Timed
    public ResponseEntity<BubblAdminReviewDTO> updateBubblAdminReview(@Valid @RequestBody BubblAdminReviewDTO bubblAdminReviewDTO) throws URISyntaxException {
        log.debug("REST request to update BubblAdminReview : {}", bubblAdminReviewDTO);
        if (bubblAdminReviewDTO.getId() == null) {
            return createBubblAdminReview(bubblAdminReviewDTO);
        }
        BubblAdminReviewDTO result = bubblAdminReviewService.save(bubblAdminReviewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubblAdminReview", bubblAdminReviewDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bubbl-admin-reviews : get all the bubblAdminReviews.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bubblAdminReviews in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bubbl-admin-reviews")
    public ResponseEntity<List<BubblAdminReviewDTO>> getAllBubblAdminReviews(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BubblAdminReviews");
        Page<BubblAdminReviewDTO> page = bubblAdminReviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbl-admin-reviews");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bubbl-admin-reviews/:id : get the "id" bubblAdminReview.
     *
     * @param id the id of the bubblAdminReviewDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bubblAdminReviewDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bubbl-admin-reviews/{id}")
    public ResponseEntity<BubblAdminReviewDTO> getBubblAdminReview(@PathVariable Long id) {
        log.debug("REST request to get BubblAdminReview : {}", id);
        BubblAdminReviewDTO bubblAdminReviewDTO = bubblAdminReviewService.findOne(id);
        return Optional.ofNullable(bubblAdminReviewDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bubbl-admin-reviews/:id : delete the "id" bubblAdminReview.
     *
     * @param id the id of the bubblAdminReviewDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bubbl-admin-reviews/{id}")
    public ResponseEntity<Void> deleteBubblAdminReview(@PathVariable Long id) {
        log.debug("REST request to delete BubblAdminReview : {}", id);
        bubblAdminReviewService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubblAdminReview", id.toString())).build();
    }

}
