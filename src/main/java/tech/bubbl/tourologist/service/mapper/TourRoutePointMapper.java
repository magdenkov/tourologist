package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourRoutePointDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourRoutePoint and its DTO TourRoutePointDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TourRoutePointMapper {

    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    TourRoutePointDTO tourRoutePointToTourRoutePointDTO(TourRoutePoint tourRoutePoint);

    List<TourRoutePointDTO> tourRoutePointsToTourRoutePointDTOs(List<TourRoutePoint> tourRoutePoints);

    @Mapping(source = "tourId", target = "tour")
    TourRoutePoint tourRoutePointDTOToTourRoutePoint(TourRoutePointDTO tourRoutePointDTO);

    List<TourRoutePoint> tourRoutePointDTOsToTourRoutePoints(List<TourRoutePointDTO> tourRoutePointDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
