package tech.bubbl.tourologist.service.mapper;

import tech.bubbl.tourologist.domain.*;
import tech.bubbl.tourologist.service.dto.BubblAdminReviewDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BubblAdminReview and its DTO BubblAdminReviewDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface BubblAdminReviewMapper {

    @Mapping(source = "bubbl.id", target = "bubblId")
    @Mapping(source = "bubbl.name", target = "bubblName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    BubblAdminReviewDTO bubblAdminReviewToBubblAdminReviewDTO(BubblAdminReview bubblAdminReview);

    List<BubblAdminReviewDTO> bubblAdminReviewsToBubblAdminReviewDTOs(List<BubblAdminReview> bubblAdminReviews);

    @Mapping(source = "bubblId", target = "bubbl")
    @Mapping(source = "userId", target = "user")
    BubblAdminReview bubblAdminReviewDTOToBubblAdminReview(BubblAdminReviewDTO bubblAdminReviewDTO);

    List<BubblAdminReview> bubblAdminReviewDTOsToBubblAdminReviews(List<BubblAdminReviewDTO> bubblAdminReviewDTOs);

    default Bubbl bubblFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bubbl bubbl = new Bubbl();
        bubbl.setId(id);
        return bubbl;
    }
}
