package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourDownload;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourDownload entity.
 */
@SuppressWarnings("unused")
public interface TourDownloadRepository extends JpaRepository<TourDownload,Long> {

    @Query("select tourDownload from TourDownload tourDownload where tourDownload.user.login = ?#{principal.username}")
    List<TourDownload> findByUserIsCurrentUser();

}
