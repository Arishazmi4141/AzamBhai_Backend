package e_commerce.khilat.dtomodels;

import java.math.BigDecimal;

public class DashboardResponse {

    // Jobs
    private BigDecimal totalDRRemaining;   // baaki (DR)
    private BigDecimal totalCRPaid;        // mila hua (CR)
    private BigDecimal totalJobAmount;     // DR + CR

    // Revenue (CR)
    private BigDecimal monthlyRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal yearlyRevenue;

    // Expenses
    private BigDecimal totalExpenses;
    private BigDecimal weeklyExpenses;
    private BigDecimal monthlyExpenses;
    private BigDecimal yearlyExpenses;

    // Net = CR - Expenses
    private BigDecimal netBalance;
    private BigDecimal weeklyNet;
    private BigDecimal monthlyNet;
    private BigDecimal yearlyNet;

    private long pendingJobsCount;
    private long doneJobsCount;

    public DashboardResponse(BigDecimal totalDRRemaining, BigDecimal totalCRPaid,
                             BigDecimal monthlyRevenue, BigDecimal weeklyRevenue, BigDecimal yearlyRevenue,
                             BigDecimal totalExpenses, BigDecimal weeklyExpenses,
                             BigDecimal monthlyExpenses, BigDecimal yearlyExpenses,
                             long pendingJobsCount, long doneJobsCount) {
        this.totalDRRemaining = totalDRRemaining;
        this.totalCRPaid = totalCRPaid;
        this.totalJobAmount = totalDRRemaining.add(totalCRPaid);
        this.monthlyRevenue = monthlyRevenue;
        this.weeklyRevenue = weeklyRevenue;
        this.yearlyRevenue = yearlyRevenue;
        this.totalExpenses = totalExpenses;
        this.weeklyExpenses = weeklyExpenses;
        this.monthlyExpenses = monthlyExpenses;
        this.yearlyExpenses = yearlyExpenses;
        this.netBalance = totalCRPaid.subtract(totalExpenses);
        this.weeklyNet = weeklyRevenue.subtract(weeklyExpenses);
        this.monthlyNet = monthlyRevenue.subtract(monthlyExpenses);
        this.yearlyNet = yearlyRevenue.subtract(yearlyExpenses);
        this.pendingJobsCount = pendingJobsCount;
        this.doneJobsCount = doneJobsCount;
    }

    public BigDecimal getTotalDRRemaining() { return totalDRRemaining; }
    public BigDecimal getTotalCRPaid() { return totalCRPaid; }
    public BigDecimal getTotalJobAmount() { return totalJobAmount; }
    public BigDecimal getMonthlyRevenue() { return monthlyRevenue; }
    public BigDecimal getWeeklyRevenue() { return weeklyRevenue; }
    public BigDecimal getYearlyRevenue() { return yearlyRevenue; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getWeeklyExpenses() { return weeklyExpenses; }
    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
    public BigDecimal getYearlyExpenses() { return yearlyExpenses; }
    public BigDecimal getNetBalance() { return netBalance; }
    public BigDecimal getWeeklyNet() { return weeklyNet; }
    public BigDecimal getMonthlyNet() { return monthlyNet; }
    public BigDecimal getYearlyNet() { return yearlyNet; }
    public long getPendingJobsCount() { return pendingJobsCount; }
    public long getDoneJobsCount() { return doneJobsCount; }
}