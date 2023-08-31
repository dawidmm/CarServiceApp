package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.apserwis.ap.entity.Calendar;
import pl.apserwis.ap.service.CalendarService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CalendarController {

    private CalendarService calendarService;

    @GetMapping("/calendar/all")
    public List<Calendar> getAllCalendar() {
        return calendarService.getAll();
    }

    @GetMapping("/calendar/{pageSize}/{page}")
    public Page<Calendar> getAllCalendarWithPaging(@PathVariable("pageSize") int pageSize, @PathVariable("page") int page) {
        return calendarService.get(pageSize, page);
    }

    @DeleteMapping("/calendar/{id}")
    public void deleteById(@PathVariable("id") long id) {
        calendarService.deleteById(id);
    }

    @PostMapping("/calendar")
    public int addCalendar(@RequestParam(value = "desc") String desc,
                           @RequestParam(value = "plate") String plate,
                           @RequestParam(value = "date") String date) {
        Calendar calendar = new Calendar(null, desc, plate, date);
        try {
            calendarService.save(calendar);
        } catch (Exception e) {
            return 500;
        }

        return 201;
    }
}
