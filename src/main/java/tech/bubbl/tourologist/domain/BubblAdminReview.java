package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BubblAdminReview.
 */
@Entity
@Table(name = "bubbl_admin_review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BubblAdminReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    private Bubbl bubbl;

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

    public BubblAdminReview reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean isApproved() {
        return approved;
    }

    public BubblAdminReview approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public BubblAdminReview time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public BubblAdminReview bubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
        return this;
    }

    public void setBubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
    }

    public User getUser() {
        return user;
    }

    public BubblAdminReview user(User user) {
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
        BubblAdminReview bubblAdminReview = (BubblAdminReview) o;
        if(bubblAdminReview.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bubblAdminReview.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblAdminReview{" +
            "id=" + id +
            ", reason='" + reason + "'" +
            ", approved='" + approved + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
