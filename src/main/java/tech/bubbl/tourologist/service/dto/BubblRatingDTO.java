package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the BubblRating entity.
 */
public class BubblRatingDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rate;

    @Size(max = 4096)
    private String feedback;

    private ZonedDateTime time;


    private Long userId;
    

    private String userEmail;

    private Long bubblId;
    

    private String bubblName;

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


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getBubblId() {
        return bubblId;
    }

    public void setBubblId(Long bubblId) {
        this.bubblId = bubblId;
    }


    public String getBubblName() {
        return bubblName;
    }

    public void setBubblName(String bubblName) {
        this.bubblName = bubblName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BubblRatingDTO bubblRatingDTO = (BubblRatingDTO) o;

        if ( ! Objects.equals(id, bubblRatingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblRatingDTO{" +
            "id=" + id +
            ", rate='" + rate + "'" +
            ", feedback='" + feedback + "'" +
            ", time='" + time + "'" +
            '}';
    }
}
