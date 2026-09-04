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

    public DashboardResponse getDashboard() {
        BigDecimal totalDR = jobService.sumByPaymentStatus(PaymentStatus.DR);
        BigDecimal totalCR = jobService.sumByPaymentStatus(PaymentStatus.CR);

        LocalDate today = LocalDate.now();

        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfWeek = today.with(ChronoField.DAY_OF_WEEK, 1).atStartOfDay();
        LocalDateTime startOfYear = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        BigDecimal monthlyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, startOfMonth, endOfDay);
        BigDecimal weeklyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, startOfWeek, endOfDay);
        BigDecimal yearlyRevenue = jobService.sumByPaymentStatusAndRange(PaymentStatus.CR, startOfYear, endOfDay);

        long pending = jobService.countByWorkStatus(WorkStatus.PENDING);
        long done = jobService.countByWorkStatus(WorkStatus.DONE);

        return new DashboardResponse(totalDR, totalCR, monthlyRevenue, weeklyRevenue, yearlyRevenue, pending, done);
    }
}