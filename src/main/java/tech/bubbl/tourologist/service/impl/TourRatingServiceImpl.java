package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourRatingService;
import tech.bubbl.tourologist.domain.TourRating;
import tech.bubbl.tourologist.repository.TourRatingRepository;
import tech.bubbl.tourologist.service.dto.TourRatingDTO;
import tech.bubbl.tourologist.service.mapper.TourRatingMapper;
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
 * Service Implementation for managing TourRating.
 */
@Service
@Transactional
public class TourRatingServiceImpl implements TourRatingService{

    private final Logger log = LoggerFactory.getLogger(TourRatingServiceImpl.class);
    
    @Inject
    private TourRatingRepository tourRatingRepository;

    @Inject
    private TourRatingMapper tourRatingMapper;

    /**
     * Save a tourRating.
     *
     * @param tourRatingDTO the entity to save
     * @return the persisted entity
     */
    public TourRatingDTO save(TourRatingDTO tourRatingDTO) {
        log.debug("Request to save TourRating : {}", tourRatingDTO);
        TourRating tourRating = tourRatingMapper.tourRatingDTOToTourRating(tourRatingDTO);
        tourRating = tourRatingRepository.save(tourRating);
        TourRatingDTO result = tourRatingMapper.tourRatingToTourRatingDTO(tourRating);
        return result;
    }

    /**
     *  Get all the tourRatings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourRatings");
        Page<TourRating> result = tourRatingRepository.findAll(pageable);
        return result.map(tourRating -> tourRatingMapper.tourRatingToTourRatingDTO(tourRating));
    }

    /**
     *  Get one tourRating by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourRatingDTO findOne(Long id) {
        log.debug("Request to get TourRating : {}", id);
        TourRating tourRating = tourRatingRepository.findOne(id);
        TourRatingDTO tourRatingDTO = tourRatingMapper.tourRatingToTourRatingDTO(tourRating);
        return tourRatingDTO;
    }

    /**
     *  Delete the  tourRating by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourRating : {}", id);
        tourRatingRepository.delete(id);
    }
}
