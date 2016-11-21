package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourAdminReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourAdminReview entity.
 */
@SuppressWarnings("unused")
public interface TourAdminReviewRepository extends JpaRepository<TourAdminReview,Long> {

    @Query("select tourAdminReview from TourAdminReview tourAdminReview where tourAdminReview.user.login = ?#{principal.username}")
    List<TourAdminReview> findByUserIsCurrentUser();

}
