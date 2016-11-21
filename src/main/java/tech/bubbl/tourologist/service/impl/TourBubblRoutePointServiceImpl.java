package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourBubblRoutePointService;
import tech.bubbl.tourologist.domain.TourBubblRoutePoint;
import tech.bubbl.tourologist.repository.TourBubblRoutePointRepository;
import tech.bubbl.tourologist.service.dto.TourBubblRoutePointDTO;
import tech.bubbl.tourologist.service.mapper.TourBubblRoutePointMapper;
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
 * Service Implementation for managing TourBubblRoutePoint.
 */
@Service
@Transactional
public class TourBubblRoutePointServiceImpl implements TourBubblRoutePointService{

    private final Logger log = LoggerFactory.getLogger(TourBubblRoutePointServiceImpl.class);
    
    @Inject
    private TourBubblRoutePointRepository tourBubblRoutePointRepository;

    @Inject
    private TourBubblRoutePointMapper tourBubblRoutePointMapper;

    /**
     * Save a tourBubblRoutePoint.
     *
     * @param tourBubblRoutePointDTO the entity to save
     * @return the persisted entity
     */
    public TourBubblRoutePointDTO save(TourBubblRoutePointDTO tourBubblRoutePointDTO) {
        log.debug("Request to save TourBubblRoutePoint : {}", tourBubblRoutePointDTO);
        TourBubblRoutePoint tourBubblRoutePoint = tourBubblRoutePointMapper.tourBubblRoutePointDTOToTourBubblRoutePoint(tourBubblRoutePointDTO);
        tourBubblRoutePoint = tourBubblRoutePointRepository.save(tourBubblRoutePoint);
        TourBubblRoutePointDTO result = tourBubblRoutePointMapper.tourBubblRoutePointToTourBubblRoutePointDTO(tourBubblRoutePoint);
        return result;
    }

    /**
     *  Get all the tourBubblRoutePoints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourBubblRoutePointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourBubblRoutePoints");
        Page<TourBubblRoutePoint> result = tourBubblRoutePointRepository.findAll(pageable);
        return result.map(tourBubblRoutePoint -> tourBubblRoutePointMapper.tourBubblRoutePointToTourBubblRoutePointDTO(tourBubblRoutePoint));
    }

    /**
     *  Get one tourBubblRoutePoint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourBubblRoutePointDTO findOne(Long id) {
        log.debug("Request to get TourBubblRoutePoint : {}", id);
        TourBubblRoutePoint tourBubblRoutePoint = tourBubblRoutePointRepository.findOne(id);
        TourBubblRoutePointDTO tourBubblRoutePointDTO = tourBubblRoutePointMapper.tourBubblRoutePointToTourBubblRoutePointDTO(tourBubblRoutePoint);
        return tourBubblRoutePointDTO;
    }

    /**
     *  Delete the  tourBubblRoutePoint by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourBubblRoutePoint : {}", id);
        tourBubblRoutePointRepository.delete(id);
    }
}
