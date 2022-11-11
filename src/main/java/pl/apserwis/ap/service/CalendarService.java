package pl.apserwis.ap.service;

import pl.apserwis.ap.entity.Calendar;

import java.util.List;

public interface CalendarService {

    Calendar save(Calendar calendar);

    List<Calendar> getAll();

    List<Calendar> get(int pageSize, int page);

    void deleteById(Long id);
}
