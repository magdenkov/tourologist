package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.BubblRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bubbl.tourologist.service.dto.tour.CreateTourBubblDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing BubblRating.
 */
public interface BubblRatingService {

    /**
     * Save a bubblRating.
     *
     * @param bubblRatingDTO the entity to save
     * @return the persisted entity
     */
    BubblRatingDTO save(BubblRatingDTO bubblRatingDTO);

    /**
     *  Get all the bubblRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BubblRatingDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bubblRating.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BubblRatingDTO findOne(Long id);

    /**
     *  Delete the "id" bubblRating.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    boolean createRatingForBubbl(CreateTourBubblDTO tourBubblDTO, Long tourId);
}
