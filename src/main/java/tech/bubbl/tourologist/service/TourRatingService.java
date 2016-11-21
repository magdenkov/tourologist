package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourRating.
 */
public interface TourRatingService {

    /**
     * Save a tourRating.
     *
     * @param tourRatingDTO the entity to save
     * @return the persisted entity
     */
    TourRatingDTO save(TourRatingDTO tourRatingDTO);

    /**
     *  Get all the tourRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourRatingDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourRating.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourRatingDTO findOne(Long id);

    /**
     *  Delete the "id" tourRating.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
