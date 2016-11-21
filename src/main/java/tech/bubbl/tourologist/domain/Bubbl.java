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

import tech.bubbl.tourologist.domain.enumeration.Status;

/**
 * A Bubbl.
 */
@Entity
@Table(name = "bubbl")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bubbl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Min(value = 0)
    @Column(name = "radius_meters")
    private Integer radiusMeters;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @Column(name = "deleted")
    private ZonedDateTime deleted;

    @ManyToOne
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "bubbl_interest",
               joinColumns = @JoinColumn(name="bubbls_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="interests_id", referencedColumnName="ID"))
    private Set<Interest> interests = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BubblRating> bubblRatings = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BubblDownload> bubblDownloads = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Payload> payloads = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BubblAdminReview> bubblAdminReviews = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourBubbl> tourBubbls = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Bubbl name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Bubbl description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public Bubbl status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getLat() {
        return lat;
    }

    public Bubbl lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public Bubbl lng(Double lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getRadiusMeters() {
        return radiusMeters;
    }

    public Bubbl radiusMeters(Integer radiusMeters) {
        this.radiusMeters = radiusMeters;
        return this;
    }

    public void setRadiusMeters(Integer radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Bubbl createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public Bubbl lastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public Bubbl deleted(ZonedDateTime deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
    }

    public User getUser() {
        return user;
    }

    public Bubbl user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public Bubbl interests(Set<Interest> interests) {
        this.interests = interests;
        return this;
    }

    public Bubbl addInterest(Interest interest) {
        interests.add(interest);
        interest.getBubbls().add(this);
        return this;
    }

    public Bubbl removeInterest(Interest interest) {
        interests.remove(interest);
        interest.getBubbls().remove(this);
        return this;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<BubblRating> getBubblRatings() {
        return bubblRatings;
    }

    public Bubbl bubblRatings(Set<BubblRating> bubblRatings) {
        this.bubblRatings = bubblRatings;
        return this;
    }

    public Bubbl addBubblRating(BubblRating bubblRating) {
        bubblRatings.add(bubblRating);
        bubblRating.setBubbl(this);
        return this;
    }

    public Bubbl removeBubblRating(BubblRating bubblRating) {
        bubblRatings.remove(bubblRating);
        bubblRating.setBubbl(null);
        return this;
    }

    public void setBubblRatings(Set<BubblRating> bubblRatings) {
        this.bubblRatings = bubblRatings;
    }

    public Set<BubblDownload> getBubblDownloads() {
        return bubblDownloads;
    }

    public Bubbl bubblDownloads(Set<BubblDownload> bubblDownloads) {
        this.bubblDownloads = bubblDownloads;
        return this;
    }

    public Bubbl addBubblDownload(BubblDownload bubblDownload) {
        bubblDownloads.add(bubblDownload);
        bubblDownload.setBubbl(this);
        return this;
    }

    public Bubbl removeBubblDownload(BubblDownload bubblDownload) {
        bubblDownloads.remove(bubblDownload);
        bubblDownload.setBubbl(null);
        return this;
    }

    public void setBubblDownloads(Set<BubblDownload> bubblDownloads) {
        this.bubblDownloads = bubblDownloads;
    }

    public Set<Payload> getPayloads() {
        return payloads;
    }

    public Bubbl payloads(Set<Payload> payloads) {
        this.payloads = payloads;
        return this;
    }

    public Bubbl addPayload(Payload payload) {
        payloads.add(payload);
        payload.setBubbl(this);
        return this;
    }

    public Bubbl removePayload(Payload payload) {
        payloads.remove(payload);
        payload.setBubbl(null);
        return this;
    }

    public void setPayloads(Set<Payload> payloads) {
        this.payloads = payloads;
    }

    public Set<BubblAdminReview> getBubblAdminReviews() {
        return bubblAdminReviews;
    }

    public Bubbl bubblAdminReviews(Set<BubblAdminReview> bubblAdminReviews) {
        this.bubblAdminReviews = bubblAdminReviews;
        return this;
    }

    public Bubbl addBubblAdminReview(BubblAdminReview bubblAdminReview) {
        bubblAdminReviews.add(bubblAdminReview);
        bubblAdminReview.setBubbl(this);
        return this;
    }

    public Bubbl removeBubblAdminReview(BubblAdminReview bubblAdminReview) {
        bubblAdminReviews.remove(bubblAdminReview);
        bubblAdminReview.setBubbl(null);
        return this;
    }

    public void setBubblAdminReviews(Set<BubblAdminReview> bubblAdminReviews) {
        this.bubblAdminReviews = bubblAdminReviews;
    }

    public Set<TourBubbl> getTourBubbls() {
        return tourBubbls;
    }

    public Bubbl tourBubbls(Set<TourBubbl> tourBubbls) {
        this.tourBubbls = tourBubbls;
        return this;
    }

    public Bubbl addTourBubbl(TourBubbl tourBubbl) {
        tourBubbls.add(tourBubbl);
        tourBubbl.setBubbl(this);
        return this;
    }

    public Bubbl removeTourBubbl(TourBubbl tourBubbl) {
        tourBubbls.remove(tourBubbl);
        tourBubbl.setBubbl(null);
        return this;
    }

    public void setTourBubbls(Set<TourBubbl> tourBubbls) {
        this.tourBubbls = tourBubbls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bubbl bubbl = (Bubbl) o;
        if(bubbl.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bubbl.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bubbl{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", radiusMeters='" + radiusMeters + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            '}';
    }
}
