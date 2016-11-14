package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.InterestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Interest.
 */
public interface InterestService {

    /**
     * Save a interest.
     *
     * @param interestDTO the entity to save
     * @return the persisted entity
     */
    InterestDTO save(InterestDTO interestDTO);

    /**
     *  Get all the interests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InterestDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" interest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InterestDTO findOne(Long id);

    /**
     *  Delete the "id" interest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
