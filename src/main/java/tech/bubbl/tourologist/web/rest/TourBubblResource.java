package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourBubblService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourBubblDTO;
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
 * REST controller for managing TourBubbl.
 */
@RestController
@RequestMapping("/api")
public class TourBubblResource {

    private final Logger log = LoggerFactory.getLogger(TourBubblResource.class);
        
    @Inject
    private TourBubblService tourBubblService;

    /**
     * POST  /tour-bubbls : Create a new tourBubbl.
     *
     * @param tourBubblDTO the tourBubblDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourBubblDTO, or with status 400 (Bad Request) if the tourBubbl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-bubbls")
    @Timed
    public ResponseEntity<TourBubblDTO> createTourBubbl(@Valid @RequestBody TourBubblDTO tourBubblDTO) throws URISyntaxException {
        log.debug("REST request to save TourBubbl : {}", tourBubblDTO);
        if (tourBubblDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourBubbl", "idexists", "A new tourBubbl cannot already have an ID")).body(null);
        }
        TourBubblDTO result = tourBubblService.save(tourBubblDTO);
        return ResponseEntity.created(new URI("/api/tour-bubbls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourBubbl", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-bubbls : Updates an existing tourBubbl.
     *
     * @param tourBubblDTO the tourBubblDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourBubblDTO,
     * or with status 400 (Bad Request) if the tourBubblDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourBubblDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-bubbls")
    @Timed
    public ResponseEntity<TourBubblDTO> updateTourBubbl(@Valid @RequestBody TourBubblDTO tourBubblDTO) throws URISyntaxException {
        log.debug("REST request to update TourBubbl : {}", tourBubblDTO);
        if (tourBubblDTO.getId() == null) {
            return createTourBubbl(tourBubblDTO);
        }
        TourBubblDTO result = tourBubblService.save(tourBubblDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourBubbl", tourBubblDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-bubbls : get all the tourBubbls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourBubbls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-bubbls")
    @Timed
    public ResponseEntity<List<TourBubblDTO>> getAllTourBubbls(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourBubbls");
        Page<TourBubblDTO> page = tourBubblService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-bubbls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-bubbls/:id : get the "id" tourBubbl.
     *
     * @param id the id of the tourBubblDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourBubblDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-bubbls/{id}")
    @Timed
    public ResponseEntity<TourBubblDTO> getTourBubbl(@PathVariable Long id) {
        log.debug("REST request to get TourBubbl : {}", id);
        TourBubblDTO tourBubblDTO = tourBubblService.findOne(id);
        return Optional.ofNullable(tourBubblDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-bubbls/:id : delete the "id" tourBubbl.
     *
     * @param id the id of the tourBubblDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-bubbls/{id}")
    @Timed
    public ResponseEntity<Void> deleteTourBubbl(@PathVariable Long id) {
        log.debug("REST request to delete TourBubbl : {}", id);
        tourBubblService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourBubbl", id.toString())).build();
    }

}
