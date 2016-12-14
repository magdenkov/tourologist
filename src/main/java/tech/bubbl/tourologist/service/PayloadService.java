package tech.bubbl.tourologist.service;

import org.springframework.web.multipart.MultipartFile;
import tech.bubbl.tourologist.domain.Payload;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * Service Interface for managing Payload.
 */
public interface PayloadService {

    /**
     * Save a payload.
     *
     * @param payloadDTO the entity to save
     * @param file
     * @return the persisted entity
     */
    PayloadDTO save(PayloadDTO payloadDTO, MultipartFile file) throws InterruptedException, IOException;

    /**
     *  Get all the payloads.
     *
     *
     * @param userId
     * @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PayloadDTO> findAll(Long userId, Pageable pageable);

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

    void deleteWithoutTransaction(Payload payload);

    PayloadDTO save(PayloadDTO payloadDTO);

    Page<PayloadDTO> findOnlyMyPayloads(Pageable pageable);
}
