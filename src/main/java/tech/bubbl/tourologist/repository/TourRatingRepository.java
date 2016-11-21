package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourRating;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourRating entity.
 */
@SuppressWarnings("unused")
public interface TourRatingRepository extends JpaRepository<TourRating,Long> {

    @Query("select tourRating from TourRating tourRating where tourRating.user.login = ?#{principal.username}")
    List<TourRating> findByUserIsCurrentUser();

}
