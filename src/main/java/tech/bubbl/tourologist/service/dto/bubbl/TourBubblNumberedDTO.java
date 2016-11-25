package tech.bubbl.tourologist.service.dto.bubbl;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.domain.enumeration.Status;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class TourBubblNumberedDTO {


    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private Status status;

    private Double lat;

    private Double lng;

    @Min(value = 0)
    private Integer radiusMeters;

    private String author;

    private Integer orderNumber;

    public TourBubblNumberedDTO() {
    }

    public TourBubblNumberedDTO(Bubbl bubbl, Integer orderNumber) {
        this.id = bubbl.getId();
        setName(bubbl.getName());
        setStatus(bubbl.getStatus());
        setLat(bubbl.getLat());
        setLng(bubbl.getLng());
        setDescription(bubbl.getDescription());
        setRadiusMeters(bubbl.getRadiusMeters());
        setOrderNumber(orderNumber);


        Optional.ofNullable(bubbl.getUser()).ifPresent(user -> this.setAuthor(user.getFullName()));
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Integer radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
