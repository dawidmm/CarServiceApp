package pl.apserwis.ap;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pl.apserwis.ap.entity.Calendar;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.entity.dto.WorkDto;
import pl.apserwis.ap.service.repository.CalendarRepository;
import pl.apserwis.ap.service.repository.CarsRepository;
import pl.apserwis.ap.service.repository.PeopleRepository;
import pl.apserwis.ap.service.repository.WorkRepository;


@Component
@AllArgsConstructor
public class StartInit {

    private PeopleRepository p;
    private CarsRepository c;
    private WorkRepository w;
    private CalendarRepository cal;

    @Bean
    public void onStartInit() {
        for (int i = 0; i < 10; i++) {
            People people = new People();
            people.setName("Dawid" + i);
            people.setSureName("Marcinek" + i);
            people.setPhone("11111111" + i);

            for (int j = 0; j < 10; j++)
                p.save(people);

            Cars car = new Cars();
            car.setPlateNumber("SRC XXX" + "" + i);
            car.setVin("TESTTESTTESTTESTTEST" + i);
            car.setPeople(people);

            for (int j = 0; j < 10; j++)
                c.save(car);

            Work work = new Work();
            work.setDescription("Testowy opis zmiany wprowadzonych w samochodzie klienta, wraz z kompleksową obsługą bla bla bla: " + i);
//            work.setPrice(i + "");
            work.setDate("2022-06-08<br>18:29");
            work.setCars(car);

            for (int j = 0; j < 10; j++)
                w.save(new WorkDto(work).getWork());

            Calendar calendar = new Calendar();
            calendar.setDescription("test" + i);
            calendar.setDate(i + "-11-2021");
            calendar.setPlateNumber("SRC RY02");

            cal.save(calendar);
        }
    }
}
