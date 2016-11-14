package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblDownloadDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BubblDownload and its DTO BubblDownloadDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface BubblDownloadMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    BubblDownloadDTO bubblDownloadToBubblDownloadDTO(BubblDownload bubblDownload);

    List<BubblDownloadDTO> bubblDownloadsToBubblDownloadDTOs(List<BubblDownload> bubblDownloads);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "bubblId", target = "bubbl")
    BubblDownload bubblDownloadDTOToBubblDownload(BubblDownloadDTO bubblDownloadDTO);

    List<BubblDownload> bubblDownloadDTOsToBubblDownloads(List<BubblDownloadDTO> bubblDownloadDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }
}
