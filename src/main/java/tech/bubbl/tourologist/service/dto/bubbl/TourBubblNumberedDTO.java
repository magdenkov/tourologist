package tech.bubbl.tourologist.service.dto.bubbl;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.service.dto.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Optional;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class TourBubblNumberedDTO implements TransportObject{


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

    private Long tourId;


    private Double rating;

    private Integer ratingsAmount;

    private Integer downloadsAmount;

    private Boolean isDownloaded;

    private Double distanceToBubbl;

    public TourBubblNumberedDTO() {
    }

    public TourBubblNumberedDTO(Bubbl bubbl, Integer orderNumber, Long tourId) {
        this.id = bubbl.getId();
        setName(bubbl.getName());
        setStatus(bubbl.getStatus());
        setLat(bubbl.getLat());
        setLng(bubbl.getLng());
        setDescription(bubbl.getDescription());
        setRadiusMeters(bubbl.getRadiusMeters());
        setOrderNumber(orderNumber);
        setTourId(tourId);

        setRating(bubbl.getAverageRating());
        setRatingsAmount(bubbl.getTotalRatings());
        setDownloadsAmount(bubbl.getTotalDownloads());
        setDistanceToBubbl(bubbl.getDistanceToBubbl());

        setDownloaded(false);

        Optional.ofNullable(bubbl.getUser()).ifPresent(user -> this.setAuthor(user.getFullName()));
    }

    public Double getDistanceToBubbl() {
        return distanceToBubbl;
    }

    public void setDistanceToBubbl(Double distanceToBubbl) {
        this.distanceToBubbl = distanceToBubbl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingsAmount() {
        return ratingsAmount;
    }

    public void setRatingsAmount(Integer ratingsAmount) {
        this.ratingsAmount = ratingsAmount;
    }

    public Integer getDownloadsAmount() {
        return downloadsAmount;
    }

    public void setDownloadsAmount(Integer downloadsAmount) {
        this.downloadsAmount = downloadsAmount;
    }

    public Boolean getDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        isDownloaded = downloaded;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
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
