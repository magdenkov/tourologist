package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BubblImage.
 */
@Entity
@Table(name = "bubbl_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BubblImage implements Serializable {

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
    private Bubbl bubbl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BubblImage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getUploaded() {
        return uploaded;
    }

    public BubblImage uploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
        return this;
    }

    public void setUploaded(ZonedDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public String gets3Key() {
        return s3Key;
    }

    public BubblImage s3Key(String s3Key) {
        this.s3Key = s3Key;
        return this;
    }

    public void sets3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public BubblImage s3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3ThumbKey() {
        return s3ThumbKey;
    }

    public BubblImage s3ThumbKey(String s3ThumbKey) {
        this.s3ThumbKey = s3ThumbKey;
        return this;
    }

    public void sets3ThumbKey(String s3ThumbKey) {
        this.s3ThumbKey = s3ThumbKey;
    }

    public String gets3Region() {
        return s3Region;
    }

    public BubblImage s3Region(String s3Region) {
        this.s3Region = s3Region;
        return this;
    }

    public void sets3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String getType() {
        return type;
    }

    public BubblImage type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isMaster() {
        return master;
    }

    public BubblImage master(Boolean master) {
        this.master = master;
        return this;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public BubblImage bubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
        return this;
    }

    public void setBubbl(Bubbl bubbl) {
        this.bubbl = bubbl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BubblImage bubblImage = (BubblImage) o;
        if(bubblImage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bubblImage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BubblImage{" +
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
