package tech.bubbl.tourologist.service.dto.tour;

import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.dto.TransportObject;

import java.util.Optional;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class GetAllToursDTO implements TransportObject {

    private Long id;

    private String name;

    private String author;

    private Integer bubblsAmount;

    private TourType tourType;

    private Double price;

    private Status status;

    private Double rating;

    private Integer ratingsAmount;

    private Integer downloadsAmount;

    private Integer routeLength;

    private Integer distanceToRouteStart;

    private Double lat;

    private Double lng;

    private Boolean isDownloaded;

    public GetAllToursDTO() {
    }

    public GetAllToursDTO(Tour tour) {
        setBubblsAmount(tour.getBubblsAmount());
        Optional.ofNullable(tour.getUser()).ifPresent(user -> setAuthor(user.getFullName()));
        setName(tour.getName());
        setId(tour.getId());
        setTourType(tour.getTourType());
        setStatus(tour.getStatus());
        setPrice(tour.getPrice());
        setDownloadsAmount(tour.getTotalDownloads());
        setRating(tour.getAverageRating());
        setIsDownloaded(tour.isDownloaded());
        setRouteLength(tour.getRouteLength());
        setLat(tour.getLat());
        setLng(tour.getLng());
        setRatingsAmount(tour.getTotalRatings());
    }


    public Integer getRatingsAmount() {
        return ratingsAmount;
    }

    public void setRatingsAmount(Integer ratingsAmount) {
        this.ratingsAmount = ratingsAmount;
    }

    public Boolean getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(Boolean downloaded) {
        isDownloaded = downloaded;
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

    public Integer getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(Integer routeLength) {
        this.routeLength = routeLength;
    }

    public Integer getDistanceToRouteStart() {
        return distanceToRouteStart;
    }

    public void setDistanceToRouteStart(Integer distanceToRouteStart) {
        this.distanceToRouteStart = distanceToRouteStart;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getDownloadsAmount() {
        return downloadsAmount;
    }

    public void setDownloadsAmount(Integer downloadsAmount) {
        this.downloadsAmount = downloadsAmount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TourType getTourType() {
        return tourType;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getBubblsAmount() {
        return bubblsAmount;
    }

    public void setBubblsAmount(Integer bubblsAmount) {
        this.bubblsAmount = bubblsAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetAllToursDTO)) return false;

        GetAllToursDTO that = (GetAllToursDTO) o;

        if (!getName().equals(that.getName())) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return bubblsAmount.equals(that.bubblsAmount);

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + bubblsAmount.hashCode();
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
