package pl.apserwis.ap.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import pl.apserwis.ap.entity.Work;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface WorkService {
    Page<Work> findAllWorkWithPlate(Long id, int page, int pageSize, String sql, String startDate, String endDate);

    Page<Work> findAllWithSort(int page, int pageSize, String sql, String startDate, String endDate);

    Page<Work> findAllWorkWithName(Long id, int page, int pageSize, String sql, String startDate, String endDate);

    void saveFiles(Work work, List<MultipartFile> files) throws IOException, URISyntaxException;

    List<Resource> getFiles(long workId) throws IOException;

    File getFile(long workId, String fileName) throws IOException;

    List<String> getFileNames(long workId);

    void deleteFiles(long workId) throws URISyntaxException, IOException;
}
