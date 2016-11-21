package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.BubblService;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.repository.BubblRepository;
import tech.bubbl.tourologist.service.dto.BubblDTO;
import tech.bubbl.tourologist.service.mapper.BubblMapper;
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
 * Service Implementation for managing Bubbl.
 */
@Service
@Transactional
public class BubblServiceImpl implements BubblService{

    private final Logger log = LoggerFactory.getLogger(BubblServiceImpl.class);
    
    @Inject
    private BubblRepository bubblRepository;

    @Inject
    private BubblMapper bubblMapper;

    /**
     * Save a bubbl.
     *
     * @param bubblDTO the entity to save
     * @return the persisted entity
     */
    public BubblDTO save(BubblDTO bubblDTO) {
        log.debug("Request to save Bubbl : {}", bubblDTO);
        Bubbl bubbl = bubblMapper.bubblDTOToBubbl(bubblDTO);
        bubbl = bubblRepository.save(bubbl);
        BubblDTO result = bubblMapper.bubblToBubblDTO(bubbl);
        return result;
    }

    /**
     *  Get all the bubbls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BubblDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bubbls");
        Page<Bubbl> result = bubblRepository.findAll(pageable);
        return result.map(bubbl -> bubblMapper.bubblToBubblDTO(bubbl));
    }

    /**
     *  Get one bubbl by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BubblDTO findOne(Long id) {
        log.debug("Request to get Bubbl : {}", id);
        Bubbl bubbl = bubblRepository.findOneWithEagerRelationships(id);
        BubblDTO bubblDTO = bubblMapper.bubblToBubblDTO(bubbl);
        return bubblDTO;
    }

    /**
     *  Delete the  bubbl by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bubbl : {}", id);
        bubblRepository.delete(id);
    }
}
