package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


@Entity
@Table(name = "tour_completed")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourCompleted implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private ZonedDateTime time = ZonedDateTime.now();

    @ManyToOne
    private User user;

    @ManyToOne
    private Tour tour;

    public TourCompleted() {
    }

    public TourCompleted(User user, Tour tour) {
        this.user = user;
        this.tour = tour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public TourCompleted time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public TourCompleted user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tour getTour() {
        return tour;
    }

    public TourCompleted tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TourCompleted tourDownload = (TourCompleted) o;
        if(tourDownload.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourDownload.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourDownload{" +
            "id=" + id +
            ", time='" + time + "'" +
            '}';
    }
}
