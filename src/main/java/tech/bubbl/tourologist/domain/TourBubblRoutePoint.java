package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TourBubblRoutePoint.
 */
@Entity
@Table(name = "tour_bubbl_route_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourBubblRoutePoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Min(value = 0)
    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    private TourBubbl tourBubbl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public TourBubblRoutePoint lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public TourBubblRoutePoint lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public TourBubblRoutePoint orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TourBubbl getTourBubbl() {
        return tourBubbl;
    }

    public TourBubblRoutePoint tourBubbl(TourBubbl tourBubbl) {
        this.tourBubbl = tourBubbl;
        return this;
    }

    public void setTourBubbl(TourBubbl tourBubbl) {
        this.tourBubbl = tourBubbl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TourBubblRoutePoint tourBubblRoutePoint = (TourBubblRoutePoint) o;
        if(tourBubblRoutePoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourBubblRoutePoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourBubblRoutePoint{" +
            "id=" + id +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
