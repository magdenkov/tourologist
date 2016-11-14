package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.PayloadDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Payload and its DTO PayloadDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PayloadMapper {

    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    PayloadDTO payloadToPayloadDTO(Payload payload);

    List<PayloadDTO> payloadsToPayloadDTOs(List<Payload> payloads);

    @Mapping(source = "bubblId", target = "bubbl")
    Payload payloadDTOToPayload(PayloadDTO payloadDTO);

    List<Payload> payloadDTOsToPayloads(List<PayloadDTO> payloadDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }
}
