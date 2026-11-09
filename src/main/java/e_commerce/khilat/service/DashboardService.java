package e_commerce.khilat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.DashboardResponse;
import e_commerce.khilat.entity.PaymentStatus;
import e_commerce.khilat.entity.WorkStatus;

@Service
public class DashboardService {

    @Autowired
    private JobService jobService;

    @Autowired
    private ExpenseService expenseService;

    public DashboardResponse getDashboard() {
        BigDecimal totalDR = jobService.sumByPaymentStatus(PaymentStatus.DR);
        BigDecimal totalCR = jobService.sumByPaymentStatus(PaymentStatus.CR);

        LocalDate today = LocalDate.now();

        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate weekStart = today.with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate yearStart = today.withDayOfYear(1);
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Revenue (CR jobs)
        BigDecimal monthlyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, monthStart.atStartOfDay(), endOfDay);
        BigDecimal weeklyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, weekStart.atStartOfDay(), endOfDay);
        BigDecimal yearlyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, yearStart.atStartOfDay(), endOfDay);

        // Expenses
        BigDecimal totalExpenses = expenseService.totalExpense();
        BigDecimal weeklyExpenses = expenseService.totalExpenseBetween(weekStart, today);
        BigDecimal monthlyExpenses = expenseService.totalExpenseBetween(monthStart, today);
        BigDecimal yearlyExpenses = expenseService.totalExpenseBetween(yearStart, today);

        long pending = jobService.countByWorkStatus(WorkStatus.PENDING);
        long done = jobService.countByWorkStatus(WorkStatus.DONE);

        return new DashboardResponse(totalDR, totalCR,
                monthlyRevenue, weeklyRevenue, yearlyRevenue,
                totalExpenses, weeklyExpenses, monthlyExpenses, yearlyExpenses,
                pending, done);
    }
}