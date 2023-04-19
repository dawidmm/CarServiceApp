package pl.apserwis.ap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.apserwis.ap.entity.dto.PeopleDto;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;

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

    @GetMapping("/add_calendar")
    public String addCalendar() {
        return "add_calendar";
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
}
