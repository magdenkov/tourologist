package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourBubbl;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourBubbl entity.
 */
@SuppressWarnings("unused")
public interface TourBubblRepository extends JpaRepository<TourBubbl,Long> {

}
