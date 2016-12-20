package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.BubblDownloadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing BubblDownload.
 */
public interface BubblDownloadService {

    /**
     * Save a bubblDownload.
     *
     * @param bubblDownloadDTO the entity to save
     * @return the persisted entity
     */
    BubblDownloadDTO save(BubblDownloadDTO bubblDownloadDTO);

    /**
     *  Get all the bubblDownloads.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BubblDownloadDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bubblDownload.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BubblDownloadDTO findOne(Long id);

    /**
     *  Delete the "id" bubblDownload.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    boolean addBubblToFavorites(Long tourId);

    boolean removeBubblFromFavorites(Long tourId);
}
