package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.repository.TourRepository;
import tech.bubbl.tourologist.service.dto.TourDTO;
import tech.bubbl.tourologist.service.mapper.TourMapper;
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
 * Service Implementation for managing Tour.
 */
@Service
@Transactional
public class TourServiceImpl implements TourService{

    private final Logger log = LoggerFactory.getLogger(TourServiceImpl.class);
    
    @Inject
    private TourRepository tourRepository;

    @Inject
    private TourMapper tourMapper;

    /**
     * Save a tour.
     *
     * @param tourDTO the entity to save
     * @return the persisted entity
     */
    public TourDTO save(TourDTO tourDTO) {
        log.debug("Request to save Tour : {}", tourDTO);
        Tour tour = tourMapper.tourDTOToTour(tourDTO);
        tour = tourRepository.save(tour);
        TourDTO result = tourMapper.tourToTourDTO(tour);
        return result;
    }

    /**
     *  Get all the tours.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tours");
        Page<Tour> result = tourRepository.findAll(pageable);
        return result.map(tour -> tourMapper.tourToTourDTO(tour));
    }

    /**
     *  Get one tour by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourDTO findOne(Long id) {
        log.debug("Request to get Tour : {}", id);
        Tour tour = tourRepository.findOneWithEagerRelationships(id);
        TourDTO tourDTO = tourMapper.tourToTourDTO(tour);
        return tourDTO;
    }

    /**
     *  Delete the  tour by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tour : {}", id);
        tourRepository.delete(id);
    }
}
