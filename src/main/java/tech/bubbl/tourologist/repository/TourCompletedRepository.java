package tech.bubbl.tourologist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourCompleted;
import tech.bubbl.tourologist.domain.TourDownload;
import tech.bubbl.tourologist.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the TourDownload entity.
 */
@SuppressWarnings("unused")
public interface TourCompletedRepository extends JpaRepository<TourCompleted,Long>, JpaSpecificationExecutor<TourCompleted> {

//    @Query("select TourCompleted from TourCompleted tourDownload where tourDownload.user.login = ?#{principal.username}")
//    List<TourCompleted> findByUserIsCurrentUser();

    TourCompleted findOneByUserAndTour(User user, Tour tour);

    Page<TourCompleted> findByUser(User user, Pageable page);
}
