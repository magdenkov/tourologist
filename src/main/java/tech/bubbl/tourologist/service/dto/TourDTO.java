package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tech.bubbl.tourologist.domain.enumeration.TourType;

/**
 * A DTO for the Tour entity.
 */
public class TourDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 4096)
    private String description;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModified;

    private ZonedDateTime deleted;

    private TourType tourType;

    private Boolean approved;


    private Long userId;
    
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
    public TourType getTourType() {
        return tourType;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

        TourDTO tourDTO = (TourDTO) o;

        if ( ! Objects.equals(id, tourDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourDTO{" +
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
