package dio.innovation.beerAPI.repository;

import dio.innovation.beerAPI.entity.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeerRepository extends JpaRepository<BeerEntity, Long> {

    Optional<BeerEntity> findByName(String name);
}
