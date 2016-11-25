package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.GetAllToursDTO;
import tech.bubbl.tourologist.service.dto.TourDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Tour.
 */
public interface TourService {

    /**
     * Save a tour.
     *
     * @param tourDTO the entity to save
     * @return the persisted entity
     */
    TourDTO save(TourDTO tourDTO);

    /**
     *  Get all the tours.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<GetAllToursDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tour.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourDTO findOne(Long id);

    /**
     *  Delete the "id" tour.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
