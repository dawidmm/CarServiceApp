package pl.apserwis.ap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<Calendar> get(int pageSize, int page) {
        List<Calendar> calendarList = getAll().stream().sorted(new DateComp()).collect(Collectors.toList());

        if (calendarList.size() < pageSize)
            return new PageImpl<>(calendarList);

        int listSize = calendarList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        return new PageImpl<>(calendarList.subList(startIndex, endIndex));
    }

    public void deleteById(Long id) {
        calendarRepository.deleteById(id);
    }
}
