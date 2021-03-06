package tech.bubbl.tourologist.service;

import com.google.maps.model.LatLng;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourCompleted;
import tech.bubbl.tourologist.domain.TourDownload;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.dto.tour.*;
import tech.bubbl.tourologist.service.dto.TourDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Tour.
 */
public interface TourService {

    /**
     * Save a tour.
     *
     * @param tourDTO the entity to save
     * @return the persisted entity
     */
    TourDTO save(TourDTO tourDTO);

    /**
     *  Get all the tours.
     * @param pageable the pagination information
     *  @param type
     * @param status @return the list of entities
     * @param userId
     * @param name
     */
    Page<GetAllToursDTO> findAllToursByUSerId(Pageable pageable, TourType type, Status status, Long userId, String name);

    /**
     *  Get the "id" tour.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TourFullDTO findOne(Long id);

    /**
     *  Delete the "id" tour.
     *
     * @param ids the id of the entity
     */
    void delete(List<Long> ids);

    TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO, LatLng origin, LatLng destination, TourType type);

    Page<Tour> findAllFixed(Double curLat, Double curLng, Pageable radius, String name, List<Long> exceptTourIds);

    List<TourFullDTO> getDIYTours(Double curLat, Double curLng, Double tarLat, Double tarLng, Double maxDelta);

    Page<GetAllToursDTO> findAllMyTours(Pageable pageable, TourType type, Status status, String name);


    Page<TourDownload> findMyFavoritesTours(Pageable pageable, TourType type, Status status, String name);

    List<RoutePointDTO> recalculateRoute(RecalculateRoutePointsDTO recalculateRoutePointsDTO);

    Page<TourCompleted> findMyCompletedTours(Pageable pageable, TourType type, Status status, String name);
}
