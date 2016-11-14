package tech.bubbl.tourologist.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import tech.bubbl.tourologist.domain.enumeration.PayloadType;

/**
 * A DTO for the Payload entity.
 */
public class PayloadDTO implements Serializable {

    private Long id;

    private PayloadType payloadType;

    @Size(max = 2083)
    private String url;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModified;

    private ZonedDateTime deleted;


    private Long bubblId;
    

    private String bubblName;

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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }
    public ZonedDateTime getDeleted() {
        return deleted;
    }

    public void setDeleted(ZonedDateTime deleted) {
        this.deleted = deleted;
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

        PayloadDTO payloadDTO = (PayloadDTO) o;

        if ( ! Objects.equals(id, payloadDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PayloadDTO{" +
            "id=" + id +
            ", payloadType='" + payloadType + "'" +
            ", url='" + url + "'" +
            ", createdDate='" + createdDate + "'" +
            ", lastModified='" + lastModified + "'" +
            ", deleted='" + deleted + "'" +
            '}';
    }
}
