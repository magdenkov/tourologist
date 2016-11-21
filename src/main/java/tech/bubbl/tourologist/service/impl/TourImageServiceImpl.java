package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourImageService;
import tech.bubbl.tourologist.domain.TourImage;
import tech.bubbl.tourologist.repository.TourImageRepository;
import tech.bubbl.tourologist.service.dto.TourImageDTO;
import tech.bubbl.tourologist.service.mapper.TourImageMapper;
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
 * Service Implementation for managing TourImage.
 */
@Service
@Transactional
public class TourImageServiceImpl implements TourImageService{

    private final Logger log = LoggerFactory.getLogger(TourImageServiceImpl.class);
    
    @Inject
    private TourImageRepository tourImageRepository;

    @Inject
    private TourImageMapper tourImageMapper;

    /**
     * Save a tourImage.
     *
     * @param tourImageDTO the entity to save
     * @return the persisted entity
     */
    public TourImageDTO save(TourImageDTO tourImageDTO) {
        log.debug("Request to save TourImage : {}", tourImageDTO);
        TourImage tourImage = tourImageMapper.tourImageDTOToTourImage(tourImageDTO);
        tourImage = tourImageRepository.save(tourImage);
        TourImageDTO result = tourImageMapper.tourImageToTourImageDTO(tourImage);
        return result;
    }

    /**
     *  Get all the tourImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourImages");
        Page<TourImage> result = tourImageRepository.findAll(pageable);
        return result.map(tourImage -> tourImageMapper.tourImageToTourImageDTO(tourImage));
    }

    /**
     *  Get one tourImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourImageDTO findOne(Long id) {
        log.debug("Request to get TourImage : {}", id);
        TourImage tourImage = tourImageRepository.findOne(id);
        TourImageDTO tourImageDTO = tourImageMapper.tourImageToTourImageDTO(tourImage);
        return tourImageDTO;
    }

    /**
     *  Delete the  tourImage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourImage : {}", id);
        tourImageRepository.delete(id);
    }
}
