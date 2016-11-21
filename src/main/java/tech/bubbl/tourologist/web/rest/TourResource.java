package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    /**
     * POST  /tours : Create a new tour.
     *
     * @param tourDTO the tourDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourDTO, or with status 400 (Bad Request) if the tour has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
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

    /**
     * PUT  /tours : Updates an existing tour.
     *
     * @param tourDTO the tourDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourDTO,
     * or with status 400 (Bad Request) if the tourDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
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

    /**
     * GET  /tours : get all the tours.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tours in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tours")
    @Timed
    public ResponseEntity<List<TourDTO>> getAllTours(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tours");
        Page<TourDTO> page = tourService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tours");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tours/:id : get the "id" tour.
     *
     * @param id the id of the tourDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tours/{id}")
    @Timed
    public ResponseEntity<TourDTO> getTour(@PathVariable Long id) {
        log.debug("REST request to get Tour : {}", id);
        TourDTO tourDTO = tourService.findOne(id);
        return Optional.ofNullable(tourDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tours/:id : delete the "id" tour.
     *
     * @param id the id of the tourDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tours/{id}")
    @Timed
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        log.debug("REST request to delete Tour : {}", id);
        tourService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tour", id.toString())).build();
    }

}
