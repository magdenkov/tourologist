package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TourRoutePoint.
 */
@Entity
@Table(name = "tour_route_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourRoutePoint implements Serializable {

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
    private Tour tour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public TourRoutePoint lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public TourRoutePoint lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public TourRoutePoint orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Tour getTour() {
        return tour;
    }

    public TourRoutePoint tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourRoutePoint)) return false;

        TourRoutePoint that = (TourRoutePoint) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getLat().equals(that.getLat())) return false;
        if (!getLng().equals(that.getLng())) return false;
        return getOrderNumber().equals(that.getOrderNumber());

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getLat().hashCode();
        result = 31 * result + getLng().hashCode();
        result = 31 * result + getOrderNumber().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TourRoutePoint{" +
            "id=" + id +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
