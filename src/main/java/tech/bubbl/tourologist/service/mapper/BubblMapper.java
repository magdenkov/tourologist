package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bubbl and its DTO BubblDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface BubblMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    BubblDTO bubblToBubblDTO(Bubbl bubbl);

    List<BubblDTO> bubblsToBubblDTOs(List<Bubbl> bubbls);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "tourId", target = "tour")
    @Mapping(target = "payloads", ignore = true)
    @Mapping(target = "bubblImages", ignore = true)
    Bubbl bubblDTOToBubbl(BubblDTO bubblDTO);

    List<Bubbl> bubblDTOsToBubbls(List<BubblDTO> bubblDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
