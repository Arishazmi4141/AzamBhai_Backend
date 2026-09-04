package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;

public class DashboardResponse {

    private BigDecimal totalDRRemaining;
    private BigDecimal totalCRPaid;
    private BigDecimal monthlyRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal yearlyRevenue;
    private long pendingJobsCount;
    private long doneJobsCount;

    public DashboardResponse(BigDecimal totalDRRemaining, BigDecimal totalCRPaid,
                              BigDecimal monthlyRevenue, BigDecimal weeklyRevenue, BigDecimal yearlyRevenue,
                              long pendingJobsCount, long doneJobsCount) {
        this.totalDRRemaining = totalDRRemaining;
        this.totalCRPaid = totalCRPaid;
        this.monthlyRevenue = monthlyRevenue;
        this.weeklyRevenue = weeklyRevenue;
        this.yearlyRevenue = yearlyRevenue;
        this.pendingJobsCount = pendingJobsCount;
        this.doneJobsCount = doneJobsCount;
    }

    public BigDecimal getTotalDRRemaining() { return totalDRRemaining; }
    public BigDecimal getTotalCRPaid() { return totalCRPaid; }
    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public BigDecimal getWeeklyRevenue() { return weeklyRevenue; }
    public BigDecimal getYearlyRevenue() { return yearlyRevenue; }
    public long getPendingJobsCount() { return pendingJobsCount; }
    public long getDoneJobsCount() { return doneJobsCount; }
}