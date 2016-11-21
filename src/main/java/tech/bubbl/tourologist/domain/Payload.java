package tech.bubbl.tourologist.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import tech.bubbl.tourologist.domain.enumeration.PayloadType;

/**
 * A Payload.
 */
@Entity
@Table(name = "payload")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payload_type")
    private PayloadType payloadType;

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

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "last_modified")
    private ZonedDateTime lastModified;

    @Column(name = "deleted")
    private ZonedDateTime deleted;

    @ManyToOne
    private Bubbl bubbl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayloadType getPayloadType() {
        return payloadType;
    }

    public Payload payloadType(PayloadType payloadType) {
        this.payloadType = payloadType;
        return this;
    }

    public void setPayloadType(PayloadType payloadType) {
        this.payloadType = payloadType;
    }

    public String getName() {
        return name;
    }

    public Payload name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Payload url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public Payload thumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Payload mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Payload createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public Payload lastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public Payload deleted(ZonedDateTime deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
    }

    public Bubbl getBubbl() {
        return bubbl;
    }

    public Payload bubbl(Bubbl bubbl) {
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
        Payload payload = (Payload) o;
        if(payload.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, payload.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Payload{" +
            "id=" + id +
            ", payloadType='" + payloadType + "'" +
            ", name='" + name + "'" +
            ", url='" + url + "'" +
            ", thumbUrl='" + thumbUrl + "'" +
            ", mimeType='" + mimeType + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            '}';
    }
}
