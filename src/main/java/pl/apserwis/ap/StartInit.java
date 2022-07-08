package pl.apserwis.ap;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.apserwis.ap.entity.dto.WorkDto;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;


@Component
@AllArgsConstructor
public class StartInit {

    private PeopleRepository p;
    private CarsRepository c;
    private WorkRepository w;

    @Bean
    public void onStartInit() {
        for (int i = 0; i < 20; i++) {
            People people = new People();
            people.setName("Dawid" + i);
            people.setSureName("Marcinek" + i);
            people.setPhone("11111111" + i);

            for (int j = 0; j < 20; j++)
                p.save(people);

            Cars car = new Cars();
            car.setPlateNumber("SRC XXX" + "" + i);
            car.setPeople(people);

            for (int j = 0; j < 20; j++)
                c.save(car);

            Work work = new Work();
            work.setDescription("Testowy opis zmiany wprowadzonych w samochodzie klienta, wraz z kompleksową obsługą bla bla bla: " + i);
            work.setPrice(i + "");
            work.setDate("2022-06-08<br>18:29");
            work.setCars(car);

            for (int j = 0; j < 20; j++)
                w.save(new WorkDto(work).getWork());
        }
    }
}
