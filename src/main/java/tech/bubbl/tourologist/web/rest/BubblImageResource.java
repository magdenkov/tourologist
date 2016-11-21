package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.BubblImageService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing BubblImage.
 */
@RestController
@RequestMapping("/api")
public class BubblImageResource {

    private final Logger log = LoggerFactory.getLogger(BubblImageResource.class);
        
    @Inject
    private BubblImageService bubblImageService;

    /**
     * POST  /bubbl-images : Create a new bubblImage.
     *
     * @param bubblImageDTO the bubblImageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bubblImageDTO, or with status 400 (Bad Request) if the bubblImage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bubbl-images")
    @Timed
    public ResponseEntity<BubblImageDTO> createBubblImage(@RequestBody BubblImageDTO bubblImageDTO) throws URISyntaxException {
        log.debug("REST request to save BubblImage : {}", bubblImageDTO);
        if (bubblImageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubblImage", "idexists", "A new bubblImage cannot already have an ID")).body(null);
        }
        BubblImageDTO result = bubblImageService.save(bubblImageDTO);
        return ResponseEntity.created(new URI("/api/bubbl-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubblImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bubbl-images : Updates an existing bubblImage.
     *
     * @param bubblImageDTO the bubblImageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bubblImageDTO,
     * or with status 400 (Bad Request) if the bubblImageDTO is not valid,
     * or with status 500 (Internal Server Error) if the bubblImageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bubbl-images")
    @Timed
    public ResponseEntity<BubblImageDTO> updateBubblImage(@RequestBody BubblImageDTO bubblImageDTO) throws URISyntaxException {
        log.debug("REST request to update BubblImage : {}", bubblImageDTO);
        if (bubblImageDTO.getId() == null) {
            return createBubblImage(bubblImageDTO);
        }
        BubblImageDTO result = bubblImageService.save(bubblImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubblImage", bubblImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bubbl-images : get all the bubblImages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bubblImages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bubbl-images")
    @Timed
    public ResponseEntity<List<BubblImageDTO>> getAllBubblImages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BubblImages");
        Page<BubblImageDTO> page = bubblImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbl-images");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bubbl-images/:id : get the "id" bubblImage.
     *
     * @param id the id of the bubblImageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bubblImageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bubbl-images/{id}")
    @Timed
    public ResponseEntity<BubblImageDTO> getBubblImage(@PathVariable Long id) {
        log.debug("REST request to get BubblImage : {}", id);
        BubblImageDTO bubblImageDTO = bubblImageService.findOne(id);
        return Optional.ofNullable(bubblImageDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bubbl-images/:id : delete the "id" bubblImage.
     *
     * @param id the id of the bubblImageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bubbl-images/{id}")
    @Timed
    public ResponseEntity<Void> deleteBubblImage(@PathVariable Long id) {
        log.debug("REST request to delete BubblImage : {}", id);
        bubblImageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubblImage", id.toString())).build();
    }

}
