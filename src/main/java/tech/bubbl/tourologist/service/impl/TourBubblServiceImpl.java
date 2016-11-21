package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.TourBubblService;
import tech.bubbl.tourologist.domain.TourBubbl;
import tech.bubbl.tourologist.repository.TourBubblRepository;
import tech.bubbl.tourologist.service.dto.TourBubblDTO;
import tech.bubbl.tourologist.service.mapper.TourBubblMapper;
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
 * Service Implementation for managing TourBubbl.
 */
@Service
@Transactional
public class TourBubblServiceImpl implements TourBubblService{

    private final Logger log = LoggerFactory.getLogger(TourBubblServiceImpl.class);
    
    @Inject
    private TourBubblRepository tourBubblRepository;

    @Inject
    private TourBubblMapper tourBubblMapper;

    /**
     * Save a tourBubbl.
     *
     * @param tourBubblDTO the entity to save
     * @return the persisted entity
     */
    public TourBubblDTO save(TourBubblDTO tourBubblDTO) {
        log.debug("Request to save TourBubbl : {}", tourBubblDTO);
        TourBubbl tourBubbl = tourBubblMapper.tourBubblDTOToTourBubbl(tourBubblDTO);
        tourBubbl = tourBubblRepository.save(tourBubbl);
        TourBubblDTO result = tourBubblMapper.tourBubblToTourBubblDTO(tourBubbl);
        return result;
    }

    /**
     *  Get all the tourBubbls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TourBubblDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TourBubbls");
        Page<TourBubbl> result = tourBubblRepository.findAll(pageable);
        return result.map(tourBubbl -> tourBubblMapper.tourBubblToTourBubblDTO(tourBubbl));
    }

    /**
     *  Get one tourBubbl by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TourBubblDTO findOne(Long id) {
        log.debug("Request to get TourBubbl : {}", id);
        TourBubbl tourBubbl = tourBubblRepository.findOne(id);
        TourBubblDTO tourBubblDTO = tourBubblMapper.tourBubblToTourBubblDTO(tourBubbl);
        return tourBubblDTO;
    }

    /**
     *  Delete the  tourBubbl by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TourBubbl : {}", id);
        tourBubblRepository.delete(id);
    }
}
