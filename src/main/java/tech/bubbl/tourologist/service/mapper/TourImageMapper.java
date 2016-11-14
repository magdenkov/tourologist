package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourImageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourImage and its DTO TourImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TourImageMapper {

    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    TourImageDTO tourImageToTourImageDTO(TourImage tourImage);

    List<TourImageDTO> tourImagesToTourImageDTOs(List<TourImage> tourImages);

    @Mapping(source = "tourId", target = "tour")
    TourImage tourImageDTOToTourImage(TourImageDTO tourImageDTO);

    List<TourImage> tourImageDTOsToTourImages(List<TourImageDTO> tourImageDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
