package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourBubblRoutePointDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourBubblRoutePoint.
 */
public interface TourBubblRoutePointService {

    /**
     * Save a tourBubblRoutePoint.
     *
     * @param tourBubblRoutePointDTO the entity to save
     * @return the persisted entity
     */
    TourBubblRoutePointDTO save(TourBubblRoutePointDTO tourBubblRoutePointDTO);

    /**
     *  Get all the tourBubblRoutePoints.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourBubblRoutePointDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourBubblRoutePoint.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourBubblRoutePointDTO findOne(Long id);

    /**
     *  Delete the "id" tourBubblRoutePoint.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
