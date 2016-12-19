package tech.bubbl.tourologist.service;

import com.google.maps.model.LatLng;
import org.springframework.http.ResponseEntity;
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
     *  @param id the id of the entity
     */
    void delete(Long id);

    TourFullDTO saveFixedTour(CreateFixedTourDTO tourDTO, LatLng origin, LatLng destination, TourType type);

    List<GetAllToursDTO> findAllFixed(Double curLat, Double curLng, Double radius, String name);

    List<TourFullDTO> getDIYTours(Double curLat, Double curLng, Double tarLat, Double tarLng);

    Page<GetAllToursDTO> findAllMyTours(Pageable pageable, TourType type, Status status, String name);


    Page<TourDownload> findMyFavoritesTours(Pageable pageable, TourType type, Status status, String name);

    List<RoutePointDTO> recalculateRoute(RecalculateRoutePointsDTO recalculateRoutePointsDTO);
}
