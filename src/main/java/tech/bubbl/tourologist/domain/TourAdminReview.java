package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TourAdminReview.
 */
@Entity
@Table(name = "tour_admin_review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourAdminReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 4096)
    @Column(name = "reason", length = 4096)
    private String reason;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    private Tour tour;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public TourAdminReview reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean isApproved() {
        return approved;
    }

    public TourAdminReview approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public TourAdminReview time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Tour getTour() {
        return tour;
    }

    public TourAdminReview tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getUser() {
        return user;
    }

    public TourAdminReview user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TourAdminReview tourAdminReview = (TourAdminReview) o;
        if(tourAdminReview.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourAdminReview.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourAdminReview{" +
            "id=" + id +
            ", reason='" + reason + "'" +
            ", approved='" + approved + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
