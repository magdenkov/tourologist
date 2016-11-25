package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Tour;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tour entity.
 */
@SuppressWarnings("unused")
public interface TourRepository extends JpaRepository<Tour,Long> {

    @Query("select tour from Tour tour where tour.user.login = ?#{principal.username}")
    List<Tour> findByUserIsCurrentUser();

    @Query("select distinct tour from Tour tour left join fetch tour.interests")
    List<Tour> findAllWithEagerRelationships();

    @Query("select tour from Tour tour left join fetch tour.interests left join fetch tour.tourBubbls left join fetch tour.tourRoutePoints left join fetch tour.tourImages where tour.id =:id")
    Tour findOneWithEagerRelationships(@Param("id") Long id);

}
