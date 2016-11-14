package tech.bubbl.tourologist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tech.bubbl.tourologist.domain.enumeration.TourType;

/**
 * A Tour.
 */
@Entity
@Table(name = "tour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 4096)
    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @Column(name = "deleted")
    private ZonedDateTime deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_type")
    private TourType tourType;

    @Column(name = "approved")
    private Boolean approved;

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tour_interest",
               joinColumns = @JoinColumn(name="tours_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="interests_id", referencedColumnName="ID"))
    private Set<Interest> interests = new HashSet<>();

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bubbl> bubbls = new HashSet<>();

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourImage> tourImages = new HashSet<>();

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourRoutePoint> tourRoutePoints = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tour name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Tour description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Tour createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public Tour lastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public Tour deleted(ZonedDateTime deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
    }

    public TourType getTourType() {
        return tourType;
    }

    public Tour tourType(TourType tourType) {
        this.tourType = tourType;
        return this;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }

    public Boolean isApproved() {
        return approved;
    }

    public Tour approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public User getUser() {
        return user;
    }

    public Tour user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public Tour interests(Set<Interest> interests) {
        this.interests = interests;
        return this;
    }

    public Tour addInterest(Interest interest) {
        interests.add(interest);
        interest.getTours().add(this);
        return this;
    }

    public Tour removeInterest(Interest interest) {
        interests.remove(interest);
        interest.getTours().remove(this);
        return this;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<Bubbl> getBubbls() {
        return bubbls;
    }

    public Tour bubbls(Set<Bubbl> bubbls) {
        this.bubbls = bubbls;
        return this;
    }

    public Tour addBubbl(Bubbl bubbl) {
        bubbls.add(bubbl);
        bubbl.setTour(this);
        return this;
    }

    public Tour removeBubbl(Bubbl bubbl) {
        bubbls.remove(bubbl);
        bubbl.setTour(null);
        return this;
    }

    public void setBubbls(Set<Bubbl> bubbls) {
        this.bubbls = bubbls;
    }

    public Set<TourImage> getTourImages() {
        return tourImages;
    }

    public Tour tourImages(Set<TourImage> tourImages) {
        this.tourImages = tourImages;
        return this;
    }

    public Tour addTourImage(TourImage tourImage) {
        tourImages.add(tourImage);
        tourImage.setTour(this);
        return this;
    }

    public Tour removeTourImage(TourImage tourImage) {
        tourImages.remove(tourImage);
        tourImage.setTour(null);
        return this;
    }

    public void setTourImages(Set<TourImage> tourImages) {
        this.tourImages = tourImages;
    }

    public Set<TourRoutePoint> getTourRoutePoints() {
        return tourRoutePoints;
    }

    public Tour tourRoutePoints(Set<TourRoutePoint> tourRoutePoints) {
        this.tourRoutePoints = tourRoutePoints;
        return this;
    }

    public Tour addTourRoutePoint(TourRoutePoint tourRoutePoint) {
        tourRoutePoints.add(tourRoutePoint);
        tourRoutePoint.setTour(this);
        return this;
    }

    public Tour removeTourRoutePoint(TourRoutePoint tourRoutePoint) {
        tourRoutePoints.remove(tourRoutePoint);
        tourRoutePoint.setTour(null);
        return this;
    }

    public void setTourRoutePoints(Set<TourRoutePoint> tourRoutePoints) {
        this.tourRoutePoints = tourRoutePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tour tour = (Tour) o;
        if(tour.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tour{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            ", tourType='" + tourType + "'" +
            ", approved='" + approved + "'" +
            '}';
    }
}
