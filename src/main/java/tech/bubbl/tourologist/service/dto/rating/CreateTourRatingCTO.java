package tech.bubbl.tourologist.service.dto.rating;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


public class CreateTourRatingCTO implements Serializable {


    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rate;

    @Size(max = 4096)
    private String feedback;

    @JsonIgnore
    private Long tourId;


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

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    @Override
    public String toString() {
        return "CreateTourRatingCTO{" +
            "rate=" + rate +
            ", feedback='" + feedback + '\'' +
            ", tourId=" + tourId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateTourRatingCTO)) return false;

        CreateTourRatingCTO that = (CreateTourRatingCTO) o;

        if (!getRate().equals(that.getRate())) return false;
        if (getFeedback() != null ? !getFeedback().equals(that.getFeedback()) : that.getFeedback() != null)
            return false;
        return getTourId() != null ? getTourId().equals(that.getTourId()) : that.getTourId() == null;

    }

    @Override
    public int hashCode() {
        int result = getRate().hashCode();
        result = 31 * result + (getFeedback() != null ? getFeedback().hashCode() : 0);
        result = 31 * result + (getTourId() != null ? getTourId().hashCode() : 0);
        return result;
    }
}
