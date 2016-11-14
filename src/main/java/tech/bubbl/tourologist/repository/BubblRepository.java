package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Bubbl;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bubbl entity.
 */
@SuppressWarnings("unused")
public interface BubblRepository extends JpaRepository<Bubbl,Long> {

    @Query("select bubbl from Bubbl bubbl where bubbl.user.login = ?#{principal.username}")
    List<Bubbl> findByUserIsCurrentUser();

}
