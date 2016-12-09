package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;

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
     *  @param status
     *@param userId @return the list of entities
     */
    Page<FullTourBubblNumberedDTO> findAll(Pageable pageable, Status status, Long userId);

    /**
     *  Get the "id" bubbl.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FullTourBubblNumberedDTO findOne(Long id);

    /**
     *  Delete the "id" bubbl.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List <Bubbl> findBubblsSurprise(Double curLat, Double curLng, Double radius);

    List<String> reverseGeocode(Double lat, Double lng);
}
