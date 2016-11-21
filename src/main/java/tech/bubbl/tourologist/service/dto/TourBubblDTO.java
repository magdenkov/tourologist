package tech.bubbl.tourologist.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TourBubbl entity.
 */
public class TourBubblDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer orderNumber;


    private Long bubblId;
    

    private String bubblName;

    private Long tourId;
    

    private String tourName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public String getBubblName() {
        return bubblName;
    }

    public void setBubblName(String bubblName) {
        this.bubblName = bubblName;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }


    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TourBubblDTO tourBubblDTO = (TourBubblDTO) o;

        if ( ! Objects.equals(id, tourBubblDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourBubblDTO{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
