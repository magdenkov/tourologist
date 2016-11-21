package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourRoutePointService;
import tech.bubbl.tourologist.domain.TourRoutePoint;
import tech.bubbl.tourologist.repository.TourRoutePointRepository;
import tech.bubbl.tourologist.service.dto.TourRoutePointDTO;
import tech.bubbl.tourologist.service.mapper.TourRoutePointMapper;
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
 * Service Implementation for managing TourRoutePoint.
 */
@Service
@Transactional
public class TourRoutePointServiceImpl implements TourRoutePointService{

    private final Logger log = LoggerFactory.getLogger(TourRoutePointServiceImpl.class);
    
    @Inject
    private TourRoutePointRepository tourRoutePointRepository;

    @Inject
    private TourRoutePointMapper tourRoutePointMapper;

    /**
     * Save a tourRoutePoint.
     *
     * @param tourRoutePointDTO the entity to save
     * @return the persisted entity
     */
    public TourRoutePointDTO save(TourRoutePointDTO tourRoutePointDTO) {
        log.debug("Request to save TourRoutePoint : {}", tourRoutePointDTO);
        TourRoutePoint tourRoutePoint = tourRoutePointMapper.tourRoutePointDTOToTourRoutePoint(tourRoutePointDTO);
        tourRoutePoint = tourRoutePointRepository.save(tourRoutePoint);
        TourRoutePointDTO result = tourRoutePointMapper.tourRoutePointToTourRoutePointDTO(tourRoutePoint);
        return result;
    }

    /**
     *  Get all the tourRoutePoints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourRoutePointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourRoutePoints");
        Page<TourRoutePoint> result = tourRoutePointRepository.findAll(pageable);
        return result.map(tourRoutePoint -> tourRoutePointMapper.tourRoutePointToTourRoutePointDTO(tourRoutePoint));
    }

    /**
     *  Get one tourRoutePoint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourRoutePointDTO findOne(Long id) {
        log.debug("Request to get TourRoutePoint : {}", id);
        TourRoutePoint tourRoutePoint = tourRoutePointRepository.findOne(id);
        TourRoutePointDTO tourRoutePointDTO = tourRoutePointMapper.tourRoutePointToTourRoutePointDTO(tourRoutePoint);
        return tourRoutePointDTO;
    }

    /**
     *  Delete the  tourRoutePoint by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourRoutePoint : {}", id);
        tourRoutePointRepository.delete(id);
    }
}
