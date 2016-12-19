package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourBubblRoutePointService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourBubblRoutePointDTO;
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
 * REST controller for managing TourBubblRoutePoint.
 */
@RestController
@RequestMapping("/api")
public class TourBubblRoutePointResource {

    private final Logger log = LoggerFactory.getLogger(TourBubblRoutePointResource.class);

    @Inject
    private TourBubblRoutePointService tourBubblRoutePointService;

    /**
     * POST  /tour-bubbl-route-points : Create a new tourBubblRoutePoint.
     *
     * @param tourBubblRoutePointDTO the tourBubblRoutePointDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourBubblRoutePointDTO, or with status 400 (Bad Request) if the tourBubblRoutePoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-bubbl-route-points")
    public ResponseEntity<TourBubblRoutePointDTO> createTourBubblRoutePoint(@Valid @RequestBody TourBubblRoutePointDTO tourBubblRoutePointDTO) throws URISyntaxException {
        log.debug("REST request to save TourBubblRoutePoint : {}", tourBubblRoutePointDTO);
        if (tourBubblRoutePointDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourBubblRoutePoint", "idexists", "A new tourBubblRoutePoint cannot already have an ID")).body(null);
        }
        TourBubblRoutePointDTO result = tourBubblRoutePointService.save(tourBubblRoutePointDTO);
        return ResponseEntity.created(new URI("/api/tour-bubbl-route-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourBubblRoutePoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-bubbl-route-points : Updates an existing tourBubblRoutePoint.
     *
     * @param tourBubblRoutePointDTO the tourBubblRoutePointDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourBubblRoutePointDTO,
     * or with status 400 (Bad Request) if the tourBubblRoutePointDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourBubblRoutePointDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-bubbl-route-points")
    public ResponseEntity<TourBubblRoutePointDTO> updateTourBubblRoutePoint(@Valid @RequestBody TourBubblRoutePointDTO tourBubblRoutePointDTO) throws URISyntaxException {
        log.debug("REST request to update TourBubblRoutePoint : {}", tourBubblRoutePointDTO);
        if (tourBubblRoutePointDTO.getId() == null) {
            return createTourBubblRoutePoint(tourBubblRoutePointDTO);
        }
        TourBubblRoutePointDTO result = tourBubblRoutePointService.save(tourBubblRoutePointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourBubblRoutePoint", tourBubblRoutePointDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-bubbl-route-points : get all the tourBubblRoutePoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourBubblRoutePoints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-bubbl-route-points")
    public ResponseEntity<List<TourBubblRoutePointDTO>> getAllTourBubblRoutePoints(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourBubblRoutePoints");
        Page<TourBubblRoutePointDTO> page = tourBubblRoutePointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-bubbl-route-points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-bubbl-route-points/:id : get the "id" tourBubblRoutePoint.
     *
     * @param id the id of the tourBubblRoutePointDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourBubblRoutePointDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-bubbl-route-points/{id}")
    public ResponseEntity<TourBubblRoutePointDTO> getTourBubblRoutePoint(@PathVariable Long id) {
        log.debug("REST request to get TourBubblRoutePoint : {}", id);
        TourBubblRoutePointDTO tourBubblRoutePointDTO = tourBubblRoutePointService.findOne(id);
        return Optional.ofNullable(tourBubblRoutePointDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-bubbl-route-points/:id : delete the "id" tourBubblRoutePoint.
     *
     * @param id the id of the tourBubblRoutePointDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-bubbl-route-points/{id}")
    public ResponseEntity<Void> deleteTourBubblRoutePoint(@PathVariable Long id) {
        log.debug("REST request to delete TourBubblRoutePoint : {}", id);
        tourBubblRoutePointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourBubblRoutePoint", id.toString())).build();
    }

}
