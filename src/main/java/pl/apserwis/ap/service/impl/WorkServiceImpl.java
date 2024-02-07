package pl.apserwis.ap.service.impl;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.apserwis.ap.comp.SortComp;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.service.WorkService;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

@AllArgsConstructor
@Service
public class WorkServiceImpl implements WorkService {

    private WorkRepository workRepository;

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
            workPath = workPath.substring(1).replaceAll("%20", " ");
            file = new File(workPath + "/" + f.getOriginalFilename());

            File workDirectory = new File(workPath);

            if (!workDirectory.exists())
                workDirectory.mkdirs();

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
        String fileName = ResourceUtils.getURL("").getFile().substring(1) + FILES_PATH + "/" + workId;
        fileName = fileName.replaceAll("%20", " ");
        Path dir = Paths.get(fileName);
        if (Files.exists(dir)) {
            Files.walk(dir).sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public File getFile(long workId, String fileName) throws IOException {
        String fileNameToGet = ResourceUtils.getURL("").getFile().substring(1) + FILES_PATH + "/" + workId + "/" + fileName;
        fileNameToGet = fileNameToGet.replaceAll("%20", " ");

        return ResourceUtils.getFile(fileNameToGet);
    }

    @Override
    @Transactional
    public void acceptWork(long workId, String signature) throws IOException, URISyntaxException {
        String base64Image = signature.split(",")[1];
        byte[] imageBytes = parseBase64Binary(base64Image);

        String workPath = ResourceUtils.getURL("").getFile() + FILES_PATH + "/" + workId + "/sign";
        workPath = workPath.replaceAll("%20", " ");

        new File(workPath).mkdirs();
        workPath += "/signature.png";
        new File(workPath).createNewFile();

        try (FileOutputStream fos = new FileOutputStream(workPath)) {
            fos.write(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Can't save file");
        }

        Optional<Work> work = workRepository.findById(workId);

        if (work.isPresent()) {
            work.get().setAccepted(true);
            workRepository.save(work.get());
        } else {
            throw new RuntimeException("Work with id not found: " + workId);
        }
    }

    @Override
    public String getBase64Signature(long workId) throws IOException {
        Optional<Work> work = workRepository.findById(workId);

        if (work.isPresent()) {
            if (work.get().getAccepted()) {
                String fileName = ResourceUtils.getURL("").getFile().substring(1) + FILES_PATH + "/" + workId + "/sign/signature.png";
                fileName = fileName.replaceAll("%20", " ");

                File f = ResourceUtils.getFile(fileName);
                byte[] encoded = Base64.encodeBase64(Files.readAllBytes(f.toPath()));

                return new String(encoded, StandardCharsets.US_ASCII);
            } else {
                throw new RuntimeException("Work is'n accepted! id: " + workId);
            }
        } else {
            throw new RuntimeException("Work with id not found: " + workId);
        }
    }
}
