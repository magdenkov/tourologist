package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the TourImage entity.
 */
public class TourImageDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime uploaded;

    private String s3Key;

    private String s3Bucket;

    private String s3ThumbKey;

    private String s3Region;

    private String type;

    private Boolean master;


    private Long tourId;
    

    private String tourName;

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
    public ZonedDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
    }
    public String gets3Key() {
        return s3Key;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }
    public String gets3Bucket() {
        return s3Bucket;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }
    public String gets3ThumbKey() {
        return s3ThumbKey;
    }

    public void sets3ThumbKey(String s3ThumbKey) {
        this.s3ThumbKey = s3ThumbKey;
    }
    public String gets3Region() {
        return s3Region;
    }

    public void sets3Region(String s3Region) {
        this.s3Region = s3Region;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
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

        TourImageDTO tourImageDTO = (TourImageDTO) o;

        if ( ! Objects.equals(id, tourImageDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourImageDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", uploaded='" + uploaded + "'" +
            ", s3Key='" + s3Key + "'" +
            ", s3Bucket='" + s3Bucket + "'" +
            ", s3ThumbKey='" + s3ThumbKey + "'" +
            ", s3Region='" + s3Region + "'" +
            ", type='" + type + "'" +
            ", master='" + master + "'" +
            '}';
    }
}
