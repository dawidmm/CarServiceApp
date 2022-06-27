package pl.apserwis.ap.service;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface CarsService {

    Page<Map<String, String>> findAllCarWithParam(int page, int pageSize, String sql, String key);
}
