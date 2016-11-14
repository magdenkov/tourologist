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

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Min(value = 0)
    @Column(name = "radius_meters")
    private Integer radiusMeters;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @Column(name = "deleted")
    private ZonedDateTime deleted;

    @Min(value = 0)
    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tour tour;

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Payload> payloads = new HashSet<>();

    @OneToMany(mappedBy = "bubbl")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BubblImage> bubblImages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isApproved() {
        return approved;
    }

    public Bubbl approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public Bubbl orderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public Tour getTour() {
        return tour;
    }

    public Bubbl tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
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

    public Set<BubblImage> getBubblImages() {
        return bubblImages;
    }

    public Bubbl bubblImages(Set<BubblImage> bubblImages) {
        this.bubblImages = bubblImages;
        return this;
    }

    public Bubbl addBubblImage(BubblImage bubblImage) {
        bubblImages.add(bubblImage);
        bubblImage.setBubbl(this);
        return this;
    }

    public Bubbl removeBubblImage(BubblImage bubblImage) {
        bubblImages.remove(bubblImage);
        bubblImage.setBubbl(null);
        return this;
    }

    public void setBubblImages(Set<BubblImage> bubblImages) {
        this.bubblImages = bubblImages;
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
            ", approved='" + approved + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", radiusMeters='" + radiusMeters + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
