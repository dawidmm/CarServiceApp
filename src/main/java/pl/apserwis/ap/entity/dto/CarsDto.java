package pl.apserwis.ap.entity.dto;

import lombok.AllArgsConstructor;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;

@AllArgsConstructor
public class CarsDto extends Cars {
    Long id;
    String plate;


    public Cars getCars(){
        Cars cars = new Cars();
        People people = new People();

        people.setId(id);

        cars.setPeople(people);
        cars.setPlateNumber(plate);

        return cars;
    }
}
