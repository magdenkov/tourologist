package tech.bubbl.tourologist.repository;

import tech.bubbl.tourologist.domain.TourImage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TourImage entity.
 */
@SuppressWarnings("unused")
public interface TourImageRepository extends JpaRepository<TourImage,Long> {

}
