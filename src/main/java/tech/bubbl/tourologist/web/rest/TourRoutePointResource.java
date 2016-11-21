package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourRoutePointService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourRoutePointDTO;
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
 * REST controller for managing TourRoutePoint.
 */
@RestController
@RequestMapping("/api")
public class TourRoutePointResource {

    private final Logger log = LoggerFactory.getLogger(TourRoutePointResource.class);
        
    @Inject
    private TourRoutePointService tourRoutePointService;

    /**
     * POST  /tour-route-points : Create a new tourRoutePoint.
     *
     * @param tourRoutePointDTO the tourRoutePointDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourRoutePointDTO, or with status 400 (Bad Request) if the tourRoutePoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-route-points")
    @Timed
    public ResponseEntity<TourRoutePointDTO> createTourRoutePoint(@Valid @RequestBody TourRoutePointDTO tourRoutePointDTO) throws URISyntaxException {
        log.debug("REST request to save TourRoutePoint : {}", tourRoutePointDTO);
        if (tourRoutePointDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourRoutePoint", "idexists", "A new tourRoutePoint cannot already have an ID")).body(null);
        }
        TourRoutePointDTO result = tourRoutePointService.save(tourRoutePointDTO);
        return ResponseEntity.created(new URI("/api/tour-route-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourRoutePoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-route-points : Updates an existing tourRoutePoint.
     *
     * @param tourRoutePointDTO the tourRoutePointDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourRoutePointDTO,
     * or with status 400 (Bad Request) if the tourRoutePointDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourRoutePointDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-route-points")
    @Timed
    public ResponseEntity<TourRoutePointDTO> updateTourRoutePoint(@Valid @RequestBody TourRoutePointDTO tourRoutePointDTO) throws URISyntaxException {
        log.debug("REST request to update TourRoutePoint : {}", tourRoutePointDTO);
        if (tourRoutePointDTO.getId() == null) {
            return createTourRoutePoint(tourRoutePointDTO);
        }
        TourRoutePointDTO result = tourRoutePointService.save(tourRoutePointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourRoutePoint", tourRoutePointDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-route-points : get all the tourRoutePoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourRoutePoints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-route-points")
    @Timed
    public ResponseEntity<List<TourRoutePointDTO>> getAllTourRoutePoints(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourRoutePoints");
        Page<TourRoutePointDTO> page = tourRoutePointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-route-points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-route-points/:id : get the "id" tourRoutePoint.
     *
     * @param id the id of the tourRoutePointDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourRoutePointDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-route-points/{id}")
    @Timed
    public ResponseEntity<TourRoutePointDTO> getTourRoutePoint(@PathVariable Long id) {
        log.debug("REST request to get TourRoutePoint : {}", id);
        TourRoutePointDTO tourRoutePointDTO = tourRoutePointService.findOne(id);
        return Optional.ofNullable(tourRoutePointDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-route-points/:id : delete the "id" tourRoutePoint.
     *
     * @param id the id of the tourRoutePointDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-route-points/{id}")
    @Timed
    public ResponseEntity<Void> deleteTourRoutePoint(@PathVariable Long id) {
        log.debug("REST request to delete TourRoutePoint : {}", id);
        tourRoutePointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourRoutePoint", id.toString())).build();
    }

}
