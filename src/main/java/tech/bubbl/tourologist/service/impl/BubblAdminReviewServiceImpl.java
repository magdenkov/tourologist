package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.BubblAdminReviewService;
import tech.bubbl.tourologist.domain.BubblAdminReview;
import tech.bubbl.tourologist.repository.BubblAdminReviewRepository;
import tech.bubbl.tourologist.service.dto.BubblAdminReviewDTO;
import tech.bubbl.tourologist.service.mapper.BubblAdminReviewMapper;
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
 * Service Implementation for managing BubblAdminReview.
 */
@Service
@Transactional
public class BubblAdminReviewServiceImpl implements BubblAdminReviewService{

    private final Logger log = LoggerFactory.getLogger(BubblAdminReviewServiceImpl.class);
    
    @Inject
    private BubblAdminReviewRepository bubblAdminReviewRepository;

    @Inject
    private BubblAdminReviewMapper bubblAdminReviewMapper;

    /**
     * Save a bubblAdminReview.
     *
     * @param bubblAdminReviewDTO the entity to save
     * @return the persisted entity
     */
    public BubblAdminReviewDTO save(BubblAdminReviewDTO bubblAdminReviewDTO) {
        log.debug("Request to save BubblAdminReview : {}", bubblAdminReviewDTO);
        BubblAdminReview bubblAdminReview = bubblAdminReviewMapper.bubblAdminReviewDTOToBubblAdminReview(bubblAdminReviewDTO);
        bubblAdminReview = bubblAdminReviewRepository.save(bubblAdminReview);
        BubblAdminReviewDTO result = bubblAdminReviewMapper.bubblAdminReviewToBubblAdminReviewDTO(bubblAdminReview);
        return result;
    }

    /**
     *  Get all the bubblAdminReviews.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BubblAdminReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BubblAdminReviews");
        Page<BubblAdminReview> result = bubblAdminReviewRepository.findAll(pageable);
        return result.map(bubblAdminReview -> bubblAdminReviewMapper.bubblAdminReviewToBubblAdminReviewDTO(bubblAdminReview));
    }

    /**
     *  Get one bubblAdminReview by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BubblAdminReviewDTO findOne(Long id) {
        log.debug("Request to get BubblAdminReview : {}", id);
        BubblAdminReview bubblAdminReview = bubblAdminReviewRepository.findOne(id);
        BubblAdminReviewDTO bubblAdminReviewDTO = bubblAdminReviewMapper.bubblAdminReviewToBubblAdminReviewDTO(bubblAdminReview);
        return bubblAdminReviewDTO;
    }

    /**
     *  Delete the  bubblAdminReview by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BubblAdminReview : {}", id);
        bubblAdminReviewRepository.delete(id);
    }
}
