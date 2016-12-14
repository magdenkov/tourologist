package tech.bubbl.tourologist.service.dto.bubbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.service.dto.PayloadDTO;
import tech.bubbl.tourologist.service.dto.payload.*;
import tech.bubbl.tourologist.service.impl.TourServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
