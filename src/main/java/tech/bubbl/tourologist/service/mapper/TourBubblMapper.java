package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourBubblDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourBubbl and its DTO TourBubblNumberedDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TourBubblMapper {

    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    TourBubblDTO tourBubblToTourBubblDTO(TourBubbl tourBubbl);

    List<TourBubblDTO> tourBubblsToTourBubblDTOs(List<TourBubbl> tourBubbls);

    @Mapping(source = "bubblId", target = "bubbl")
    @Mapping(source = "tourId", target = "tour")
    @Mapping(target = "tourBubbls", ignore = true)
    TourBubbl tourBubblDTOToTourBubbl(TourBubblDTO tourBubblDTO);

    List<TourBubbl> tourBubblDTOsToTourBubbls(List<TourBubblDTO> tourBubblDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
