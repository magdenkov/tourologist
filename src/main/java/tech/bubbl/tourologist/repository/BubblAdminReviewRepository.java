package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.BubblAdminReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BubblAdminReview entity.
 */
@SuppressWarnings("unused")
public interface BubblAdminReviewRepository extends JpaRepository<BubblAdminReview,Long> {

    @Query("select bubblAdminReview from BubblAdminReview bubblAdminReview where bubblAdminReview.user.login = ?#{principal.username}")
    List<BubblAdminReview> findByUserIsCurrentUser();

}
