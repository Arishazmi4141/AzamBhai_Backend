package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;

public class JobUpdateRequest {

    private String title;
    private String description;
    private BigDecimal amount;
    private WorkStatus workStatus;
    private PaymentStatus paymentStatus;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public WorkStatus getWorkStatus() { return workStatus; }
    public void setWorkStatus(WorkStatus workStatus) { this.workStatus = workStatus; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
}