package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest {

    private String paidTo;
    private String purpose;
    private BigDecimal amount;
    private LocalDate expenseDate;   // "yyyy-MM-dd", optional (default = aaj)
    private String note;

    public String getPaidTo() { return paidTo; }
    public void setPaidTo(String paidTo) { this.paidTo = paidTo; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}