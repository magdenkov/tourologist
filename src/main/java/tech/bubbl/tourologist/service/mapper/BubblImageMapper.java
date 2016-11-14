package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblImageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BubblImage and its DTO BubblImageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BubblImageMapper {

    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    BubblImageDTO bubblImageToBubblImageDTO(BubblImage bubblImage);

    List<BubblImageDTO> bubblImagesToBubblImageDTOs(List<BubblImage> bubblImages);

    @Mapping(source = "bubblId", target = "bubbl")
    BubblImage bubblImageDTOToBubblImage(BubblImageDTO bubblImageDTO);

    List<BubblImage> bubblImageDTOsToBubblImages(List<BubblImageDTO> bubblImageDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }
}
