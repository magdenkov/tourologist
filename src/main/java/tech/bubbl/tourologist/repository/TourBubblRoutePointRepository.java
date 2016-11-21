package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourBubblRoutePoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourBubblRoutePoint entity.
 */
@SuppressWarnings("unused")
public interface TourBubblRoutePointRepository extends JpaRepository<TourBubblRoutePoint,Long> {

}
