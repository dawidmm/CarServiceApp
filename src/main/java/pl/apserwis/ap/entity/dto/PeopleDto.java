package pl.apserwis.ap.entity.dto;


import lombok.AllArgsConstructor;
import pl.apserwis.ap.entity.People;

@AllArgsConstructor
public class PeopleDto extends People {

    private String name;

    private String sureName;

    private String phone;

    public People getPeople(){
        People people = new People();

        people.setName(name);
        people.setSureName(sureName);
        people.setPhone(phone);

        return people;
    }
}
