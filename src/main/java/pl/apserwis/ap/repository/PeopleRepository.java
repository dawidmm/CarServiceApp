package pl.apserwis.ap.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.apserwis.ap.entity.People;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {

    @Query("Select p from People p")
    List<People> findAllPeople(Pageable page);
}
