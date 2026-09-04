package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.JobRequest;
import e_commerce.khilat.dtomodels.JobUpdateRequest;
import e_commerce.khilat.entity.Customer;
import e_commerce.khilat.entity.Job;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;
import e_commerce.khilat.repository.JobRepo;
import e_commerce.khilat.util.IdGenerator;

@Service
public class JobService {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private CustomerService customerService;

    public Job addJob(JobRequest request) {
        Customer customer = customerService.getById(request.getCustomerId());

        Job job = new Job();
        job.setId(IdGenerator.generate());
        job.setCustomer(customer);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setAmount(request.getAmount());
        job.setWorkStatus(WorkStatus.PENDING);
        job.setPaymentStatus(PaymentStatus.DR);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        return jobRepo.save(job);
    }

    public Job updateJob(String jobId, JobUpdateRequest request) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getAmount() != null) job.setAmount(request.getAmount());
        if (request.getWorkStatus() != null) job.setWorkStatus(request.getWorkStatus());
        if (request.getPaymentStatus() != null) job.setPaymentStatus(request.getPaymentStatus());

        job.setUpdatedAt(LocalDateTime.now());
        return jobRepo.save(job);
    }

    public void deleteJob(String jobId) {
        jobRepo.deleteById(jobId);
    }

    public List<Job> getByCustomer(String customerId) {
        return jobRepo.findByCustomerId(customerId);
    }

    public List<Job> filter(String customerId, WorkStatus workStatus, PaymentStatus paymentStatus) {

        List<Job> result;

        if (customerId != null && workStatus != null && paymentStatus != null) {
            result = jobRepo.findByCustomerIdAndWorkStatusAndPaymentStatus(customerId, workStatus, paymentStatus);
        } else if (customerId != null && workStatus != null) {
            result = jobRepo.findByCustomerIdAndWorkStatus(customerId, workStatus);
        } else if (customerId != null && paymentStatus != null) {
            result = jobRepo.findByCustomerIdAndPaymentStatus(customerId, paymentStatus);
        } else if (customerId != null) {
            result = jobRepo.findByCustomerId(customerId);
        } else if (workStatus != null && paymentStatus != null) {
            result = jobRepo.findByWorkStatusAndPaymentStatus(workStatus, paymentStatus);
        } else if (workStatus != null) {
            result = jobRepo.findByWorkStatus(workStatus);
        } else if (paymentStatus != null) {
            result = jobRepo.findByPaymentStatus(paymentStatus);
        } else {
            result = jobRepo.findAll();
        }

        result.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return result;
    }

    public List<Job> searchJobsByCustomer(String customerId, String keyword, WorkStatus workStatus,
                                           PaymentStatus paymentStatus, LocalDate fromDate, LocalDate toDate) {

        List<Job> jobs = jobRepo.findByCustomerId(customerId);

        List<Job> filtered = jobs.stream()
                .filter(j -> keyword == null || keyword.isBlank()
                        || (j.getTitle() != null && j.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                        || (j.getDescription() != null && j.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .filter(j -> workStatus == null || j.getWorkStatus() == workStatus)
                .filter(j -> paymentStatus == null || j.getPaymentStatus() == paymentStatus)
                .filter(j -> {
                    if (fromDate == null && toDate == null) return true;
                    if (j.getCreatedAt() == null) return false;
                    LocalDate createdDate = j.getCreatedAt().toLocalDate();
                    boolean afterFrom = fromDate == null || !createdDate.isBefore(fromDate);
                    boolean beforeTo = toDate == null || !createdDate.isAfter(toDate);
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());

        filtered.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return filtered;
    }

    public BigDecimal sumByPaymentStatus(PaymentStatus status) {
        return jobRepo.findByPaymentStatus(status).stream()
                .map(Job::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal sumByPaymentStatusAndRange(PaymentStatus status, LocalDateTime start, LocalDateTime end) {
        return jobRepo.findByPaymentStatus(status).stream()
                .filter(j -> !j.getCreatedAt().isBefore(start) && !j.getCreatedAt().isAfter(end))
                .map(Job::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countByWorkStatus(WorkStatus status) {
        return jobRepo.countByWorkStatus(status);
    }
}