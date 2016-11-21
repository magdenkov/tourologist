package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.PayloadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Payload.
 */
public interface PayloadService {

    /**
     * Save a payload.
     *
     * @param payloadDTO the entity to save
     * @return the persisted entity
     */
    PayloadDTO save(PayloadDTO payloadDTO);

    /**
     *  Get all the payloads.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PayloadDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" payload.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PayloadDTO findOne(Long id);

    /**
     *  Delete the "id" payload.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
