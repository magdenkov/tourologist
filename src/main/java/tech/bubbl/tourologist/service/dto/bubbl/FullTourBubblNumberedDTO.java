package tech.bubbl.tourologist.service.dto.bubbl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.service.dto.payload.PayloadBubblDTO;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class FullTourBubblNumberedDTO extends TourBubblNumberedDTO {

//    private final Logger log = LoggerFactory.getLogger(FullTourBubblNumberedDTO.class);

    public FullTourBubblNumberedDTO (Bubbl bubbl, Integer orderNumber, Long tourId) {
        super(bubbl, orderNumber, tourId);
//        log.error("processing {}  with order {}", bubbl.toString(), orderNumber);
        this.payloads = bubbl.getPayloads().stream()
            .map(payload -> new PayloadBubblDTO(payload))
            .collect(Collectors.toList());
        this.interests = new ArrayList<>(bubbl.getInterests());
    }

    private List<Interest> interests = new ArrayList<>();

    private List<PayloadBubblDTO> payloads = new ArrayList<>();


    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<PayloadBubblDTO> getPayloads() {
        return payloads;
    }

    public void setPayloads(List <PayloadBubblDTO> payloads) {
        this.payloads = payloads;
    }
}
