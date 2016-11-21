package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.BubblAdminReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing BubblAdminReview.
 */
public interface BubblAdminReviewService {

    /**
     * Save a bubblAdminReview.
     *
     * @param bubblAdminReviewDTO the entity to save
     * @return the persisted entity
     */
    BubblAdminReviewDTO save(BubblAdminReviewDTO bubblAdminReviewDTO);

    /**
     *  Get all the bubblAdminReviews.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BubblAdminReviewDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bubblAdminReview.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BubblAdminReviewDTO findOne(Long id);

    /**
     *  Delete the "id" bubblAdminReview.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
