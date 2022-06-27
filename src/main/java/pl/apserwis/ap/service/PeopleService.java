package pl.apserwis.ap.service;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface PeopleService {

    Page<Map<String, String>> findAllPeopleWithParam(int page, int pageSize, String sql, String key);
}
