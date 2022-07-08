package pl.apserwis.ap.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;

import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Long> {

    List<Cars> findByPeople(People people);

    @Query("Select c from Cars c")
    List<People> findAllCars(Pageable page);
}
