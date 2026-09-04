package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import e_commerce.khilat.entity.Job;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;

public class CustomerJobResponse {

    private String id;
    private String title;              // ✅ pehle missing tha, add kar diya (frontend ko chahiye)
    private String description;
    private BigDecimal amount;
    private WorkStatus workStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    public static CustomerJobResponse build(Job job) {

        CustomerJobResponse res = new CustomerJobResponse();

        res.id = job.getId();
        res.title = job.getTitle();
        res.description = job.getDescription();
        res.amount = job.getAmount();
        res.workStatus = job.getWorkStatus();
        res.paymentStatus = job.getPaymentStatus();
        res.createdAt = job.getCreatedAt();

        return res;
    }

    public String getId() { return id; }   // ✅ Long se String
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public WorkStatus getWorkStatus() { return workStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}