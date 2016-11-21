package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TourRating.
 */
@Entity
@Table(name = "tour_rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Size(max = 4096)
    @Column(name = "feedback", length = 4096)
    private String feedback;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tour tour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public TourRating rate(Integer rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getFeedback() {
        return feedback;
    }

    public TourRating feedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public TourRating time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public TourRating user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tour getTour() {
        return tour;
    }

    public TourRating tour(Tour tour) {
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
        TourRating tourRating = (TourRating) o;
        if(tourRating.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourRating.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourRating{" +
            "id=" + id +
            ", rate='" + rate + "'" +
            ", feedback='" + feedback + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
