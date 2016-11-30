package tech.bubbl.tourologist.service.dto.tour;

import tech.bubbl.tourologist.domain.TourRoutePoint;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the TourRoutePoint entity.
 */
public class RoutePointDTO implements Serializable {


    private Double lat;

    private Double lng;

    @Min(value = 0)
    private Integer orderNumber;

    public RoutePointDTO(TourRoutePoint tourRoutePoint) {
        setLat(tourRoutePoint.getLat());
        setLng(tourRoutePoint.getLng());
        setOrderNumber(tourRoutePoint.getOrderNumber());
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoutePointDTO)) return false;

        RoutePointDTO that = (RoutePointDTO) o;

        if (!getLat().equals(that.getLat())) return false;
        if (!getLng().equals(that.getLng())) return false;
        return getOrderNumber().equals(that.getOrderNumber());

    }

    @Override
    public int hashCode() {
        int result = getLat().hashCode();
        result = 31 * result + getLng().hashCode();
        result = 31 * result + getOrderNumber().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TourRoutePointDTO{" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
