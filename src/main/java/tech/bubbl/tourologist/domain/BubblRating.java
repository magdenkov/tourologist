package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BubblRating.
 */
@Entity
@Table(name = "bubbl_rating")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BubblRating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Size(max = 255)
    @Column(name = "feedback", length = 255)
    private String feedback;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    private User user;

    @ManyToOne
    private Bubbl bubbl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public BubblRating rate(Integer rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getFeedback() {
        return feedback;
    }

    public BubblRating feedback(String feedback) {
        this.feedback = feedback;
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public BubblRating time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public BubblRating user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public BubblRating bubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
        return this;
    }

    public void setBubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BubblRating bubblRating = (BubblRating) o;
        if(bubblRating.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bubblRating.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblRating{" +
            "id=" + id +
            ", rate='" + rate + "'" +
            ", feedback='" + feedback + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
