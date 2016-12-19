package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourImageService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourImageDTO;
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
 * REST controller for managing TourImage.
 */
@RestController
@RequestMapping("/api")
public class TourImageResource {

    private final Logger log = LoggerFactory.getLogger(TourImageResource.class);

    @Inject
    private TourImageService tourImageService;

    /**
     * POST  /tour-images : Create a new tourImage.
     *
     * @param tourImageDTO the tourImageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourImageDTO, or with status 400 (Bad Request) if the tourImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-images")
    public ResponseEntity<TourImageDTO> createTourImage(@Valid @RequestBody TourImageDTO tourImageDTO) throws URISyntaxException {
        log.debug("REST request to save TourImage : {}", tourImageDTO);
        if (tourImageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourImage", "idexists", "A new tourImage cannot already have an ID")).body(null);
        }
        TourImageDTO result = tourImageService.save(tourImageDTO);
        return ResponseEntity.created(new URI("/api/tour-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-images : Updates an existing tourImage.
     *
     * @param tourImageDTO the tourImageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourImageDTO,
     * or with status 400 (Bad Request) if the tourImageDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourImageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-images")
    public ResponseEntity<TourImageDTO> updateTourImage(@Valid @RequestBody TourImageDTO tourImageDTO) throws URISyntaxException {
        log.debug("REST request to update TourImage : {}", tourImageDTO);
        if (tourImageDTO.getId() == null) {
            return createTourImage(tourImageDTO);
        }
        TourImageDTO result = tourImageService.save(tourImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourImage", tourImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-images : get all the tourImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourImages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-images")
    @Timed
    public ResponseEntity<List<TourImageDTO>> getAllTourImages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourImages");
        Page<TourImageDTO> page = tourImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-images/:id : get the "id" tourImage.
     *
     * @param id the id of the tourImageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourImageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-images/{id}")
    public ResponseEntity<TourImageDTO> getTourImage(@PathVariable Long id) {
        log.debug("REST request to get TourImage : {}", id);
        TourImageDTO tourImageDTO = tourImageService.findOne(id);
        return Optional.ofNullable(tourImageDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-images/:id : delete the "id" tourImage.
     *
     * @param id the id of the tourImageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-images/{id}")
    public ResponseEntity<Void> deleteTourImage(@PathVariable Long id) {
        log.debug("REST request to delete TourImage : {}", id);
        tourImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourImage", id.toString())).build();
    }

}
