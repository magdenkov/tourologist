package tech.bubbl.tourologist.service.dto.payload;

import tech.bubbl.tourologist.domain.Payload;
import tech.bubbl.tourologist.domain.enumeration.PayloadType;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A custom DTO for the Payload entity.
 */
public class PayloadBubblDTO implements Serializable {

    private Long id;

    private PayloadType payloadType;

    private String name;

    @Size(max = 2083)
    private String url;

    @Size(max = 2083)
    private String thumbUrl;

    private String mimeType;


    public PayloadBubblDTO(Payload payload) {
        setId(payload.getId());
        setName(payload.getName());
        setUrl(payload.getUrl());
        setThumbUrl(payload.getThumbUrl());
        setMimeType(payload.getMimeType());
        setPayloadType(payload.getPayloadType());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public PayloadType getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(PayloadType payloadType) {
        this.payloadType = payloadType;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }



    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PayloadBubblDTO{" +
            "id=" + id +
            ", payloadType='" + payloadType + "'" +
            ", name='" + name + "'" +
            ", url='" + url + "'" +
            ", thumbUrl='" + thumbUrl + "'" +
            ", mimeType='" + mimeType + "'" +

            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayloadBubblDTO)) return false;

        PayloadBubblDTO that = (PayloadBubblDTO) o;

        if (!getId().equals(that.getId())) return false;
        if (getPayloadType() != that.getPayloadType()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) return false;
        if (getThumbUrl() != null ? !getThumbUrl().equals(that.getThumbUrl()) : that.getThumbUrl() != null)
            return false;
        return getMimeType() != null ? getMimeType().equals(that.getMimeType()) : that.getMimeType() == null;

    }
}
