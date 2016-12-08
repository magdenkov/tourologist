package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.BubblDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.dto.bubbl.TourBubblNumberedDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Bubbl.
 */
public interface BubblService {

    /**
     * Save a bubbl.
     *
     * @param bubblDTO the entity to save
     * @return the persisted entity
     */
    BubblDTO save(BubblDTO bubblDTO);

    /**
     *  Get all the bubbls.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BubblDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bubbl.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BubblDTO findOne(Long id);

    /**
     *  Delete the "id" bubbl.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List <FullTourBubblNumberedDTO> findBubblsSurprise(Double curLat, Double curLng, Double radius);
}
