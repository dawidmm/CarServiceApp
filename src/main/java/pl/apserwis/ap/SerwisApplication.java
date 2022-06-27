package pl.apserwis.ap;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.repository.CarsRepository;
import pl.apserwis.ap.repository.PeopleRepository;
import pl.apserwis.ap.repository.WorkRepository;

import java.util.Date;

@SpringBootApplication
public class SerwisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SerwisApplication.class, args);
    }

}
