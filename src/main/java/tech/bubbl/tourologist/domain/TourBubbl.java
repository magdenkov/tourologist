package tech.bubbl.tourologist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TourBubbl.
 */
@Entity
@Table(name = "tour_bubbl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourBubbl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 0)
    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    private Bubbl bubbl;

    @ManyToOne
    private Tour tour;

    @OneToMany(mappedBy = "tourBubbl", orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourBubblRoutePoint> tourBubbls = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public TourBubbl orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public TourBubbl bubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
        return this;
    }

    public void setBubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
    }

    public Tour getTour() {
        return tour;
    }

    public TourBubbl tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Set<TourBubblRoutePoint> getTourBubbls() {
        return tourBubbls;
    }

    public TourBubbl tourBubbls(Set<TourBubblRoutePoint> tourBubblRoutePoints) {
        this.tourBubbls = tourBubblRoutePoints;
        return this;
    }

    public TourBubbl addTourBubbl(TourBubblRoutePoint tourBubblRoutePoint) {
        tourBubbls.add(tourBubblRoutePoint);
        tourBubblRoutePoint.setTourBubbl(this);
        return this;
    }

    public TourBubbl removeTourBubbl(TourBubblRoutePoint tourBubblRoutePoint) {
        tourBubbls.remove(tourBubblRoutePoint);
        tourBubblRoutePoint.setTourBubbl(null);
        return this;
    }

    public void setTourBubbls(Set<TourBubblRoutePoint> tourBubblRoutePoints) {
        this.tourBubbls = tourBubblRoutePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TourBubbl tourBubbl = (TourBubbl) o;
        if(tourBubbl.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourBubbl.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourBubbl{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
