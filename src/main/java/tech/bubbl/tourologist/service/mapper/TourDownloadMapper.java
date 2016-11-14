package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.TourDownloadDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TourDownload and its DTO TourDownloadDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TourDownloadMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.name", target = "tourName")
    TourDownloadDTO tourDownloadToTourDownloadDTO(TourDownload tourDownload);

    List<TourDownloadDTO> tourDownloadsToTourDownloadDTOs(List<TourDownload> tourDownloads);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "tourId", target = "tour")
    TourDownload tourDownloadDTOToTourDownload(TourDownloadDTO tourDownloadDTO);

    List<TourDownload> tourDownloadDTOsToTourDownloads(List<TourDownloadDTO> tourDownloadDTOs);

    default Tour tourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tour tour = new Tour();
        tour.setId(id);
        return tour;
    }
}
