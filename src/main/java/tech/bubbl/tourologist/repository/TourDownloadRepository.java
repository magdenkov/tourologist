package tech.bubbl.tourologist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bubbl.tourologist.domain.Tour;
import tech.bubbl.tourologist.domain.TourDownload;

import org.springframework.data.jpa.repository.*;
import tech.bubbl.tourologist.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the TourDownload entity.
 */
@SuppressWarnings("unused")
public interface TourDownloadRepository extends JpaRepository<TourDownload,Long>, JpaSpecificationExecutor<TourDownload> {

    @Query("select tourDownload from TourDownload tourDownload where tourDownload.user.login = ?#{principal.username}")
    List<TourDownload> findByUserIsCurrentUser();

    TourDownload findOneByUserAndTour(User user, Tour tour);

    Page<List<TourDownload>> findByUser(User user, Pageable page);
}
