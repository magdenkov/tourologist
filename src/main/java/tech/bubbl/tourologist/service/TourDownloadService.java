package tech.bubbl.tourologist.service;

import tech.bubbl.tourologist.service.dto.TourDownloadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TourDownload.
 */
public interface TourDownloadService {

    /**
     * Save a tourDownload.
     *
     * @param tourDownloadDTO the entity to save
     * @return the persisted entity
     */
    TourDownloadDTO save(TourDownloadDTO tourDownloadDTO);

    /**
     *  Get all the tourDownloads.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TourDownloadDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" tourDownload.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourDownloadDTO findOne(Long id);

    /**
     *  Delete the "id" tourDownload.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Boolean addTourToFavorites(Long tourId);

    Boolean removeTourFromFavorites(Long tourId);

    boolean markTourAsCompleted(Long tourId);

    boolean removeTourFromCompleted(Long tourId);
}
