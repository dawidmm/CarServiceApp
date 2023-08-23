package pl.apserwis.ap.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.Work;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    @Query("Select w from Work w Order By Id DESC")
    List<Work> findAllWork(Pageable page);

    List<Work> findByCars(Cars c);

    Work findWorkById(long id);
}
