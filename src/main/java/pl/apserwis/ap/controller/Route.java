package pl.apserwis.ap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apserwis.ap.entity.dto.CarsDto;
import pl.apserwis.ap.entity.dto.PeopleDto;
import pl.apserwis.ap.entity.dto.WorkDto;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@org.springframework.stereotype.Controller
public class Route {

    private PeopleRepository peopleRepository;
    private CarsRepository carsRepository;
    private WorkRepository workRepository;

    public Route(PeopleRepository peopleRepository, CarsRepository carsRepository, WorkRepository workRepository) {
        this.peopleRepository = peopleRepository;
        this.carsRepository = carsRepository;
        this.workRepository = workRepository;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Timestamp timestamp;

    // *HTML TEMPLATE*

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/add")
    public String add() {
        return "add";
    }

    @GetMapping("/add_work")
    public String addWork() {
        return "add_work";
    }

    @GetMapping("/add_car")
    public String car() {
        return "add_car";
    }

    @GetMapping("/find")
    public String find() {
        return "find";
    }

    @GetMapping("/all")
    public String all() {
        return "all";
    }

    @GetMapping("/finduser")
    public String findUser() {
        return "find_user";
    }

    @GetMapping("/findcar")
    public String findCars() {
        return "find_car";
    }

    @GetMapping("/remove_people")
    public String removePeople() {
        return "remove_people";
    }

    @GetMapping("/remove_car")
    public String removeCar() {
        return "remove_car";
    }

    @GetMapping("/remove_work")
    public String removeWork() {
        return "remove_work";
    }

    // *POST MAPPING*

    @PostMapping("/add")
    public String addPeople(String name, String surname, String phone) {
        long count = peopleRepository.findAll().stream().filter(e -> e.getName().equals(name) && e.getSureName().equals(surname)).count();

        if (count == 0)
            peopleRepository.save(new PeopleDto(name, surname, phone).getPeople());
        else
            return "redirect:/error";
        return "redirect:/add?add";
    }

    @PostMapping("/add_car")
    public String addCar(Long owner, String plate, String vin) {
        long count = carsRepository.findAll().stream().filter(e -> e.getPlateNumber().equals(plate)).count();

        if (count == 0)
            carsRepository.save(new CarsDto(owner, plate.toUpperCase(), vin.toUpperCase()).getCars());
        else
            return "redirect:/error";
        return "redirect:/add_car?add";
    }

    @PostMapping("/add_work")
    public String addWork(Long car, String price, String desc) {
        timestamp = new Timestamp(System.currentTimeMillis());
        String[] date = sdf.format(timestamp).split(" ");
        workRepository.save(new WorkDto(car, price, desc, date[0] + "<br>" + date[1]).getWork());
        return "redirect:/add_work?add";
    }
}
