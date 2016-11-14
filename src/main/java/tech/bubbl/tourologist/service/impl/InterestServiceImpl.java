package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.InterestService;
import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.repository.InterestRepository;
import tech.bubbl.tourologist.service.dto.InterestDTO;
import tech.bubbl.tourologist.service.mapper.InterestMapper;
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
 * Service Implementation for managing Interest.
 */
@Service
@Transactional
public class InterestServiceImpl implements InterestService{

    private final Logger log = LoggerFactory.getLogger(InterestServiceImpl.class);
    
    @Inject
    private InterestRepository interestRepository;

    @Inject
    private InterestMapper interestMapper;

    /**
     * Save a interest.
     *
     * @param interestDTO the entity to save
     * @return the persisted entity
     */
    public InterestDTO save(InterestDTO interestDTO) {
        log.debug("Request to save Interest : {}", interestDTO);
        Interest interest = interestMapper.interestDTOToInterest(interestDTO);
        interest = interestRepository.save(interest);
        InterestDTO result = interestMapper.interestToInterestDTO(interest);
        return result;
    }

    /**
     *  Get all the interests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<InterestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Interests");
        Page<Interest> result = interestRepository.findAll(pageable);
        return result.map(interest -> interestMapper.interestToInterestDTO(interest));
    }

    /**
     *  Get one interest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public InterestDTO findOne(Long id) {
        log.debug("Request to get Interest : {}", id);
        Interest interest = interestRepository.findOne(id);
        InterestDTO interestDTO = interestMapper.interestToInterestDTO(interest);
        return interestDTO;
    }

    /**
     *  Delete the  interest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Interest : {}", id);
        interestRepository.delete(id);
    }
}
