package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.BubblImage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BubblImage entity.
 */
@SuppressWarnings("unused")
public interface BubblImageRepository extends JpaRepository<BubblImage,Long> {

}
