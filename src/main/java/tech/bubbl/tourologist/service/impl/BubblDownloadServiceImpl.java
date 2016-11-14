package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.BubblDownloadService;
import tech.bubbl.tourologist.domain.BubblDownload;
import tech.bubbl.tourologist.repository.BubblDownloadRepository;
import tech.bubbl.tourologist.service.dto.BubblDownloadDTO;
import tech.bubbl.tourologist.service.mapper.BubblDownloadMapper;
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
 * Service Implementation for managing BubblDownload.
 */
@Service
@Transactional
public class BubblDownloadServiceImpl implements BubblDownloadService{

    private final Logger log = LoggerFactory.getLogger(BubblDownloadServiceImpl.class);
    
    @Inject
    private BubblDownloadRepository bubblDownloadRepository;

    @Inject
    private BubblDownloadMapper bubblDownloadMapper;

    /**
     * Save a bubblDownload.
     *
     * @param bubblDownloadDTO the entity to save
     * @return the persisted entity
     */
    public BubblDownloadDTO save(BubblDownloadDTO bubblDownloadDTO) {
        log.debug("Request to save BubblDownload : {}", bubblDownloadDTO);
        BubblDownload bubblDownload = bubblDownloadMapper.bubblDownloadDTOToBubblDownload(bubblDownloadDTO);
        bubblDownload = bubblDownloadRepository.save(bubblDownload);
        BubblDownloadDTO result = bubblDownloadMapper.bubblDownloadToBubblDownloadDTO(bubblDownload);
        return result;
    }

    /**
     *  Get all the bubblDownloads.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BubblDownloadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BubblDownloads");
        Page<BubblDownload> result = bubblDownloadRepository.findAll(pageable);
        return result.map(bubblDownload -> bubblDownloadMapper.bubblDownloadToBubblDownloadDTO(bubblDownload));
    }

    /**
     *  Get one bubblDownload by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BubblDownloadDTO findOne(Long id) {
        log.debug("Request to get BubblDownload : {}", id);
        BubblDownload bubblDownload = bubblDownloadRepository.findOne(id);
        BubblDownloadDTO bubblDownloadDTO = bubblDownloadMapper.bubblDownloadToBubblDownloadDTO(bubblDownload);
        return bubblDownloadDTO;
    }

    /**
     *  Delete the  bubblDownload by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BubblDownload : {}", id);
        bubblDownloadRepository.delete(id);
    }
}
