package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourDownloadService;
import tech.bubbl.tourologist.domain.TourDownload;
import tech.bubbl.tourologist.repository.TourDownloadRepository;
import tech.bubbl.tourologist.service.dto.TourDownloadDTO;
import tech.bubbl.tourologist.service.mapper.TourDownloadMapper;
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
 * Service Implementation for managing TourDownload.
 */
@Service
@Transactional
public class TourDownloadServiceImpl implements TourDownloadService{

    private final Logger log = LoggerFactory.getLogger(TourDownloadServiceImpl.class);
    
    @Inject
    private TourDownloadRepository tourDownloadRepository;

    @Inject
    private TourDownloadMapper tourDownloadMapper;

    /**
     * Save a tourDownload.
     *
     * @param tourDownloadDTO the entity to save
     * @return the persisted entity
     */
    public TourDownloadDTO save(TourDownloadDTO tourDownloadDTO) {
        log.debug("Request to save TourDownload : {}", tourDownloadDTO);
        TourDownload tourDownload = tourDownloadMapper.tourDownloadDTOToTourDownload(tourDownloadDTO);
        tourDownload = tourDownloadRepository.save(tourDownload);
        TourDownloadDTO result = tourDownloadMapper.tourDownloadToTourDownloadDTO(tourDownload);
        return result;
    }

    /**
     *  Get all the tourDownloads.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourDownloadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourDownloads");
        Page<TourDownload> result = tourDownloadRepository.findAll(pageable);
        return result.map(tourDownload -> tourDownloadMapper.tourDownloadToTourDownloadDTO(tourDownload));
    }

    /**
     *  Get one tourDownload by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourDownloadDTO findOne(Long id) {
        log.debug("Request to get TourDownload : {}", id);
        TourDownload tourDownload = tourDownloadRepository.findOne(id);
        TourDownloadDTO tourDownloadDTO = tourDownloadMapper.tourDownloadToTourDownloadDTO(tourDownload);
        return tourDownloadDTO;
    }

    /**
     *  Delete the  tourDownload by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourDownload : {}", id);
        tourDownloadRepository.delete(id);
    }
}
