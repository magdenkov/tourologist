package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tour and its DTO TourDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, InterestMapper.class, })
public interface TourMapper {

    @Mapping(source = "user.id", target = "userId")
    TourDTO tourToTourDTO(Tour tour);

    List<TourDTO> toursToTourDTOs(List<Tour> tours);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "bubbls", ignore = true)
    @Mapping(target = "tourImages", ignore = true)
    @Mapping(target = "tourRoutePoints", ignore = true)
    Tour tourDTOToTour(TourDTO tourDTO);

    List<Tour> tourDTOsToTours(List<TourDTO> tourDTOs);

    default Interest interestFromId(Long id) {
        if (id == null) {
            return null;
        }
        Interest interest = new Interest();
        interest.setId(id);
        return interest;
    }
}
