package tech.bubbl.tourologist.service.dto.tour;

import tech.bubbl.tourologist.domain.Tour;

import java.util.Optional;

/**
 * Created by Denis Magdenkov on 25.11.2016.
 */
public class GetAllToursDTO {

    private String name;

    private String author;

    private Integer bubblsAmount;

    public GetAllToursDTO(Tour tour) {
        setBubblsAmount(tour.getTourBubbls().size());
        Optional.ofNullable(tour.getUser()).ifPresent(user ->{
            setAuthor(user.getFirstName() + " " + user.getLastName());
        });
        setName(tour.getName());
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
