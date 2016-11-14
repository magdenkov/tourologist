package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourRoutePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourRoutePoint.
 */
public interface TourRoutePointService {

    /**
     * Save a tourRoutePoint.
     *
     * @param tourRoutePointDTO the entity to save
     * @return the persisted entity
     */
    TourRoutePointDTO save(TourRoutePointDTO tourRoutePointDTO);

    /**
     *  Get all the tourRoutePoints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourRoutePointDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourRoutePoint.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourRoutePointDTO findOne(Long id);

    /**
     *  Delete the "id" tourRoutePoint.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
