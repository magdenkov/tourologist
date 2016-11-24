package tech.bubbl.tourologist.repository;

import org.springframework.data.repository.query.Param;
import tech.bubbl.tourologist.domain.Interest;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Interest entity.
 */
@SuppressWarnings("unused")
public interface InterestRepository extends JpaRepository<Interest,Long> {

    @Query("SELECT dpt FROM Interest dpt WHERE dpt.id in (:ids) ")
    Set<Interest> findByInterestsIds(@Param("ids")List<Integer> interestIds);
}
