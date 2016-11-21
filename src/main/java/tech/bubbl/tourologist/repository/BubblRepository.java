package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Bubbl;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Bubbl entity.
 */
@SuppressWarnings("unused")
public interface BubblRepository extends JpaRepository<Bubbl,Long> {

    @Query("select bubbl from Bubbl bubbl where bubbl.user.login = ?#{principal.username}")
    List<Bubbl> findByUserIsCurrentUser();

    @Query("select distinct bubbl from Bubbl bubbl left join fetch bubbl.interests")
    List<Bubbl> findAllWithEagerRelationships();

    @Query("select bubbl from Bubbl bubbl left join fetch bubbl.interests where bubbl.id =:id")
    Bubbl findOneWithEagerRelationships(@Param("id") Long id);

}
