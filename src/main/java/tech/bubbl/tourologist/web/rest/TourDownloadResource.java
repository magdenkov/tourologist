package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.TourDownloadService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.TourDownloadDTO;
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
 * REST controller for managing TourDownload.
 */
@RestController
@RequestMapping("/api")
public class TourDownloadResource {

    private final Logger log = LoggerFactory.getLogger(TourDownloadResource.class);

    @Inject
    private TourDownloadService tourDownloadService;

    /**
     * POST  /tour-downloads : Create a new tourDownload.
     *
     * @param tourDownloadDTO the tourDownloadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourDownloadDTO, or with status 400 (Bad Request) if the tourDownload has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tour-downloads")
    public ResponseEntity<TourDownloadDTO> createTourDownload(@Valid @RequestBody TourDownloadDTO tourDownloadDTO) throws URISyntaxException {
        log.debug("REST request to save TourDownload : {}", tourDownloadDTO);
        if (tourDownloadDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourDownload", "idexists", "A new tourDownload cannot already have an ID")).body(null);
        }
        TourDownloadDTO result = tourDownloadService.save(tourDownloadDTO);
        return ResponseEntity.created(new URI("/api/tour-downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourDownload", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tour-downloads : Updates an existing tourDownload.
     *
     * @param tourDownloadDTO the tourDownloadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourDownloadDTO,
     * or with status 400 (Bad Request) if the tourDownloadDTO is not valid,
     * or with status 500 (Internal Server Error) if the tourDownloadDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tour-downloads")
    public ResponseEntity<TourDownloadDTO> updateTourDownload(@Valid @RequestBody TourDownloadDTO tourDownloadDTO) throws URISyntaxException {
        log.debug("REST request to update TourDownload : {}", tourDownloadDTO);
        if (tourDownloadDTO.getId() == null) {
            return createTourDownload(tourDownloadDTO);
        }
        TourDownloadDTO result = tourDownloadService.save(tourDownloadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourDownload", tourDownloadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tour-downloads : get all the tourDownloads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tourDownloads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tour-downloads")
    public ResponseEntity<List<TourDownloadDTO>> getAllTourDownloads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TourDownloads");
        Page<TourDownloadDTO> page = tourDownloadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tour-downloads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tour-downloads/:id : get the "id" tourDownload.
     *
     * @param id the id of the tourDownloadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourDownloadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tour-downloads/{id}")
    public ResponseEntity<TourDownloadDTO> getTourDownload(@PathVariable Long id) {
        log.debug("REST request to get TourDownload : {}", id);
        TourDownloadDTO tourDownloadDTO = tourDownloadService.findOne(id);
        return Optional.ofNullable(tourDownloadDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tour-downloads/:id : delete the "id" tourDownload.
     *
     * @param id the id of the tourDownloadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tour-downloads/{id}")
    public ResponseEntity<Void> deleteTourDownload(@PathVariable Long id) {
        log.debug("REST request to delete TourDownload : {}", id);
        tourDownloadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourDownload", id.toString())).build();
    }

}
