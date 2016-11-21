package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @Size(max = 2083)
    @Column(name = "url", length = 2083)
    private String url;

    @Size(max = 2083)
    @Column(name = "thumb_url", length = 2083)
    private String thumbUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "master")
    private Boolean master;

    @Column(name = "uploaded")
    private ZonedDateTime uploaded;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @Column(name = "deleted")
    private ZonedDateTime deleted;

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

    public String getUrl() {
        return url;
    }

    public TourImage url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public TourImage thumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public TourImage mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public TourImage lastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public TourImage deleted(ZonedDateTime deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
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
            ", url='" + url + "'" +
            ", thumbUrl='" + thumbUrl + "'" +
            ", mimeType='" + mimeType + "'" +
            ", master='" + master + "'" +
            ", uploaded='" + uploaded + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            '}';
    }
}
