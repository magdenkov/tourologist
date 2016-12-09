package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourRoutePoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourRoutePoint entity.
 */
@SuppressWarnings("unused")
public interface TourRoutePointRepository extends JpaRepository<TourRoutePoint,Long> {

    @Modifying
    void deleteByTour(Tour tourFromDto);
}
