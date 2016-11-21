package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourBubblDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourBubbl.
 */
public interface TourBubblService {

    /**
     * Save a tourBubbl.
     *
     * @param tourBubblDTO the entity to save
     * @return the persisted entity
     */
    TourBubblDTO save(TourBubblDTO tourBubblDTO);

    /**
     *  Get all the tourBubbls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourBubblDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourBubbl.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourBubblDTO findOne(Long id);

    /**
     *  Delete the "id" tourBubbl.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
