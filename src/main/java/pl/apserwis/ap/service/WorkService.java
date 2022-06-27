package pl.apserwis.ap.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.apserwis.ap.entity.Work;

import java.util.List;

public interface WorkService {
    Page<Work> findAllWorkWithPlate(Long id, int page, int pageSize, String sql, String startDate, String endDate);

    Page<Work> findAllWithSort(int page, int pageSize, String sql, String startDate, String endDate);

    Page<Work> findAllWorkWithName(Long id, int page, int pageSize, String sql, String startDate, String endDate);
}
