package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.BubblImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing BubblImage.
 */
public interface BubblImageService {

    /**
     * Save a bubblImage.
     *
     * @param bubblImageDTO the entity to save
     * @return the persisted entity
     */
    BubblImageDTO save(BubblImageDTO bubblImageDTO);

    /**
     *  Get all the bubblImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BubblImageDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bubblImage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BubblImageDTO findOne(Long id);

    /**
     *  Delete the "id" bubblImage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
