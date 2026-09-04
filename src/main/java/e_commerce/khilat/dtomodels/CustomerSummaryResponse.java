package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import e_commerce.khilat.entity.Customer;

public class CustomerSummaryResponse {

    private String id;   // ✅ Long se String
    private String name;
    private String phone;
    private String email;
    private long totalJobs;
    private long pendingJobs;
    private long doneJobs;
    private BigDecimal totalDR;
    private BigDecimal totalCR;

    public static CustomerSummaryResponse build(Customer customer, long totalJobs, long pendingJobs,
                                                  long doneJobs, BigDecimal totalDR, BigDecimal totalCR) {
        CustomerSummaryResponse res = new CustomerSummaryResponse();
        res.id = customer.getId();
        res.name = customer.getName();
        res.phone = customer.getPhone();
        res.email = customer.getEmail();
        res.totalJobs = totalJobs;
        res.pendingJobs = pendingJobs;
        res.doneJobs = doneJobs;
        res.totalDR = totalDR;
        res.totalCR = totalCR;
        return res;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public long getTotalJobs() { return totalJobs; }
    public long getPendingJobs() { return pendingJobs; }
    public long getDoneJobs() { return doneJobs; }
    public BigDecimal getTotalDR() { return totalDR; }
    public BigDecimal getTotalCR() { return totalCR; }
}