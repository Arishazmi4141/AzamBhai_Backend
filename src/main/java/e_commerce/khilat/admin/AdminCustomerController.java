package e_commerce.khilat.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.dtomodels.CustomerDetailResponse;
import e_commerce.khilat.dtomodels.CustomerRequest;
import e_commerce.khilat.dtomodels.CustomerSummaryResponse;
import e_commerce.khilat.entity.Customer;
import e_commerce.khilat.service.CustomerService;

@RestController
@RequestMapping("/api/admin/customer")
public class AdminCustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.addCustomer(request));
    }

    @GetMapping
    public ResponseEntity<List<CustomerSummaryResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllWithSummary());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerSummaryResponse>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        LocalDate from = (fromDate != null && !fromDate.isBlank()) ? LocalDate.parse(fromDate) : null;
        LocalDate to = (toDate != null && !toDate.isBlank()) ? LocalDate.parse(toDate) : null;

        return ResponseEntity.ok(customerService.searchCustomers(name, phone, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailResponse> getCustomerDetail(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getDetailById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}