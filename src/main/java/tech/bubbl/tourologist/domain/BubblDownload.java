package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BubblDownload.
 */
@Entity
@Table(name = "bubbl_download")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BubblDownload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public ZonedDateTime getTime() {
        return time;
    }

    public BubblDownload time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public BubblDownload user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public BubblDownload bubbl(Bubbl bubbl) {
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
        BubblDownload bubblDownload = (BubblDownload) o;
        if(bubblDownload.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bubblDownload.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblDownload{" +
            "id=" + id +
            ", time='" + time + "'" +
            '}';
    }
}
