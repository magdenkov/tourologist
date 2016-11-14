package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.BubblImageService;
import tech.bubbl.tourologist.domain.BubblImage;
import tech.bubbl.tourologist.repository.BubblImageRepository;
import tech.bubbl.tourologist.service.dto.BubblImageDTO;
import tech.bubbl.tourologist.service.mapper.BubblImageMapper;
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
 * Service Implementation for managing BubblImage.
 */
@Service
@Transactional
public class BubblImageServiceImpl implements BubblImageService{

    private final Logger log = LoggerFactory.getLogger(BubblImageServiceImpl.class);
    
    @Inject
    private BubblImageRepository bubblImageRepository;

    @Inject
    private BubblImageMapper bubblImageMapper;

    /**
     * Save a bubblImage.
     *
     * @param bubblImageDTO the entity to save
     * @return the persisted entity
     */
    public BubblImageDTO save(BubblImageDTO bubblImageDTO) {
        log.debug("Request to save BubblImage : {}", bubblImageDTO);
        BubblImage bubblImage = bubblImageMapper.bubblImageDTOToBubblImage(bubblImageDTO);
        bubblImage = bubblImageRepository.save(bubblImage);
        BubblImageDTO result = bubblImageMapper.bubblImageToBubblImageDTO(bubblImage);
        return result;
    }

    /**
     *  Get all the bubblImages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BubblImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BubblImages");
        Page<BubblImage> result = bubblImageRepository.findAll(pageable);
        return result.map(bubblImage -> bubblImageMapper.bubblImageToBubblImageDTO(bubblImage));
    }

    /**
     *  Get one bubblImage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BubblImageDTO findOne(Long id) {
        log.debug("Request to get BubblImage : {}", id);
        BubblImage bubblImage = bubblImageRepository.findOne(id);
        BubblImageDTO bubblImageDTO = bubblImageMapper.bubblImageToBubblImageDTO(bubblImage);
        return bubblImageDTO;
    }

    /**
     *  Delete the  bubblImage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BubblImage : {}", id);
        bubblImageRepository.delete(id);
    }
}
