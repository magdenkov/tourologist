package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourAdminReviewService;
import tech.bubbl.tourologist.domain.TourAdminReview;
import tech.bubbl.tourologist.repository.TourAdminReviewRepository;
import tech.bubbl.tourologist.service.dto.TourAdminReviewDTO;
import tech.bubbl.tourologist.service.mapper.TourAdminReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TourAdminReview.
 */
@Service
@Transactional
public class TourAdminReviewServiceImpl implements TourAdminReviewService{

    private final Logger log = LoggerFactory.getLogger(TourAdminReviewServiceImpl.class);
    
    @Inject
    private TourAdminReviewRepository tourAdminReviewRepository;

    @Inject
    private TourAdminReviewMapper tourAdminReviewMapper;

    /**
     * Save a tourAdminReview.
     *
     * @param tourAdminReviewDTO the entity to save
     * @return the persisted entity
     */
    public TourAdminReviewDTO save(TourAdminReviewDTO tourAdminReviewDTO) {
        log.debug("Request to save TourAdminReview : {}", tourAdminReviewDTO);
        TourAdminReview tourAdminReview = tourAdminReviewMapper.tourAdminReviewDTOToTourAdminReview(tourAdminReviewDTO);
        tourAdminReview = tourAdminReviewRepository.save(tourAdminReview);
        TourAdminReviewDTO result = tourAdminReviewMapper.tourAdminReviewToTourAdminReviewDTO(tourAdminReview);
        return result;
    }

    /**
     *  Get all the tourAdminReviews.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourAdminReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourAdminReviews");
        Page<TourAdminReview> result = tourAdminReviewRepository.findAll(pageable);
        return result.map(tourAdminReview -> tourAdminReviewMapper.tourAdminReviewToTourAdminReviewDTO(tourAdminReview));
    }

    /**
     *  Get one tourAdminReview by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourAdminReviewDTO findOne(Long id) {
        log.debug("Request to get TourAdminReview : {}", id);
        TourAdminReview tourAdminReview = tourAdminReviewRepository.findOne(id);
        TourAdminReviewDTO tourAdminReviewDTO = tourAdminReviewMapper.tourAdminReviewToTourAdminReviewDTO(tourAdminReview);
        return tourAdminReviewDTO;
    }

    /**
     *  Delete the  tourAdminReview by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourAdminReview : {}", id);
        tourAdminReviewRepository.delete(id);
    }
}
