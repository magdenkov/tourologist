package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TourRating entity.
 */
public class TourRatingDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer rate;

    @Size(max = 255)
    private String feedback;

    private ZonedDateTime time;


    private Long userId;
    
    private Long tourId;
    

    private String tourName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
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

        TourRatingDTO tourRatingDTO = (TourRatingDTO) o;

        if ( ! Objects.equals(id, tourRatingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourRatingDTO{" +
            "id=" + id +
            ", rate='" + rate + "'" +
            ", feedback='" + feedback + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
