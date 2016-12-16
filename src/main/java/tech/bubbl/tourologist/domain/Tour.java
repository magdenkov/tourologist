package tech.bubbl.tourologist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import tech.bubbl.tourologist.config.JHipsterProperties;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.security.SecurityUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Tour.
 */
@Entity
@Table(name = "tour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Spatial
//@Indexed
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_type")
    private TourType tourType;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_date")
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Column(name = "last_modified")
    private ZonedDateTime lastModified = ZonedDateTime.now();

    @Column(name = "deleted")
    private ZonedDateTime deleted;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tour_interest",
               joinColumns = @JoinColumn(name="tours_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="interests_id", referencedColumnName="ID"))
    private Set<Interest> interests = new HashSet<>();

    @OneToMany(mappedBy = "tour",  orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourRating> tourRatings = new HashSet<>();

    @OneToMany(mappedBy = "tour", orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourDownload> tourDownloads = new HashSet<>();

    @OneToMany(mappedBy = "tour", orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourImage> tourImages = new HashSet<>();

    @OneToMany(mappedBy = "tour", orphanRemoval = true)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourAdminReview> tourAdminReviews = new HashSet<>();

    @OneToMany(mappedBy = "tour", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourRoutePoint> tourRoutePoints = new HashSet<>();

    @OneToMany(mappedBy = "tour") // put
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TourBubbl> tourBubbls = new HashSet<>();

    @Column(name = "lat")
//    @Latitude
    private Double lat;

    @Column(name = "lng")
//    @Longitude
    private Double lng;

    @Column(name = "route_length")
    private Integer routeLength;

    @Formula("(select count(*) from tour_bubbl tb where tb.tour_id = id)")
    private Integer bubblsAmount;

    @Formula("(select count(*) from tour_download td, jhi_user u " +
        "where td.tour_id = id " +
        "and u.id = td.user_id " +
        "and u.login = @userLogin )")
    private Integer downloadsAmountByCurrentUser;


    public Boolean isDownloaded() {
        return this.downloadsAmountByCurrentUser > 0;
    }

    public Integer getBubblsAmount() {
        return bubblsAmount;
    }

    public void setBubblsAmount(Integer bubblsAmount) {
        this.bubblsAmount = bubblsAmount;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(Integer routeLength) {
        this.routeLength = routeLength;
    }

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

    public Status getStatus() {
        return status;
    }

    public Tour status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public Double getPrice() {
        return price;
    }

    public Tour price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Set<TourRating> getTourRatings() {
        return tourRatings;
    }

    public Tour tourRatings(Set<TourRating> tourRatings) {
        this.tourRatings = tourRatings;
        return this;
    }

    public Tour addTourRating(TourRating tourRating) {
        tourRatings.add(tourRating);
        tourRating.setTour(this);
        return this;
    }

    public Tour removeTourRating(TourRating tourRating) {
        tourRatings.remove(tourRating);
        tourRating.setTour(null);
        return this;
    }

    public void setTourRatings(Set<TourRating> tourRatings) {
        this.tourRatings = tourRatings;
    }

    public Set<TourDownload> getTourDownloads() {
        return tourDownloads;
    }

    public Tour tourDownloads(Set<TourDownload> tourDownloads) {
        this.tourDownloads = tourDownloads;
        return this;
    }

    public Tour addTourDownload(TourDownload tourDownload) {
        tourDownloads.add(tourDownload);
        tourDownload.setTour(this);
        return this;
    }

    public Tour removeTourDownload(TourDownload tourDownload) {
        tourDownloads.remove(tourDownload);
        tourDownload.setTour(null);
        return this;
    }

    public void setTourDownloads(Set<TourDownload> tourDownloads) {
        this.tourDownloads = tourDownloads;
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

    public Set<TourAdminReview> getTourAdminReviews() {
        return tourAdminReviews;
    }

    public Tour tourAdminReviews(Set<TourAdminReview> tourAdminReviews) {
        this.tourAdminReviews = tourAdminReviews;
        return this;
    }

    public Tour addTourAdminReview(TourAdminReview tourAdminReview) {
        tourAdminReviews.add(tourAdminReview);
        tourAdminReview.setTour(this);
        return this;
    }

    public Tour removeTourAdminReview(TourAdminReview tourAdminReview) {
        tourAdminReviews.remove(tourAdminReview);
        tourAdminReview.setTour(null);
        return this;
    }

    public void setTourAdminReviews(Set<TourAdminReview> tourAdminReviews) {
        this.tourAdminReviews = tourAdminReviews;
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

    public Set<TourBubbl> getTourBubbls() {
        return tourBubbls;
    }

    public Tour tourBubbls(Set<TourBubbl> tourBubbls) {
        this.tourBubbls = tourBubbls;
        return this;
    }

    public Tour addTourBubbl(TourBubbl tourBubbl) {
        tourBubbls.add(tourBubbl);
        tourBubbl.setTour(this);
        return this;
    }

    public Tour removeTourBubbl(TourBubbl tourBubbl) {
        tourBubbls.remove(tourBubbl);
        tourBubbl.setTour(null);
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
            ", status='" + status + "'" +
            ", tourType='" + tourType + "'" +
            ", price='" + price + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            '}';
    }
}
