package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourImage.
 */
public interface TourImageService {

    /**
     * Save a tourImage.
     *
     * @param tourImageDTO the entity to save
     * @return the persisted entity
     */
    TourImageDTO save(TourImageDTO tourImageDTO);

    /**
     *  Get all the tourImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourImageDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourImage.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourImageDTO findOne(Long id);

    /**
     *  Delete the "id" tourImage.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
