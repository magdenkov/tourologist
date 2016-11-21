package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.Payload;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Payload entity.
 */
@SuppressWarnings("unused")
public interface PayloadRepository extends JpaRepository<Payload,Long> {

}
