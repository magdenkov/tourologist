package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourBubblRoutePointDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourBubblRoutePoint and its DTO TourBubblRoutePointDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TourBubblRoutePointMapper {

    @Mapping(source = "tourBubbl.id", target = "tourBubblId")
    TourBubblRoutePointDTO tourBubblRoutePointToTourBubblRoutePointDTO(TourBubblRoutePoint tourBubblRoutePoint);

    List<TourBubblRoutePointDTO> tourBubblRoutePointsToTourBubblRoutePointDTOs(List<TourBubblRoutePoint> tourBubblRoutePoints);

    @Mapping(source = "tourBubblId", target = "tourBubbl")
    TourBubblRoutePoint tourBubblRoutePointDTOToTourBubblRoutePoint(TourBubblRoutePointDTO tourBubblRoutePointDTO);

    List<TourBubblRoutePoint> tourBubblRoutePointDTOsToTourBubblRoutePoints(List<TourBubblRoutePointDTO> tourBubblRoutePointDTOs);

    default TourBubbl tourBubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        TourBubbl tourBubbl = new TourBubbl();
        tourBubbl.setId(id);
        return tourBubbl;
    }
}
