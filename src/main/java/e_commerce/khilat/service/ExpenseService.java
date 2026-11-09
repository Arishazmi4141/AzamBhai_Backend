package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.ExpenseRequest;
import e_commerce.khilat.entity.Expense;
import e_commerce.khilat.repository.ExpenseRepo;
import e_commerce.khilat.util.IdGenerator;

@Service
public class ExpenseService {

    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);

    @Autowired
    private ExpenseRepo expenseRepo;

    public Expense addExpense(ExpenseRequest request) {
        validate(request);

        Expense e = new Expense();
        e.setId(IdGenerator.generate());
        apply(e, request);
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return expenseRepo.save(e);
    }

    public Expense updateExpense(String id, ExpenseRequest request) {
        validate(request);

        Expense e = getById(id);
        apply(e, request);
        e.setUpdatedAt(LocalDateTime.now());
        return expenseRepo.save(e);
    }

    public void deleteExpense(String id) {
        getById(id);
        expenseRepo.deleteById(id);
    }

    public Expense getById(String id) {
        return expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public List<Expense> search(String keyword, LocalDate from, LocalDate to) {
        return expenseRepo.search(
                keyword == null ? "" : keyword.trim(),
                from == null ? MIN_DATE : from,
                to == null ? MAX_DATE : to);
    }

    // Kisko kitna diya (paidTo ke hisaab se total)
    public Map<String, BigDecimal> totalsByPaidTo(LocalDate from, LocalDate to) {
        return search(null, from, to).stream()
                .collect(Collectors.groupingBy(
                        x -> x.getPaidTo().trim().toLowerCase(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));
    }

    // Kis cheez pe kitna kharcha (purpose ke hisaab se total)
    public Map<String, BigDecimal> totalsByPurpose(LocalDate from, LocalDate to) {
        return search(null, from, to).stream()
                .collect(Collectors.groupingBy(
                        x -> x.getPurpose().trim().toLowerCase(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));
    }

    // Dashboard ke liye
    public BigDecimal totalExpense() {
        return expenseRepo.sumAll();
    }

    public BigDecimal totalExpenseBetween(LocalDate from, LocalDate to) {
        return expenseRepo.sumBetween(from, to);
    }

    private void apply(Expense e, ExpenseRequest r) {
        e.setPaidTo(r.getPaidTo().trim());
        e.setPurpose(r.getPurpose().trim());
        e.setAmount(r.getAmount());
        e.setExpenseDate(r.getExpenseDate() != null ? r.getExpenseDate() : LocalDate.now());
        e.setNote(r.getNote());
    }

    private void validate(ExpenseRequest r) {
        if (r.getPaidTo() == null || r.getPaidTo().isBlank())
            throw new RuntimeException("paidTo (kisko diya) required hai");
        if (r.getPurpose() == null || r.getPurpose().isBlank())
            throw new RuntimeException("purpose (kis pe kharcha) required hai");
        if (r.getAmount() == null || r.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("amount 0 se zyada hona chahiye");
    }
}