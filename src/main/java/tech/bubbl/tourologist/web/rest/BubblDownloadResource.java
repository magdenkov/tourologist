package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.BubblDownloadService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblDownloadDTO;
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
 * REST controller for managing BubblDownload.
 */
@RestController
@RequestMapping("/api")
public class BubblDownloadResource {

    private final Logger log = LoggerFactory.getLogger(BubblDownloadResource.class);
        
    @Inject
    private BubblDownloadService bubblDownloadService;

    /**
     * POST  /bubbl-downloads : Create a new bubblDownload.
     *
     * @param bubblDownloadDTO the bubblDownloadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bubblDownloadDTO, or with status 400 (Bad Request) if the bubblDownload has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bubbl-downloads")
    @Timed
    public ResponseEntity<BubblDownloadDTO> createBubblDownload(@Valid @RequestBody BubblDownloadDTO bubblDownloadDTO) throws URISyntaxException {
        log.debug("REST request to save BubblDownload : {}", bubblDownloadDTO);
        if (bubblDownloadDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubblDownload", "idexists", "A new bubblDownload cannot already have an ID")).body(null);
        }
        BubblDownloadDTO result = bubblDownloadService.save(bubblDownloadDTO);
        return ResponseEntity.created(new URI("/api/bubbl-downloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubblDownload", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bubbl-downloads : Updates an existing bubblDownload.
     *
     * @param bubblDownloadDTO the bubblDownloadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bubblDownloadDTO,
     * or with status 400 (Bad Request) if the bubblDownloadDTO is not valid,
     * or with status 500 (Internal Server Error) if the bubblDownloadDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bubbl-downloads")
    @Timed
    public ResponseEntity<BubblDownloadDTO> updateBubblDownload(@Valid @RequestBody BubblDownloadDTO bubblDownloadDTO) throws URISyntaxException {
        log.debug("REST request to update BubblDownload : {}", bubblDownloadDTO);
        if (bubblDownloadDTO.getId() == null) {
            return createBubblDownload(bubblDownloadDTO);
        }
        BubblDownloadDTO result = bubblDownloadService.save(bubblDownloadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubblDownload", bubblDownloadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bubbl-downloads : get all the bubblDownloads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bubblDownloads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bubbl-downloads")
    @Timed
    public ResponseEntity<List<BubblDownloadDTO>> getAllBubblDownloads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BubblDownloads");
        Page<BubblDownloadDTO> page = bubblDownloadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbl-downloads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bubbl-downloads/:id : get the "id" bubblDownload.
     *
     * @param id the id of the bubblDownloadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bubblDownloadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bubbl-downloads/{id}")
    @Timed
    public ResponseEntity<BubblDownloadDTO> getBubblDownload(@PathVariable Long id) {
        log.debug("REST request to get BubblDownload : {}", id);
        BubblDownloadDTO bubblDownloadDTO = bubblDownloadService.findOne(id);
        return Optional.ofNullable(bubblDownloadDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bubbl-downloads/:id : delete the "id" bubblDownload.
     *
     * @param id the id of the bubblDownloadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bubbl-downloads/{id}")
    @Timed
    public ResponseEntity<Void> deleteBubblDownload(@PathVariable Long id) {
        log.debug("REST request to delete BubblDownload : {}", id);
        bubblDownloadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubblDownload", id.toString())).build();
    }

}
