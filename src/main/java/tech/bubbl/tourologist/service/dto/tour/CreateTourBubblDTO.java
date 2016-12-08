package tech.bubbl.tourologist.service.dto.tour;

import java.io.Serializable;

/**
 * Created by Denis Magdenkov on 28.11.2016.
 */
public class CreateTourBubblDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderNumber;

    private Long bubblId;

    public CreateTourBubblDTO() {
    }

    public CreateTourBubblDTO orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public CreateTourBubblDTO(Integer orderNumber, Long bubblId) {
        this.orderNumber = orderNumber;
        this.bubblId = bubblId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getBubblId() {
        return bubblId;
    }

    public void setBubblId(Long bubblId) {
        this.bubblId = bubblId;
    }
}
