package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourAdminReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourAdminReview.
 */
public interface TourAdminReviewService {

    /**
     * Save a tourAdminReview.
     *
     * @param tourAdminReviewDTO the entity to save
     * @return the persisted entity
     */
    TourAdminReviewDTO save(TourAdminReviewDTO tourAdminReviewDTO);

    /**
     *  Get all the tourAdminReviews.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourAdminReviewDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourAdminReview.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourAdminReviewDTO findOne(Long id);

    /**
     *  Delete the "id" tourAdminReview.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
