package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.service.PeopleService;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PeopleController {

    private PeopleRepository peopleRepository;
    private PeopleService peopleService;
    private WorkRepository workRepository;
    private CarsRepository carsRepository;

    @GetMapping("/allpeople")
    public List<People> getAllPeople() {
        return peopleRepository.findAll();
    }

    @DeleteMapping("/people/{id}")
    @Transactional
    public void deletePeople(@PathVariable("id") Long id) {
        var people = peopleRepository.findById(id).get();

        peopleRepository.delete(people);
    }

    @GetMapping("/allpeople/{page}/{pageSize}")
    public Page<Map<String, String>> getAllPeople(@PathVariable("page") int page,
                                                  @PathVariable("pageSize") int pageSize,
                                                  @RequestParam(value = "sql", required = false) String sql,
                                                  @RequestParam(value = "key", required = true) String key) {

        return peopleService.findAllPeopleWithParam(page, pageSize, sql, key);
    }

    @GetMapping("/people/work/{id}")
    public long getPeopleWorkCount(@PathVariable("id") Long id) {
        try {
            return workRepository
                    .findByCars(carsRepository
                            .findByPeople(peopleRepository
                                    .findById(id)
                                    .get())
                            .stream()
                            .findAny()
                            .get())
                    .size();
        } catch (Exception e) {
            return 0;
        }
    }

    @GetMapping("/people/car/{id}")
    public long getPeopleCarCount(@PathVariable("id") Long id) {
        return carsRepository
                .findByPeople(peopleRepository
                        .findById(id)
                        .get())
                .size();
    }
}
