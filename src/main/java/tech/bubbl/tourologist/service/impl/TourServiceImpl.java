package tech.bubbl.tourologist.service.impl;

import tech.bubbl.tourologist.domain.TourBubbl;
import tech.bubbl.tourologist.domain.User;
import tech.bubbl.tourologist.repository.TourBubblRepository;
import tech.bubbl.tourologist.repository.UserRepository;
import tech.bubbl.tourologist.security.SecurityUtils;
import tech.bubbl.tourologist.service.TourService;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.repository.TourRepository;
import tech.bubbl.tourologist.service.dto.tour.CreateFixedTourDTO;
import tech.bubbl.tourologist.service.dto.tour.CreateTourBubblDTO;
import tech.bubbl.tourologist.service.dto.tour.GetAllToursDTO;
import tech.bubbl.tourologist.service.dto.TourDTO;
import tech.bubbl.tourologist.service.dto.tour.TourFullDTO;
import tech.bubbl.tourologist.service.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing Tour.
 */
@Service
@Transactional
public class TourServiceImpl implements TourService{

    private final Logger log = LoggerFactory.getLogger(TourServiceImpl.class);

    @Inject
    private TourRepository tourRepository;

    @Inject
    private TourMapper tourMapper;

    @Inject
    private InterestMapper interestMapper;

    @Inject
    private BubblMapper bubblMapper;

    @Inject
    private TourImageMapper tourImageMapper;

    @Inject
    private TourRoutePointMapper tourRoutePointMapper;

    @Inject
    private TourBubblMapper tourBubblMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TourBubblRepository tourBubblRepository;

    public TourDTO save(TourDTO tourDTO) {
        log.debug("Request to save Tour : {}", tourDTO);
        Tour tour = tourMapper.tourDTOToTour(tourDTO);
        tour = tourRepository.save(tour);
        TourDTO result = tourMapper.tourToTourDTO(tour);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<GetAllToursDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tours");
        Page<Tour> result = tourRepository.findAll(pageable);
        return result.map(GetAllToursDTO::new);
    }

    @Transactional(readOnly = true)
    public TourFullDTO findOne(Long id) {
        log.debug("Request to get Tour : {}", id);
        Tour tour = tourRepository.findOneWithEagerRelationships(id);
        return new TourFullDTO(tour, tourImageMapper);
    }

    public void delete(Long id) {
        log.debug("Request to delete Tour : {}", id);
        tourRepository.delete(id);
    }

    @Override
    @Transactional
    public TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Tour finalTour = tourRepository.save(tourDTO.createTour(user));

        tourDTO.getBubbls().stream().forEach(createTourBubblDTO -> {
            TourBubbl tourBubbl = new TourBubbl();
            tourBubbl.setOrderNumber(createTourBubblDTO.getOrderNumber());
            tourBubbl.setTour(finalTour);
            tourBubbl.setBubbl(tourBubblMapper.bubblFromId(createTourBubblDTO.getBubblId()));
            tourBubblRepository.save(tourBubbl);
        });


        return new TourFullDTO(finalTour, tourImageMapper);
    }


}
