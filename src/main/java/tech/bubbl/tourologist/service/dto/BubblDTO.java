package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tech.bubbl.tourologist.domain.enumeration.Status;

/**
 * A DTO for the Bubbl entity.
 */
public class BubblDTO implements Serializable {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Status status;

    private Double lat;

    private Double lng;

    @Min(value = 0)
    private Integer radiusMeters;

//    private ZonedDateTime createdDate;
//
//    private ZonedDateTime lastModified;
//
//    private ZonedDateTime deleted;


    private Long userId;


    private String userEmail;

    private Set<InterestDTO> interests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
    public Integer getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Integer radiusMeters) {
        this.radiusMeters = radiusMeters;
    }
//    public ZonedDateTime getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(ZonedDateTime createdDate) {
//        this.createdDate = createdDate;
//    }
//    public ZonedDateTime getLastModified() {
//        return lastModified;
//    }
//
//    public void setLastModified(ZonedDateTime lastModified) {
//        this.lastModified = lastModified;
//    }
//    public ZonedDateTime getDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(ZonedDateTime deleted) {
//        this.deleted = deleted;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Set<InterestDTO> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestDTO> interests) {
        this.interests = interests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BubblDTO bubblDTO = (BubblDTO) o;

        if ( ! Objects.equals(id, bubblDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", radiusMeters='" + radiusMeters + "'" +
            '}';
    }
}
