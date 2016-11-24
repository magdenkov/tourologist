package tech.bubbl.tourologist.service.dto;

import tech.bubbl.tourologist.domain.Interest;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Interest entity.
 */
public class InterestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String icon;

//    public InterestDTO(Interest interest) {
//        this.name = interest.getName();
//        this.icon = interest.getIcon();
//        this.id = interest.getId();
//    }


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
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InterestDTO interestDTO = (InterestDTO) o;

        if ( ! Objects.equals(id, interestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InterestDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", icon='" + icon + "'" +
            '}';
    }
}
