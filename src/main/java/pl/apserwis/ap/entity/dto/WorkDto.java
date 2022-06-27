package pl.apserwis.ap.entity.dto;

import lombok.AllArgsConstructor;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.Work;

import java.util.WeakHashMap;

@AllArgsConstructor
public class WorkDto extends Work {
    Long id;
    String price;
    String desc;
    String date;

    public WorkDto(Work w) {
        this.price = w.getPrice();
        this.desc = w.getDescription();
        this.date = w.getDate();
        this.id = w.getCars().getId();
    }

    public Work getWork() {
        Work work = new Work();
        work.setDescription(desc);
        work.setPrice(price);
        work.setDate(date);

        Cars cars = new Cars();
        cars.setId(id);
        work.setCars(cars);

        return work;
    }
}
