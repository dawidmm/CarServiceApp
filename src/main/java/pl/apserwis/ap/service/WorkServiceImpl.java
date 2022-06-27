package pl.apserwis.ap.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.repository.WorkRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WorkServiceImpl implements WorkService {

    WorkRepository workRepository;

    @Override
    public Page<Work> findAllWorkWithPlate(Long id, int page, int pageSize, String sql, String startDate, String endDate) {
        List<Work> list = workRepository.findAll();
        List<Work> filteredList;
        if (startDate.isEmpty() && endDate.isEmpty()) {
            filteredList = list.stream().filter(c -> Objects.equals(c.getCars().getId(), id)).sorted(new SortComp(sql)).collect(Collectors.toList());
        } else {
            filteredList = getSortedWork(list, startDate, endDate).stream()
                    .filter(c -> Objects.equals(c.getCars().getId(), id))
                    .sorted(new SortComp(sql))
                    .collect(Collectors.toList());
        }

        int listSize = filteredList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        return new PageImpl<>(filteredList.subList(startIndex, endIndex));
    }

    @Override
    public Page<Work> findAllWorkWithName(Long id, int page, int pageSize, String sql, String startDate, String endDate) {
        List<Work> list = workRepository.findAll();
        List<Work> filteredList;
        if (startDate.isEmpty() && endDate.isEmpty()) {
            filteredList = list.stream().filter(c -> Objects.equals(c.getCars().getId(), id)).sorted(new SortComp(sql)).collect(Collectors.toList());
        } else {
            filteredList = getSortedWork(list, startDate, endDate).stream()
                    .filter(c -> Objects.equals(c.getCars().getPeople().getId(), id))
                    .sorted(new SortComp(sql))
                    .collect(Collectors.toList());
        }

        int listSize = filteredList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        return new PageImpl<>(filteredList.subList(startIndex, endIndex));
    }

    @Override
    public Page<Work> findAllWithSort(int page, int pageSize, String sql, String startDate, String endDate) {
        List<Work> list = workRepository.findAll();
        List<Work> filteredList;

        if (startDate.isEmpty() && endDate.isEmpty()) {
            filteredList = list.stream().sorted(new SortComp(sql)).collect(Collectors.toList());
        } else {
            filteredList = getSortedWork(list, startDate, endDate).stream()
                    .sorted(new SortComp(sql))
                    .collect(Collectors.toList());
        }

        int listSize = filteredList.size();
        int startIndex = pageSize * page;
        int endIndex = startIndex + pageSize;

        if (endIndex > listSize)
            endIndex = listSize;

        return new PageImpl<>(filteredList.subList(startIndex, endIndex));
    }

    private List<Work> getSortedWork(List<Work> workList, String startDate, String endDate) {
        return workList.stream()
                .filter(work -> {
                    String compareDate = work.getDate().split("<br>")[0];
                    if (!startDate.isEmpty() && !endDate.isEmpty()) {
                        int start = compareDate.compareTo(startDate);
                        int end = compareDate.compareTo(endDate);
                        return start >= 0 && end <= 0;
                    }
                    if (!startDate.isEmpty())
                        return compareDate.compareTo(startDate) >= 0;
                    else
                        return compareDate.compareTo(endDate) <= 0;
                }).collect(Collectors.toList());
    }

}
