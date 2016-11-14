package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TourImage.
 */
@Entity
@Table(name = "tour_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TourImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "uploaded")
    private ZonedDateTime uploaded;

    @Column(name = "s_3_key")
    private String s3Key;

    @Column(name = "s_3_bucket")
    private String s3Bucket;

    @Column(name = "s_3_thumb_key")
    private String s3ThumbKey;

    @Column(name = "s_3_region")
    private String s3Region;

    @Column(name = "type")
    private String type;

    @Column(name = "master")
    private Boolean master;

    @ManyToOne
    private Tour tour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TourImage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getUploaded() {
        return uploaded;
    }

    public TourImage uploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
        return this;
    }

    public void setUploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public String gets3Key() {
        return s3Key;
    }

    public TourImage s3Key(String s3Key) {
        this.s3Key = s3Key;
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public TourImage s3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3ThumbKey() {
        return s3ThumbKey;
    }

    public TourImage s3ThumbKey(String s3ThumbKey) {
        this.s3ThumbKey = s3ThumbKey;
        return this;
    }

    public void sets3ThumbKey(String s3ThumbKey) {
        this.s3ThumbKey = s3ThumbKey;
    }

    public String gets3Region() {
        return s3Region;
    }

    public TourImage s3Region(String s3Region) {
        this.s3Region = s3Region;
        return this;
    }

    public void sets3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String getType() {
        return type;
    }

    public TourImage type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isMaster() {
        return master;
    }

    public TourImage master(Boolean master) {
        this.master = master;
        return this;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Tour getTour() {
        return tour;
    }

    public TourImage tour(Tour tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TourImage tourImage = (TourImage) o;
        if(tourImage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tourImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourImage{" +
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
