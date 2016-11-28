package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.service.PayloadService;
import tech.bubbl.tourologist.domain.Payload;
import tech.bubbl.tourologist.repository.PayloadRepository;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
import tech.bubbl.tourologist.service.mapper.PayloadMapper;
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
 * Service Implementation for managing Payload.
 */
@Service
@Transactional
public class PayloadServiceImpl implements PayloadService{

    private final Logger log = LoggerFactory.getLogger(PayloadServiceImpl.class);

    @Inject
    private PayloadRepository payloadRepository;

    @Inject
    private PayloadMapper payloadMapper;

    /**
     * Save a payload.
     *
     * @param payloadDTO the entity to save
     * @return the persisted entity
     */
    public PayloadDTO save(PayloadDTO payloadDTO) {
        log.debug("Request to save Payload : {}", payloadDTO);
        // todo copy paste upload to s3


        Payload payload = payloadMapper.payloadDTOToPayload(payloadDTO);
        payload = payloadRepository.save(payload);
        PayloadDTO result = payloadMapper.payloadToPayloadDTO(payload);
        return result;
    }

    /**
     *  Get all the payloads.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PayloadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payloads");
        Page<Payload> result = payloadRepository.findAll(pageable);
        return result.map(payload -> payloadMapper.payloadToPayloadDTO(payload));
    }

    /**
     *  Get one payload by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PayloadDTO findOne(Long id) {
        log.debug("Request to get Payload : {}", id);
        Payload payload = payloadRepository.findOne(id);
        PayloadDTO payloadDTO = payloadMapper.payloadToPayloadDTO(payload);
        return payloadDTO;
    }

    /**
     *  Delete the  payload by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payload : {}", id);
        payloadRepository.delete(id);
    }
}
