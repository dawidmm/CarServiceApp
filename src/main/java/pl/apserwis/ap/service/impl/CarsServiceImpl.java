package pl.apserwis.ap.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import pl.apserwis.ap.comp.MapSortForCarsComp;
import pl.apserwis.ap.entity.Cars;
import pl.apserwis.ap.repository.CarsRepository;
import pl.apserwis.ap.repository.WorkRepository;
import pl.apserwis.ap.service.CarsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarsServiceImpl implements CarsService {

    private CarsRepository carsRepository;
    private WorkRepository workRepository;

    @Override
    public Page<Map<String, String>> findAllCarWithParam(int page, int pageSize, String sql, String key) {
        List<Cars> carsList = carsRepository.findAll().stream()
                .filter(f -> f.getPlateNumber().contains(key.toUpperCase()))
                .collect(Collectors.toList());

        Map<String, String> map;
        List<Map<String, String>> endMapList = new ArrayList<>();

        for (Cars c : carsList) {
            map = new HashMap<>();
            map.put("plate", c.getPlateNumber());
            map.put("name", c.getPeople().getName()+" "+c.getPeople().getSureName());
            map.put("phone", c.getPeople().getPhone());
            map.put("works", String.valueOf(workRepository.findByCars(c).size()));

            endMapList.add(map);
        }

        int listSize = endMapList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        endMapList = endMapList.stream().sorted(new MapSortForCarsComp(sql)).collect(Collectors.toList());

        return new PageImpl<>(endMapList.subList(startIndex, endIndex));
    }
}
