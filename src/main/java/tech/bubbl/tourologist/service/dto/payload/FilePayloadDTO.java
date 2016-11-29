package tech.bubbl.tourologist.service.dto.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.bubbl.tourologist.domain.enumeration.PayloadType;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Payload entity.
 */
public class FilePayloadDTO implements Serializable {

    private Long id;

    private PayloadType payloadType;

    private String name;

    @Size(max = 2083)
    private String url;

    @Size(max = 2083)
    private String thumbUrl;

    private String mimeType;

    @JsonIgnore
    private byte[] fileBytes;

//    private ZonedDateTime createdDate;
//
//    private ZonedDateTime lastModified;
//
//    private ZonedDateTime deleted;


    private Long bubblId;

    private String bubblName;


    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
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
//    public ZonedDateTime getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(ZonedDateTime createdDate) {
//        this.createdDate = createdDate;
//    }
//    public ZonedDateTime getLastModified() {
//        return lastModified;
//    }
//
//    public void setLastModified(ZonedDateTime lastModified) {
//        this.lastModified = lastModified;
//    }
//    public ZonedDateTime getDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(ZonedDateTime deleted) {
//        this.deleted = deleted;
//    }

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

        FilePayloadDTO payloadDTO = (FilePayloadDTO) o;

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
            ", name='" + name + "'" +
            ", url='" + url + "'" +
            ", thumbUrl='" + thumbUrl + "'" +
            ", mimeType='" + mimeType + "'" +
//            ", createdDate='" + createdDate + "'" +
//            ", lastModified='" + lastModified + "'" +
//            ", deleted='" + deleted + "'" +
            '}';
    }
}
