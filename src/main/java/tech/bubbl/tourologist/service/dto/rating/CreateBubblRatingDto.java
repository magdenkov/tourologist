package tech.bubbl.tourologist.service.dto.rating;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


public class CreateBubblRatingDto implements Serializable {


    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rate;

    @Size(max = 4096)
    private String feedback;

    @JsonIgnore
    private Long bubblId;


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

    public Long getBubblId() {
        return bubblId;
    }

    public void setBubblId(Long bubblId) {
        this.bubblId = bubblId;
    }

    @Override
    public String toString() {
        return "CreateTourRatingCTO{" +
            "rate=" + rate +
            ", feedback='" + feedback + '\'' +
            ", tourId=" + bubblId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateBubblRatingDto)) return false;

        CreateBubblRatingDto that = (CreateBubblRatingDto) o;

        if (!getRate().equals(that.getRate())) return false;
        if (getFeedback() != null ? !getFeedback().equals(that.getFeedback()) : that.getFeedback() != null)
            return false;
        return getBubblId() != null ? getBubblId().equals(that.getBubblId()) : that.getBubblId() == null;

    }

    @Override
    public int hashCode() {
        int result = getRate().hashCode();
        result = 31 * result + (getFeedback() != null ? getFeedback().hashCode() : 0);
        result = 31 * result + (getBubblId() != null ? getBubblId().hashCode() : 0);
        return result;
    }
}
