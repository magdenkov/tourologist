package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblRatingDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BubblRating and its DTO BubblRatingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface BubblRatingMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    BubblRatingDTO bubblRatingToBubblRatingDTO(BubblRating bubblRating);

    List<BubblRatingDTO> bubblRatingsToBubblRatingDTOs(List<BubblRating> bubblRatings);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "bubblId", target = "bubbl")
    BubblRating bubblRatingDTOToBubblRating(BubblRatingDTO bubblRatingDTO);

    List<BubblRating> bubblRatingDTOsToBubblRatings(List<BubblRatingDTO> bubblRatingDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }
}
