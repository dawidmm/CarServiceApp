package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.repository.CarsRepository;
import pl.apserwis.ap.repository.PeopleRepository;
import pl.apserwis.ap.repository.WorkRepository;
import pl.apserwis.ap.service.CarsService;
import pl.apserwis.ap.service.PeopleService;
import pl.apserwis.ap.service.WorkService;

import java.util.*;
import java.util.stream.Collectors;

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
}
