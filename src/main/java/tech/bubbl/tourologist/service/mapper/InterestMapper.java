package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.InterestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Interest and its DTO InterestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InterestMapper {

    InterestDTO interestToInterestDTO(Interest interest);

    List<InterestDTO> interestsToInterestDTOs(List<Interest> interests);

    @Mapping(target = "tours", ignore = true)
    @Mapping(target = "bubbls", ignore = true)
    Interest interestDTOToInterest(InterestDTO interestDTO);

    List<Interest> interestDTOsToInterests(List<InterestDTO> interestDTOs);
}
