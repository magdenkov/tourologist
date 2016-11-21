package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.BubblRatingService;
import tech.bubbl.tourologist.domain.BubblRating;
import tech.bubbl.tourologist.repository.BubblRatingRepository;
import tech.bubbl.tourologist.service.dto.BubblRatingDTO;
import tech.bubbl.tourologist.service.mapper.BubblRatingMapper;
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
 * Service Implementation for managing BubblRating.
 */
@Service
@Transactional
public class BubblRatingServiceImpl implements BubblRatingService{

    private final Logger log = LoggerFactory.getLogger(BubblRatingServiceImpl.class);
    
    @Inject
    private BubblRatingRepository bubblRatingRepository;

    @Inject
    private BubblRatingMapper bubblRatingMapper;

    /**
     * Save a bubblRating.
     *
     * @param bubblRatingDTO the entity to save
     * @return the persisted entity
     */
    public BubblRatingDTO save(BubblRatingDTO bubblRatingDTO) {
        log.debug("Request to save BubblRating : {}", bubblRatingDTO);
        BubblRating bubblRating = bubblRatingMapper.bubblRatingDTOToBubblRating(bubblRatingDTO);
        bubblRating = bubblRatingRepository.save(bubblRating);
        BubblRatingDTO result = bubblRatingMapper.bubblRatingToBubblRatingDTO(bubblRating);
        return result;
    }

    /**
     *  Get all the bubblRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BubblRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BubblRatings");
        Page<BubblRating> result = bubblRatingRepository.findAll(pageable);
        return result.map(bubblRating -> bubblRatingMapper.bubblRatingToBubblRatingDTO(bubblRating));
    }

    /**
     *  Get one bubblRating by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BubblRatingDTO findOne(Long id) {
        log.debug("Request to get BubblRating : {}", id);
        BubblRating bubblRating = bubblRatingRepository.findOne(id);
        BubblRatingDTO bubblRatingDTO = bubblRatingMapper.bubblRatingToBubblRatingDTO(bubblRating);
        return bubblRatingDTO;
    }

    /**
     *  Delete the  bubblRating by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BubblRating : {}", id);
        bubblRatingRepository.delete(id);
    }
}
