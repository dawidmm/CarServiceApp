package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.dto.CarsDto;
import pl.apserwis.ap.service.CarsService;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CarController {

    private CarsRepository carsRepository;
    private CarsService carsService;
    private WorkRepository workRepository;

    @GetMapping("/allcars")
    public List<Cars> getAllCars() {
        return carsRepository.findAll();
    }

    @DeleteMapping("/car/{id}")
    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void deleteCar(@PathVariable("id") Long id) throws URISyntaxException, IOException {
        var car = carsRepository.findById(id);
        if (car.isPresent()) {
            carsRepository.delete(car.get());
        }
    }

    @PostMapping("/add_car")
    public int addCar(Long owner, String plate, String vin) {
        long count = carsRepository.findAll().stream()
                .filter(
                        e -> e.getPlateNumber().equals(plate.toUpperCase())
                                || e.getVin().equals(vin.toUpperCase())
                ).count();

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

    @GetMapping("/allcar/{page}/{pageSize}")
    public Page<Map<String, String>> getAllCars(@PathVariable("page") int page,
                                                @PathVariable("pageSize") int pageSize,
                                                @RequestParam(value = "sql", required = false) String sql,
                                                @RequestParam(value = "key", required = true) String key) {

        return carsService.findAllCarWithParam(page, pageSize, sql, key);
    }

    @GetMapping("/car/work/{id}")
    public long getCarWorkCount(@PathVariable("id") Long id) {
        return workRepository.findByCars(carsRepository.findById(id).get()).size();
    }
}
