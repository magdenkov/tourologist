package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.BubblRating;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BubblRating entity.
 */
@SuppressWarnings("unused")
public interface BubblRatingRepository extends JpaRepository<BubblRating,Long> {

    @Query("select bubblRating from BubblRating bubblRating where bubblRating.user.login = ?#{principal.username}")
    List<BubblRating> findByUserIsCurrentUser();

}
