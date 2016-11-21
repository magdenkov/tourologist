package tech.bubbl.tourologist.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TourBubblRoutePoint entity.
 */
public class TourBubblRoutePointDTO implements Serializable {

    private Long id;

    private Double lat;

    private Double lng;

    @Min(value = 0)
    private Integer orderNumber;


    private Long tourBubblId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getTourBubblId() {
        return tourBubblId;
    }

    public void setTourBubblId(Long tourBubblId) {
        this.tourBubblId = tourBubblId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TourBubblRoutePointDTO tourBubblRoutePointDTO = (TourBubblRoutePointDTO) o;

        if ( ! Objects.equals(id, tourBubblRoutePointDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourBubblRoutePointDTO{" +
            "id=" + id +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
