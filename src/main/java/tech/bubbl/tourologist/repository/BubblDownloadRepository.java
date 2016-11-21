package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.BubblDownload;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BubblDownload entity.
 */
@SuppressWarnings("unused")
public interface BubblDownloadRepository extends JpaRepository<BubblDownload,Long> {

    @Query("select bubblDownload from BubblDownload bubblDownload where bubblDownload.user.login = ?#{principal.username}")
    List<BubblDownload> findByUserIsCurrentUser();

}
