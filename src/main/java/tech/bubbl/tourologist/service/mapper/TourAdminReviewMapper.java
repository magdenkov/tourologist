package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourAdminReviewDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourAdminReview and its DTO TourAdminReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TourAdminReviewMapper {

    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    TourAdminReviewDTO tourAdminReviewToTourAdminReviewDTO(TourAdminReview tourAdminReview);

    List<TourAdminReviewDTO> tourAdminReviewsToTourAdminReviewDTOs(List<TourAdminReview> tourAdminReviews);

    @Mapping(source = "tourId", target = "tour")
    @Mapping(source = "userId", target = "user")
    TourAdminReview tourAdminReviewDTOToTourAdminReview(TourAdminReviewDTO tourAdminReviewDTO);

    List<TourAdminReview> tourAdminReviewDTOsToTourAdminReviews(List<TourAdminReviewDTO> tourAdminReviewDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
