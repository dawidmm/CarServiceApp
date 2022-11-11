package pl.apserwis.ap.entity.dto;

import pl.apserwis.ap.entity.Calendar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CalendarDto extends Calendar {

    public CalendarDto(Long id, @NotEmpty String description, @NotEmpty String plateNumber, @NotEmpty @NotNull String date) {
        super(id, description, plateNumber, date);
    }

    public CalendarDto(Calendar c) {
        this.setDescription(c.getDescription());
        this.setDate(c.getDate());
        this.setPlateNumber(c.getPlateNumber());
        this.setId(c.getId());
    }
}
