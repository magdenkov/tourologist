package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.BubblDTO;
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
 * REST controller for managing Bubbl.
 */
@RestController
@RequestMapping("/api")
public class BubblResource {

    private final Logger log = LoggerFactory.getLogger(BubblResource.class);
        
    @Inject
    private BubblService bubblService;

    /**
     * POST  /bubbls : Create a new bubbl.
     *
     * @param bubblDTO the bubblDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bubblDTO, or with status 400 (Bad Request) if the bubbl has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bubbls")
    @Timed
    public ResponseEntity<BubblDTO> createBubbl(@Valid @RequestBody BubblDTO bubblDTO) throws URISyntaxException {
        log.debug("REST request to save Bubbl : {}", bubblDTO);
        if (bubblDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bubbl", "idexists", "A new bubbl cannot already have an ID")).body(null);
        }
        BubblDTO result = bubblService.save(bubblDTO);
        return ResponseEntity.created(new URI("/api/bubbls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bubbl", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bubbls : Updates an existing bubbl.
     *
     * @param bubblDTO the bubblDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bubblDTO,
     * or with status 400 (Bad Request) if the bubblDTO is not valid,
     * or with status 500 (Internal Server Error) if the bubblDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bubbls")
    @Timed
    public ResponseEntity<BubblDTO> updateBubbl(@Valid @RequestBody BubblDTO bubblDTO) throws URISyntaxException {
        log.debug("REST request to update Bubbl : {}", bubblDTO);
        if (bubblDTO.getId() == null) {
            return createBubbl(bubblDTO);
        }
        BubblDTO result = bubblService.save(bubblDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bubbl", bubblDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bubbls : get all the bubbls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bubbls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bubbls")
    @Timed
    public ResponseEntity<List<BubblDTO>> getAllBubbls(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bubbls");
        Page<BubblDTO> page = bubblService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bubbls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bubbls/:id : get the "id" bubbl.
     *
     * @param id the id of the bubblDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bubblDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bubbls/{id}")
    @Timed
    public ResponseEntity<BubblDTO> getBubbl(@PathVariable Long id) {
        log.debug("REST request to get Bubbl : {}", id);
        BubblDTO bubblDTO = bubblService.findOne(id);
        return Optional.ofNullable(bubblDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bubbls/:id : delete the "id" bubbl.
     *
     * @param id the id of the bubblDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bubbls/{id}")
    @Timed
    public ResponseEntity<Void> deleteBubbl(@PathVariable Long id) {
        log.debug("REST request to delete Bubbl : {}", id);
        bubblService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bubbl", id.toString())).build();
    }

}
