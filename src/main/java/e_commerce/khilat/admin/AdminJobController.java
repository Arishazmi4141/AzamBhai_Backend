package e_commerce.khilat.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.dtomodels.JobRequest;
import e_commerce.khilat.dtomodels.JobUpdateRequest;
import e_commerce.khilat.entity.Job;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;
import e_commerce.khilat.service.JobService;

@RestController
@RequestMapping("/api/admin/job")
public class AdminJobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> addJob(@RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.addJob(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable String id, @RequestBody JobUpdateRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Job>> getJobsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(jobService.getByCustomer(customerId));
    }

    @GetMapping("/customer/{customerId}/search")
    public ResponseEntity<List<Job>> searchJobsByCustomer(
            @PathVariable String customerId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) WorkStatus workStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        LocalDate from = (fromDate != null && !fromDate.isBlank()) ? LocalDate.parse(fromDate) : null;
        LocalDate to = (toDate != null && !toDate.isBlank()) ? LocalDate.parse(toDate) : null;

        return ResponseEntity.ok(
                jobService.searchJobsByCustomer(customerId, keyword, workStatus, paymentStatus, from, to)
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Job>> filterJobs(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) WorkStatus workStatus,
            @RequestParam(required = false) PaymentStatus paymentStatus) {

        return ResponseEntity.ok(jobService.filter(customerId, workStatus, paymentStatus));
    }
}