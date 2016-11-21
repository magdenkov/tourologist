package tech.bubbl.tourologist.web.rest;

import com.codahale.metrics.annotation.Timed;
import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.web.rest.util.HeaderUtil;
import tech.bubbl.tourologist.web.rest.util.PaginationUtil;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
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
 * REST controller for managing Payload.
 */
@RestController
@RequestMapping("/api")
public class PayloadResource {

    private final Logger log = LoggerFactory.getLogger(PayloadResource.class);
        
    @Inject
    private PayloadService payloadService;

    /**
     * POST  /payloads : Create a new payload.
     *
     * @param payloadDTO the payloadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payloadDTO, or with status 400 (Bad Request) if the payload has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payloads")
    @Timed
    public ResponseEntity<PayloadDTO> createPayload(@Valid @RequestBody PayloadDTO payloadDTO) throws URISyntaxException {
        log.debug("REST request to save Payload : {}", payloadDTO);
        if (payloadDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("payload", "idexists", "A new payload cannot already have an ID")).body(null);
        }
        PayloadDTO result = payloadService.save(payloadDTO);
        return ResponseEntity.created(new URI("/api/payloads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("payload", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payloads : Updates an existing payload.
     *
     * @param payloadDTO the payloadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payloadDTO,
     * or with status 400 (Bad Request) if the payloadDTO is not valid,
     * or with status 500 (Internal Server Error) if the payloadDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payloads")
    @Timed
    public ResponseEntity<PayloadDTO> updatePayload(@Valid @RequestBody PayloadDTO payloadDTO) throws URISyntaxException {
        log.debug("REST request to update Payload : {}", payloadDTO);
        if (payloadDTO.getId() == null) {
            return createPayload(payloadDTO);
        }
        PayloadDTO result = payloadService.save(payloadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("payload", payloadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payloads : get all the payloads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payloads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/payloads")
    @Timed
    public ResponseEntity<List<PayloadDTO>> getAllPayloads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Payloads");
        Page<PayloadDTO> page = payloadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payloads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payloads/:id : get the "id" payload.
     *
     * @param id the id of the payloadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payloadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/payloads/{id}")
    @Timed
    public ResponseEntity<PayloadDTO> getPayload(@PathVariable Long id) {
        log.debug("REST request to get Payload : {}", id);
        PayloadDTO payloadDTO = payloadService.findOne(id);
        return Optional.ofNullable(payloadDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /payloads/:id : delete the "id" payload.
     *
     * @param id the id of the payloadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payloads/{id}")
    @Timed
    public ResponseEntity<Void> deletePayload(@PathVariable Long id) {
        log.debug("REST request to delete Payload : {}", id);
        payloadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("payload", id.toString())).build();
    }

}
