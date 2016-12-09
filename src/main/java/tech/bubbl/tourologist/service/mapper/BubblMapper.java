package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bubbl and its DTO BubblDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, InterestMapper.class, })
public interface BubblMapper {


    BubblDTO bubblToBubblDTO(Bubbl bubbl);

    List<BubblDTO> bubblsToBubblDTOs(List<Bubbl> bubbls);


    @Mapping(target = "bubblRatings", ignore = true)
    @Mapping(target = "bubblDownloads", ignore = true)
//    @Mapping(target = "payloads", ignore = true)
    @Mapping(target = "bubblAdminReviews", ignore = true)
    @Mapping(target = "tourBubbls", ignore = true)
    Bubbl bubblDTOToBubbl(BubblDTO bubblDTO);

    List<Bubbl> bubblDTOsToBubbls(List<BubblDTO> bubblDTOs);

    default Interest interestFromId(Long id) {
        if (id == null) {
            return null;
        }
        Interest interest = new Interest();
        interest.setId(id);
        return interest;
    }
}
