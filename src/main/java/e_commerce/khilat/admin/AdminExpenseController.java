package e_commerce.khilat.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import e_commerce.khilat.dtomodels.ExpenseRequest;
import e_commerce.khilat.entity.Expense;
import e_commerce.khilat.service.ExpenseService;

@RestController
@RequestMapping("/api/admin/expense")
public class AdminExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> add(@RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(request));
    }

    // GET /api/admin/expense?keyword=ram&fromDate=2026-01-01&toDate=2026-12-31
    @GetMapping
    public ResponseEntity<List<Expense>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        return ResponseEntity.ok(expenseService.search(keyword, parse(fromDate), parse(toDate)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> get(@PathVariable String id) {
        return ResponseEntity.ok(expenseService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable String id, @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // Kisko kitna diya
    @GetMapping("/summary/paid-to")
    public ResponseEntity<Map<String, BigDecimal>> byPaidTo(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        return ResponseEntity.ok(expenseService.totalsByPaidTo(parse(fromDate), parse(toDate)));
    }

    // Kis cheez pe kitna kharcha
    @GetMapping("/summary/purpose")
    public ResponseEntity<Map<String, BigDecimal>> byPurpose(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        return ResponseEntity.ok(expenseService.totalsByPurpose(parse(fromDate), parse(toDate)));
    }

    private LocalDate parse(String d) {
        return (d != null && !d.isBlank()) ? LocalDate.parse(d) : null;
    }
}