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
 * A Interest.
 */
@Entity
@Table(name = "interest")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "icon", length = 255)
    private String icon;

    @ManyToMany(mappedBy = "interests")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tour> tours = new HashSet<>();

    @ManyToMany(mappedBy = "interests")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bubbl> bubbls = new HashSet<>();

    @ManyToMany(mappedBy = "interests")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Interest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public Interest icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public Interest tours(Set<Tour> tours) {
        this.tours = tours;
        return this;
    }

    public Interest addTour(Tour tour) {
        tours.add(tour);
        tour.getInterests().add(this);
        return this;
    }

    public Interest removeTour(Tour tour) {
        tours.remove(tour);
        tour.getInterests().remove(this);
        return this;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }

    public Set<Bubbl> getBubbls() {
        return bubbls;
    }

    public Interest bubbls(Set<Bubbl> bubbls) {
        this.bubbls = bubbls;
        return this;
    }

    public Interest addBubbl(Bubbl bubbl) {
        bubbls.add(bubbl);
        bubbl.getInterests().add(this);
        return this;
    }

    public Interest removeBubbl(Bubbl bubbl) {
        bubbls.remove(bubbl);
        bubbl.getInterests().remove(this);
        return this;
    }

    public void setBubbls(Set<Bubbl> bubbls) {
        this.bubbls = bubbls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Interest interest = (Interest) o;
        if(interest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, interest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Interest{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", icon='" + icon + "'" +
            '}';
    }
}
