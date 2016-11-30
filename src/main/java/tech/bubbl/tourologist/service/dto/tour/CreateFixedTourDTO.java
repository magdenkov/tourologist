package tech.bubbl.tourologist.service.dto.tour;

import tech.bubbl.tourologist.domain.Interest;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.User;
import tech.bubbl.tourologist.domain.enumeration.Status;
import tech.bubbl.tourologist.domain.enumeration.TourType;
import tech.bubbl.tourologist.service.dto.InterestDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A DTO for the Tour entity.
 */
public class CreateFixedTourDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 4096)
    private String description;

//    private Status status;
//
//    private TourType tourType;

    private Double price;

    private List<CreateTourBubblDTO> bubbls =  new ArrayList<>();

    private Set<InterestDTO> interests = new HashSet<>();

    public Tour createTour(User user) {
        Tour tour = new Tour();
        tour.setName(name);
        tour.setTourType(TourType.FIXED);
        tour.setStatus(Status.DRAFT);
        tour.setPrice(price);
        tour.setDescription(description);
        tour.setCreatedDate(ZonedDateTime.now());
        tour.setLastModified(ZonedDateTime.now());
        tour.setUser(user);
        tour.setInterests(interests.stream().map(interestDTO -> {
            Interest interest =  new Interest();
            interest.setId(interestDTO.getId());
            return interest;
        }).collect(Collectors.toSet()));
        return tour;
    }


    public List<CreateTourBubblDTO> getBubbls() {
        return bubbls;
    }

    public void setBubbls(List<CreateTourBubblDTO> bubbls) {
        this.bubbls = bubbls;
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
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//    public TourType getTourType() {
//        return tourType;
//    }
//
//    public void setTourType(TourType tourType) {
//        this.tourType = tourType;
//    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Set<InterestDTO> getInterests() {
        return interests;
    }

    public void setInterests(Set<InterestDTO> interests) {
        this.interests = interests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CreateFixedTourDTO tourDTO = (CreateFixedTourDTO) o;

        if ( ! Objects.equals(id, tourDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TourDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
//            ", status='" + status + "'" +
//            ", tourType='" + tourType + "'" +
            ", price='" + price + "'" +
            '}';
    }
}
