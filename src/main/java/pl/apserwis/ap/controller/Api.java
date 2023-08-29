package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.apserwis.ap.entity.Calendar;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.entity.dto.CarsDto;
import pl.apserwis.ap.entity.dto.WorkDto;
import pl.apserwis.ap.service.CalendarService;
import pl.apserwis.ap.service.CarsService;
import pl.apserwis.ap.service.PeopleService;
import pl.apserwis.ap.service.WorkService;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class Api {
    private PeopleRepository peopleRepository;
    private CarsRepository carsRepository;
    private CarsService carsService;
    private WorkRepository workRepository;
    private WorkService workService;
    private PeopleService peopleService;
    private CalendarService calendarService;

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
    public void deletePeople(@PathVariable("id") Long id) throws URISyntaxException, IOException {
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
    public void deleteCar(@PathVariable("id") Long id) throws URISyntaxException, IOException {
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
    public void deleteWork(@PathVariable("id") Long id) throws URISyntaxException, IOException {
        var work = workRepository.findById(id);
        work.ifPresent(cars -> workRepository.delete(work.get()));
        workService.deleteFiles(id);
    }

    @PostMapping("/add_car")
    public int addCar(Long owner, String plate, String vin) {
        long count = carsRepository.findAll().stream().filter(e -> e.getPlateNumber().equals(plate.toUpperCase()) || e.getVin().equals(vin.toUpperCase())).count();

        if (plate.isEmpty())
            return 500;

        if (count == 0)
            try {
                carsRepository.save(new CarsDto(owner, plate.toUpperCase(), vin.toUpperCase()).getCars());
            } catch (Exception e) {
                return 500;
            }
        else
            return 500;
        return 200;
    }

    @Transactional
    @PostMapping(value = "/add_work")
    public ResponseEntity<Work> addWork(String car,
                                        String price,
                                        String desc,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Work work;

        if (car.isEmpty() || desc.isEmpty())
            throw new RuntimeException("Wrong request");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Timestamp timestamp;

        timestamp = new Timestamp(System.currentTimeMillis());
        String[] date = sdf.format(timestamp).split(" ");
        try {
            work = workRepository.save(new WorkDto(Long.parseLong(car), price, desc, date[0] + "<br>" + date[1]).getWork());

            if (files != null) {
                workService.saveFiles(work, files);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return new ResponseEntity<>(work, HttpStatus.CREATED);
    }

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

    @GetMapping("/files/names/{workId}")
    public ResponseEntity<List<String>> getFilesNames(@PathVariable("workId") Long workId) {
        List<String> fileNames = workService.getFileNames(workId);

        return new ResponseEntity<>(fileNames, HttpStatus.OK);
    }

    @GetMapping(value = "/files/{workId}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getFile(@PathVariable("workId") long workId, @PathVariable(value = "fileName") String fileName
    ) throws IOException {
        File f = workService.getFile(workId, fileName);
        Resource r = new InputStreamResource(new FileInputStream(f));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + f.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(f.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(r);
    }

    @PostMapping("/work/accept/{workId}")
    public ResponseEntity<Boolean> acceptWork(@PathVariable("workId") String workId,
                                              @RequestParam("signature") String signature) throws IOException, URISyntaxException {
        workService.acceptWork(Long.parseLong(workId), signature);

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @GetMapping("/work/{workId}/signature")
    public ResponseEntity<String> getSignature(@PathVariable("workId") String workId) throws IOException {
        return new ResponseEntity<>(workService.getBase64Signature(Long.parseLong(workId)), HttpStatus.OK);
    }
}