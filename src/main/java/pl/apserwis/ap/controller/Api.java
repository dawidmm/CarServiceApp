package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;
import pl.apserwis.ap.service.CarsService;
import pl.apserwis.ap.service.PeopleService;
import pl.apserwis.ap.service.WorkService;

import java.util.*;

@RestController
@AllArgsConstructor
public class Api {
    private PeopleRepository peopleRepository;
    private CarsRepository carsRepository;
    private CarsService carsService;
    private WorkRepository workRepository;
    private WorkService workService;
    private PeopleService peopleService;

    @GetMapping("/allpeople")
    public List<People> getAllPeople() {
        return peopleRepository.findAll();
    }

    @GetMapping("/allpeople/{page}/{pageSize}")
    public Page<Map<String, String>> getAllPeople(@PathVariable("page") int page,
                                                  @PathVariable("pageSize") int pageSize,
                                                  @RequestParam(value = "sql", required = false) String sql,
                                                  @RequestParam(value = "key", required = true) String key) {

        return peopleService.findAllPeopleWithParam(page, pageSize, sql, key);
    }

    @GetMapping("/allcar/{page}/{pageSize}")
    public Page<Map<String, String>> getAllCars(@PathVariable("page") int page,
                                                @PathVariable("pageSize") int pageSize,
                                                @RequestParam(value = "sql", required = false) String sql,
                                                @RequestParam(value = "key", required = true) String key) {

        return carsService.findAllCarWithParam(page, pageSize, sql, key);
    }

    @GetMapping("/allcars")
    public List<Cars> getAllCars() {
        return carsRepository.findAll();
    }


    @GetMapping("/allworks/{page}/{pageSize}")
    public Page<Work> getAllWorks(@PathVariable("page") int page,
                                  @PathVariable("pageSize") int pageSize,
                                  @RequestParam(value = "sql", required = false) String sql,
                                  @RequestParam("sdate") String startDate,
                                  @RequestParam("edate") String endDate) {

        return workService.findAllWithSort(page, pageSize, sql, startDate, endDate);
    }

    @GetMapping("/workwithplate/{page}/{pageSize}")
    public Page<Work> getWorkWithPlate(@RequestParam("id") Long id,
                                       @PathVariable("page") int page,
                                       @PathVariable("pageSize") int pageSize,
                                       @RequestParam(value = "sql", required = false) String sql,
                                       @RequestParam("sdate") String startDate,
                                       @RequestParam("edate") String endDate) {

        return workService.findAllWorkWithPlate(id, page, pageSize, sql, startDate, endDate);
    }

    @GetMapping("/workwithname/{page}/{pageSize}")
    public Page<Work> getWorkWithName(@RequestParam("id") Long id,
                                      @PathVariable("page") int page,
                                      @PathVariable("pageSize") int pageSize,
                                      @RequestParam(value = "sql", required = false) String sql,
                                      @RequestParam("sdate") String startDate,
                                      @RequestParam("edate") String endDate) {

        return workService.findAllWorkWithName(id, page, pageSize, sql, startDate, endDate);
    }

    @GetMapping("/people/car/{id}")
    public long getPeopleCarCount(@PathVariable("id") Long id) {
        return carsRepository
                .findByPeople(peopleRepository
                        .findById(id)
                        .get())
                .size();
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

    @GetMapping("/car/work/{id}")
    public long getCarWorkCount(@PathVariable("id") Long id) {
        return workRepository.findByCars(carsRepository.findById(id).get()).size();
    }

    @DeleteMapping("/people/{id}")
    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void deletePeople(@PathVariable("id") Long id) {
        var people = peopleRepository.findById(id).get();
        var cars = carsRepository.findByPeople(people);

        for (Cars c : cars) {
            for (Work w : workRepository.findByCars(c))
                deleteWork(w.getId());
            deleteCar(c.getId());
        }
        peopleRepository.delete(people);
    }

    @DeleteMapping("/car/{id}")
    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void deleteCar(@PathVariable("id") Long id) {
        var car = carsRepository.findById(id);
        if (car.isPresent()) {
            for (Work w : workRepository.findByCars(car.get()))
                deleteWork(w.getId());
            carsRepository.delete(car.get());
        }
    }

    @DeleteMapping("/work/{id}")
    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void deleteWork(@PathVariable("id") Long id) {
        var work = workRepository.findById(id);
        work.ifPresent(cars -> workRepository.delete(work.get()));
    }
}