package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.CustomerDetailResponse;
import e_commerce.khilat.dtomodels.CustomerRequest;
import e_commerce.khilat.dtomodels.CustomerSummaryResponse;
import e_commerce.khilat.entity.Customer;
import e_commerce.khilat.entity.Job;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;
import e_commerce.khilat.repository.CustomerRepo;
import e_commerce.khilat.repository.JobRepo;
import e_commerce.khilat.util.IdGenerator;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private JobRepo jobRepo;

    public Customer addCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setId(IdGenerator.generate());
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepo.save(customer);
    }

    public Customer updateCustomer(String id, CustomerRequest request) {
        Customer customer = getById(id);
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        return customerRepo.save(customer);
    }

    public void deleteCustomer(String id) {
        customerRepo.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public List<Customer> searchByName(String name) {
        return customerRepo.findByNameContainingIgnoreCase(name);
    }

    public Customer getById(String id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // Fast — N+1 query avoid kiya, 2 hi queries chalti h
    public List<CustomerSummaryResponse> getAllWithSummary() {
        List<Customer> customers = customerRepo.findAll();
        List<Job> allJobs = jobRepo.findAll();

        Map<String, List<Job>> jobsByCustomer = allJobs.stream()
                .collect(Collectors.groupingBy(j -> j.getCustomer().getId()));

        return customers.stream()
                .map(c -> buildSummary(c, jobsByCustomer.getOrDefault(c.getId(), List.of())))
                .collect(Collectors.toList());
    }

    public List<CustomerSummaryResponse> searchCustomers(String name, String phone, LocalDate fromDate, LocalDate toDate) {

        List<Customer> all = customerRepo.findAll();

        List<Customer> filtered = all.stream()
                .filter(c -> name == null || name.isBlank()
                        || (c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(c -> phone == null || phone.isBlank()
                        || (c.getPhone() != null && c.getPhone().contains(phone)))
                .filter(c -> {
                    if (fromDate == null && toDate == null) return true;
                    if (c.getCreatedAt() == null) return false;
                    LocalDate createdDate = c.getCreatedAt().toLocalDate();
                    boolean afterFrom = fromDate == null || !createdDate.isBefore(fromDate);
                    boolean beforeTo = toDate == null || !createdDate.isAfter(toDate);
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());

        List<Job> allJobs = jobRepo.findAll();
        Map<String, List<Job>> jobsByCustomer = allJobs.stream()
                .collect(Collectors.groupingBy(j -> j.getCustomer().getId()));

        return filtered.stream()
                .map(c -> buildSummary(c, jobsByCustomer.getOrDefault(c.getId(), List.of())))
                .collect(Collectors.toList());
    }

    private CustomerSummaryResponse buildSummary(Customer customer, List<Job> jobs) {
        long total = jobs.size();
        long pending = jobs.stream().filter(j -> j.getWorkStatus() == WorkStatus.PENDING).count();
        long done = jobs.stream().filter(j -> j.getWorkStatus() == WorkStatus.DONE).count();

        BigDecimal totalDR = jobs.stream()
                .filter(j -> j.getPaymentStatus() == PaymentStatus.DR)
                .map(Job::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCR = jobs.stream()
                .filter(j -> j.getPaymentStatus() == PaymentStatus.CR)
                .map(Job::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CustomerSummaryResponse.build(customer, total, pending, done, totalDR, totalCR);
    }

    public CustomerDetailResponse getDetailById(String id) {
        Customer customer = getById(id);
        return CustomerDetailResponse.build(customer);
    }
}