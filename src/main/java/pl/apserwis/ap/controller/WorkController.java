package pl.apserwis.ap.controller;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.apserwis.ap.entity.Work;
import pl.apserwis.ap.entity.dto.WorkDto;
import pl.apserwis.ap.service.WorkService;
import pl.apserwis.ap.service.repository.WorkRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class WorkController {

    private WorkRepository workRepository;
    private WorkService workService;

    @GetMapping("/workwithplate/{page}/{pageSize}")
    public Page<Work> getWorkWithPlate(@RequestParam("id") Long id,
                                       @PathVariable("page") int page,
                                       @PathVariable("pageSize") int pageSize,
                                       @RequestParam(value = "sql", required = false) String sql,
                                       @RequestParam("sdate") String startDate,
                                       @RequestParam("edate") String endDate) {

        return workService.findAllWorkWithPlate(id, page, pageSize, sql, startDate, endDate);
    }

    @GetMapping("/workwithname/{page}/{pageSize}")
    public Page<Work> getWorkWithName(@RequestParam("id") Long id,
                                      @PathVariable("page") int page,
                                      @PathVariable("pageSize") int pageSize,
                                      @RequestParam(value = "sql", required = false) String sql,
                                      @RequestParam("sdate") String startDate,
                                      @RequestParam("edate") String endDate) {

        return workService.findAllWorkWithName(id, page, pageSize, sql, startDate, endDate);
    }

    @DeleteMapping("/work/{id}")
    @Cascade(value = CascadeType.ALL)
    @Transactional
    public void deleteWork(@PathVariable("id") Long id) throws URISyntaxException, IOException {
        var work = workRepository.findById(id);
        work.ifPresent(cars -> workRepository.delete(work.get()));
        workService.deleteFiles(id);
    }

    @Transactional
    @PostMapping(value = "/add_work")
    public ResponseEntity<Work> addWork(String car,
//                                        String price,
                                        String desc,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        Work work;

        if (car.isEmpty() || desc.isEmpty())
            throw new RuntimeException("Wrong request");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Timestamp timestamp;

        timestamp = new Timestamp(System.currentTimeMillis());
        String[] date = sdf.format(timestamp).split(" ");
        try {
            work = workRepository.save(new WorkDto(Long.parseLong(car)
//                    ,price
                    , desc, date[0] + "<br>" + date[1]).getWork());

            if (files != null) {
                workService.saveFiles(work, files);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return new ResponseEntity<>(work, HttpStatus.CREATED);
    }

    @GetMapping("/files/names/{workId}")
    public ResponseEntity<List<String>> getFilesNames(@PathVariable("workId") Long workId) {
        List<String> fileNames = workService.getFileNames(workId);

        return new ResponseEntity<>(fileNames, HttpStatus.OK);
    }

    @GetMapping(value = "/files/{workId}/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getFile(@PathVariable("workId") long workId, @PathVariable(value = "fileName") String fileName
    ) throws IOException {
        File f = workService.getFile(workId, fileName);
        Resource r = new InputStreamResource(new FileInputStream(f));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + f.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(f.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(r);
    }

    @PostMapping("/work/accept/{workId}")
    public ResponseEntity<Boolean> acceptWork(@PathVariable("workId") String workId,
                                              @RequestParam("signature") String signature) throws IOException, URISyntaxException {
        workService.acceptWork(Long.parseLong(workId), signature);

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @GetMapping("/work/{workId}/signature")
    public ResponseEntity<String> getSignature(@PathVariable("workId") String workId) throws IOException {
        return new ResponseEntity<>(workService.getBase64Signature(Long.parseLong(workId)), HttpStatus.OK);
    }

    @GetMapping("/work/{workId}")
    public ResponseEntity<WorkDto> getWorkById(@PathVariable("workId") String workId) {
        Optional<Work> workOpt = workRepository.findById(Long.parseLong(workId));

        if (workOpt.isPresent()) {
            WorkDto workDto = new WorkDto(workOpt.get());
            return new ResponseEntity<>(workDto, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/allworks/{page}/{pageSize}")
    public Page<Work> getAllWorks(@PathVariable("page") int page,
                                  @PathVariable("pageSize") int pageSize,
                                  @RequestParam(value = "sql", required = false) String sql,
                                  @RequestParam("sdate") String startDate,
                                  @RequestParam("edate") String endDate) {

        return workService.findAllWithSort(page, pageSize, sql, startDate, endDate);
    }

    @Transactional
    @PostMapping("/edit_work/{id}")
    public ResponseEntity<String> editWork(@PathVariable("id") int id, @RequestBody String desc) {
        Optional<Work> workOpt = workRepository.findById(Long.parseLong(String.valueOf(id)));
        if (workOpt.isPresent()) {
            Work work = workOpt.get();
            work.setDescription(desc);
            workRepository.save(work);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();

    }
}
