package pl.apserwis.ap.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.entity.People;
import pl.apserwis.ap.repository.CarsRepository;
import pl.apserwis.ap.repository.PeopleRepository;
import pl.apserwis.ap.repository.WorkRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PeopleServiceImpl implements PeopleService{

    WorkRepository workRepository;
    PeopleRepository peopleRepository;
    CarsRepository carsRepository;

    @Override
    public Page<Map<String, String>> findAllPeopleWithParam(int page, int pageSize, String sql, String key) {
        List<People> list = peopleRepository.findAll().stream().filter((t) -> {
            boolean checkName, checkSurname, all;
            checkName = t.getName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT));
            checkSurname = t.getSureName().toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT));
            all = (t.getName().toLowerCase(Locale.ROOT) + " " + t.getSureName().toLowerCase(Locale.ROOT)).contains(key.toLowerCase(Locale.ROOT));
            return (checkName || checkSurname || all);
        }).collect(Collectors.toList());

        Map<String, String> map;
        List<Map<String, String>> endMapList = new ArrayList<>();

        for (People p : list) {
            List<Cars> carsList = carsRepository.findByPeople(p);
            int workCount = 0;
            map = new HashMap<>();

            map.put("name", p.getName() + " " + p.getSureName());
            map.put("phone", p.getPhone());
            map.put("cars", String.valueOf(carsList.size()));

            for (Cars c : carsList){
                workCount += workRepository.findByCars(c).size();
            }

            map.put("works", String.valueOf(workCount));
            endMapList.add(map);
        }

        int listSize = endMapList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        endMapList = endMapList.stream().sorted(new MapSortComp(sql)).collect(Collectors.toList());

        return new PageImpl<>(endMapList.subList(startIndex, endIndex));
    }
}
