package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourRatingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourRating and its DTO TourRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TourRatingMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    TourRatingDTO tourRatingToTourRatingDTO(TourRating tourRating);

    List<TourRatingDTO> tourRatingsToTourRatingDTOs(List<TourRating> tourRatings);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "tourId", target = "tour")
    TourRating tourRatingDTOToTourRating(TourRatingDTO tourRatingDTO);

    List<TourRating> tourRatingDTOsToTourRatings(List<TourRatingDTO> tourRatingDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
