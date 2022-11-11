package pl.apserwis.ap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.apserwis.ap.comp.DateComp;
import pl.apserwis.ap.entity.Calendar;
import pl.apserwis.ap.service.CalendarService;
import pl.apserwis.ap.service.repository.CalendarRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Override
    public Calendar save(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Override
    public List<Calendar> getAll() {
        return calendarRepository.findAll();
    }

    @Override
    public List<Calendar> get(int pageSize, int page) {
        List<Calendar> calendarList = getAll().stream().sorted(new DateComp()).collect(Collectors.toList());

        if (calendarList.size() < pageSize)
            return calendarList;

        if (page <= 0)
            return calendarList.subList(0, pageSize);
        else
            return calendarList.subList(page * pageSize, page * pageSize + pageSize);
    }

    public void deleteById(Long id) {
        calendarRepository.deleteById(id);
    }
}
