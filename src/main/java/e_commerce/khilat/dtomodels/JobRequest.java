package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;

public class JobRequest {

    private String customerId;   
    private String title;
    private String description;
    private BigDecimal amount;

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}