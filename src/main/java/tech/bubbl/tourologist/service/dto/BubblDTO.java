package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Bubbl entity.
 */
public class BubblDTO implements Serializable {

    private Long id;

    private Boolean approved;

    private Double lat;

    private Double lng;

    @Min(value = 0)
    private Integer radiusMeters;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModified;

    private ZonedDateTime deleted;

    @Min(value = 0)
    private Integer orderNumber;


    private Long userId;
    
    private Long tourId;
    

    private String tourName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
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
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }
    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }


    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
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
