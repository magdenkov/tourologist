package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Interest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Interest entity.
 */
@SuppressWarnings("unused")
public interface InterestRepository extends JpaRepository<Interest,Long> {

}
