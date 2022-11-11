package pl.apserwis.ap.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.apserwis.ap.entity.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
