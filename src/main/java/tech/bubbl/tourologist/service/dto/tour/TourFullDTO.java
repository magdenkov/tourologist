package tech.bubbl.tourologist.service.dto.tour;

import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourRoutePoint;
import tech.bubbl.tourologist.service.dto.TourImageDTO;
import tech.bubbl.tourologist.service.dto.bubbl.FullTourBubblNumberedDTO;
import tech.bubbl.tourologist.service.mapper.TourImageMapper;
import tech.bubbl.tourologist.service.mapper.TourRoutePointMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class TourFullDTO extends GetAllToursDTO {

    private List<TourImageDTO> images = new ArrayList<>();

    private List<RoutePointDTO> tourRoutePoints =  new ArrayList<>();

    private List<FullTourBubblNumberedDTO> bubbls =  new ArrayList<>();

    private List<Interest> interests = new ArrayList<>();

    private String description;

    public TourFullDTO(Tour tour, TourImageMapper tourImageMapper, TourRoutePointMapper tourRoutePointMapper) {
        super(tour);
//        this.tourRoutePoints = tourRoutePointMapper.tourRoutePointsToTourRoutePointDTOs(new ArrayList<>(tour.getTourRoutePoints()));
//        Collections.sort(this.tourRoutePoints, (o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber());
        this.tourRoutePoints = tour.getTourRoutePoints().stream()
            .map(RoutePointDTO::new)
            .sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
            .collect(Collectors.toList());
        this.interests = new ArrayList<>(tour.getInterests());
        this.images = tourImageMapper.tourImagesToTourImageDTOs(new ArrayList<>(tour.getTourImages()));
        this.bubbls = tour.getTourBubbls().stream().
            filter(tourBubbl -> (tourBubbl.getBubbl() != null && tourBubbl.getOrderNumber() != null)).
            map(tourBubbl -> new FullTourBubblNumberedDTO(tourBubbl.getBubbl(), tourBubbl.getOrderNumber(), tour.getId())).
            sorted((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber())
            .collect(Collectors.toList());
        setDescription(tour.getDescription());

    }

    @Override
    public String toString() {
        return "TourFullDTO{" +
            "images=" + images +
            ", tourRoutePoints=" + tourRoutePoints +
            ", bubbls=" + bubbls +
            ", interests=" + interests +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourFullDTO)) return false;
        if (!super.equals(o)) return false;

        TourFullDTO that = (TourFullDTO) o;

        if (getImages() != null ? !getImages().equals(that.getImages()) : that.getImages() != null) return false;
        if (getTourRoutePoints() != null ? !getTourRoutePoints().equals(that.getTourRoutePoints()) : that.getTourRoutePoints() != null)
            return false;
        if (getBubbls() != null ? !getBubbls().equals(that.getBubbls()) : that.getBubbls() != null) return false;
        return getInterests() != null ? getInterests().equals(that.getInterests()) : that.getInterests() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getImages() != null ? getImages().hashCode() : 0);
        result = 31 * result + (getTourRoutePoints() != null ? getTourRoutePoints().hashCode() : 0);
        result = 31 * result + (getBubbls() != null ? getBubbls().hashCode() : 0);
        result = 31 * result + (getInterests() != null ? getInterests().hashCode() : 0);
        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TourImageDTO> getImages() {
        return images;
    }

    public void setImages(List<TourImageDTO> images) {
        this.images = images;
    }

    public List<RoutePointDTO> getTourRoutePoints() {
        return tourRoutePoints;
    }

    public void setTourRoutePoints(List<RoutePointDTO> tourRoutePoints) {
        this.tourRoutePoints = tourRoutePoints;
    }

    public List<FullTourBubblNumberedDTO> getBubbls() {
        return bubbls;
    }

    public void setBubbls(List<FullTourBubblNumberedDTO> bubbls) {
        this.bubbls = bubbls;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }
}
