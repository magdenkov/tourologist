package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Bubbl;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourBubbl;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the TourBubbl entity.
 */
@SuppressWarnings("unused")
public interface TourBubblRepository extends JpaRepository<TourBubbl,Long> {

    Set<TourBubbl> findByBubbl(Bubbl bubbl);

    Set<TourBubbl> findByTour(Tour tour);

    @Modifying
    void deleteByTour(Tour tour);
}
