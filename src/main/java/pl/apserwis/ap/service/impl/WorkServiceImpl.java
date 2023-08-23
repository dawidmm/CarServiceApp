package pl.apserwis.ap.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.apserwis.ap.comp.SortComp;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.service.WorkService;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WorkServiceImpl implements WorkService {

    private WorkRepository workRepository;
    private ResourceLoader rs;

    public static String FILES_PATH = "files";

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
            filteredList = list.stream().filter(c -> Objects.equals(c.getCars().getPeople().getId(), id)).sorted(new SortComp(sql)).collect(Collectors.toList());
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

    public void saveFiles(Work work, List<MultipartFile> files) throws IOException {
        File file;

        List<String> fileNames = new ArrayList<>();

        for (MultipartFile f : files) {
            String workPath = ResourceUtils.getURL("").getFile() + FILES_PATH + "/" + work.getId();
            file = new File(workPath + "/" + f.getOriginalFilename());

            File workDirectory = new File(workPath);

            if (!workDirectory.exists())
                workDirectory.mkdir();

            file.createNewFile();
            f.transferTo(file);
            fileNames.add(f.getOriginalFilename());
        }

        work.setFiles(fileNames);
        workRepository.save(work);
    }

    @Override
    public List<Resource> getFiles(long workId) throws IOException {
        return Arrays.asList();
    }

    @Override
    public List<String> getFileNames(long workId) {
        return workRepository.findWorkById(workId).getFiles();
    }

    @Override
    public void deleteFiles(long workId) throws URISyntaxException, IOException {
        Path dir = Paths.get(ResourceUtils.getURL("").getFile().substring(1) + FILES_PATH + "/" + workId);

        Files.walk(dir).sorted(Comparator.reverseOrder()).forEach(p -> {
            try {
                Files.delete(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public File getFile(long workId, String fileName) throws IOException {
        return ResourceUtils.getFile(
                ResourceUtils.getURL("").getFile().substring(1) + FILES_PATH + "/" + workId + "/" + fileName);
    }
}
