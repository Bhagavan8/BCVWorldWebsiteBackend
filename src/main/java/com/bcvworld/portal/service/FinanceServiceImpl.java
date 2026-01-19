package com.bcvworld.portal.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcvworld.portal.model.AdSummary;
import com.bcvworld.portal.model.AdTransaction;
import com.bcvworld.portal.model.FinanceTransaction;
import com.bcvworld.portal.model.Subscription;
import com.bcvworld.portal.model.TotalFinanceSummary;
import com.bcvworld.portal.model.TrackingSummary;
import com.bcvworld.portal.model.WebsiteSummary;
import com.bcvworld.portal.model.WebsiteTransaction;
import com.bcvworld.portal.repository.AdTransactionRepository;
import com.bcvworld.portal.repository.FinanceTransactionRepository;
import com.bcvworld.portal.repository.SubscriptionRepository;
import com.bcvworld.portal.repository.WebsiteTransactionRepository;

@Service
@Transactional
public class FinanceServiceImpl implements FinanceService {

    private final AdTransactionRepository adTransactionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WebsiteTransactionRepository websiteTransactionRepository;
    private final FinanceTransactionRepository financeTransactionRepository;

    public FinanceServiceImpl(
            AdTransactionRepository adTransactionRepository,
            SubscriptionRepository subscriptionRepository,
            WebsiteTransactionRepository websiteTransactionRepository,
            FinanceTransactionRepository financeTransactionRepository
    ) {
        this.adTransactionRepository = adTransactionRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.websiteTransactionRepository = websiteTransactionRepository;
        this.financeTransactionRepository = financeTransactionRepository;
    }

    @Override
    public List<AdTransaction> getAdTransactions() {
        return adTransactionRepository.findAll();
    }

    @Override
    public AdTransaction createAdTransaction(AdTransaction transaction) {
        return adTransactionRepository.save(transaction);
    }

    @Override
    public void deleteAdTransaction(Long id) {
        adTransactionRepository.deleteById(id);
    }

    @Override
    public AdSummary getAdSummary() {
        List<AdTransaction> list = adTransactionRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate lastMonthDate = startOfMonth.minusDays(1);
        LocalDate lastMonthStart = lastMonthDate.withDayOfMonth(1);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal thisMonth = BigDecimal.ZERO;
        BigDecimal lastMonth = BigDecimal.ZERO;
        BigDecimal thisWeek = BigDecimal.ZERO;
        BigDecimal todaySpend = BigDecimal.ZERO;

        for (AdTransaction t : list) {
            BigDecimal amount = safeAmount(t.getAmount());
            LocalDate date = t.getDate();
            if (date == null) {
                total = total.add(amount);
                continue;
            }
            total = total.add(amount);
            if (date.equals(today)) {
                todaySpend = todaySpend.add(amount);
            }
            if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                thisWeek = thisWeek.add(amount);
            }
            if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                thisMonth = thisMonth.add(amount);
            }
            if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                lastMonth = lastMonth.add(amount);
            }
        }

        return new AdSummary(total, thisMonth, lastMonth, thisWeek, todaySpend);
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public List<WebsiteTransaction> getWebsiteTransactions() {
        return websiteTransactionRepository.findAll();
    }

    @Override
    public WebsiteTransaction createWebsiteTransaction(WebsiteTransaction transaction) {
        return websiteTransactionRepository.save(transaction);
    }

    @Override
    public void deleteWebsiteTransaction(Long id) {
        websiteTransactionRepository.deleteById(id);
    }

    @Override
    public WebsiteSummary getWebsiteSummary() {
        List<WebsiteTransaction> list = websiteTransactionRepository.findAll();
        BigDecimal revenue = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (WebsiteTransaction t : list) {
            BigDecimal amount = safeAmount(t.getAmount());
            String type = valueOrEmpty(t.getType()).toLowerCase();
            if ("revenue".equals(type)) {
                revenue = revenue.add(amount);
            } else {
                expense = expense.add(amount);
            }
        }
        BigDecimal net = revenue.subtract(expense);
        return new WebsiteSummary(revenue, expense, net);
    }

    @Override
    public TotalFinanceSummary getTotalFinanceSummary() {
        List<FinanceTransaction> finance = financeTransactionRepository.findAll();
        List<AdTransaction> ads = adTransactionRepository.findAll();
        List<Subscription> subs = subscriptionRepository.findAll();
        List<WebsiteTransaction> web = websiteTransactionRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate lastMonthDate = startOfMonth.minusDays(1);
        LocalDate lastMonthStart = lastMonthDate.withDayOfMonth(1);
        LocalDate startOfYear = today.withDayOfYear(1);
        LocalDate lastYearDate = startOfYear.minusDays(1);
        LocalDate lastYearStart = lastYearDate.withDayOfYear(1);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal spendAllTime = BigDecimal.ZERO;
        BigDecimal spendToday = BigDecimal.ZERO;
        BigDecimal spendWeek = BigDecimal.ZERO;
        BigDecimal spendMonth = BigDecimal.ZERO;
        BigDecimal spendLastMonth = BigDecimal.ZERO;
        BigDecimal spendYear = BigDecimal.ZERO;
        BigDecimal spendLastYear = BigDecimal.ZERO;
        BigDecimal totalAdSpend = BigDecimal.ZERO;
        BigDecimal totalWebsiteRevenue = BigDecimal.ZERO;
        BigDecimal totalWebsiteExpenses = BigDecimal.ZERO;
        BigDecimal totalRecurring = BigDecimal.ZERO;

        for (FinanceTransaction t : finance) {
            BigDecimal amount = safeAmount(t.getAmount());
            String type = valueOrEmpty(t.getType()).toLowerCase();
            LocalDate date = t.getDate();
            if ("income".equals(type)) {
                totalIncome = totalIncome.add(amount);
            } else {
                spendAllTime = spendAllTime.add(amount);
                if (date != null) {
                    if (date.equals(today)) {
                        spendToday = spendToday.add(amount);
                    }
                    if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                        spendWeek = spendWeek.add(amount);
                    }
                    if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                        spendMonth = spendMonth.add(amount);
                    }
                    if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                        spendLastMonth = spendLastMonth.add(amount);
                    }
                    if (!date.isBefore(startOfYear) && !date.isAfter(today)) {
                        spendYear = spendYear.add(amount);
                    }
                    if (!date.isBefore(lastYearStart) && !date.isAfter(lastYearDate)) {
                        spendLastYear = spendLastYear.add(amount);
                    }
                }
            }
        }

        for (AdTransaction t : ads) {
            BigDecimal amount = safeAmount(t.getAmount());
            LocalDate date = t.getDate();
            totalAdSpend = totalAdSpend.add(amount);
            spendAllTime = spendAllTime.add(amount);
            if (date != null) {
                if (date.equals(today)) {
                    spendToday = spendToday.add(amount);
                }
                if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                    spendWeek = spendWeek.add(amount);
                }
                if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                    spendMonth = spendMonth.add(amount);
                }
                if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                    spendLastMonth = spendLastMonth.add(amount);
                }
                if (!date.isBefore(startOfYear) && !date.isAfter(today)) {
                    spendYear = spendYear.add(amount);
                }
                if (!date.isBefore(lastYearStart) && !date.isAfter(lastYearDate)) {
                    spendLastYear = spendLastYear.add(amount);
                }
            }
        }

        for (WebsiteTransaction t : web) {
            BigDecimal amount = safeAmount(t.getAmount());
            String type = valueOrEmpty(t.getType()).toLowerCase();
            LocalDate date = t.getDate();
            if ("revenue".equals(type)) {
                totalWebsiteRevenue = totalWebsiteRevenue.add(amount);
                totalIncome = totalIncome.add(amount);
            } else {
                totalWebsiteExpenses = totalWebsiteExpenses.add(amount);
                spendAllTime = spendAllTime.add(amount);
                if (date != null) {
                    if (date.equals(today)) {
                        spendToday = spendToday.add(amount);
                    }
                    if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                        spendWeek = spendWeek.add(amount);
                    }
                    if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                        spendMonth = spendMonth.add(amount);
                    }
                    if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                        spendLastMonth = spendLastMonth.add(amount);
                    }
                    if (!date.isBefore(startOfYear) && !date.isAfter(today)) {
                        spendYear = spendYear.add(amount);
                    }
                    if (!date.isBefore(lastYearStart) && !date.isAfter(lastYearDate)) {
                        spendLastYear = spendLastYear.add(amount);
                    }
                }
            }
        }

        for (Subscription s : subs) {
            BigDecimal amount = safeAmount(s.getAmount());
            String frequency = valueOrEmpty(s.getFrequency());
            if ("Yearly".equalsIgnoreCase(frequency)) {
                totalRecurring = totalRecurring.add(amount.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP));
            } else {
                totalRecurring = totalRecurring.add(amount);
            }
        }

        BigDecimal netBalance = totalIncome.subtract(spendAllTime);

        TotalFinanceSummary summary = new TotalFinanceSummary();
        summary.setNetTotalBalance(netBalance);
        summary.setTotalAllSpending(spendAllTime);
        summary.setTotalAllIncome(totalIncome);
        summary.setSpendToday(spendToday);
        summary.setSpendWeek(spendWeek);
        summary.setSpendMonth(spendMonth);
        summary.setSpendLastMonth(spendLastMonth);
        summary.setSpendYear(spendYear);
        summary.setSpendLastYear(spendLastYear);
        summary.setTotalAdSpend(totalAdSpend);
        summary.setTotalInvestments(BigDecimal.ZERO);
        summary.setTotalLoans(BigDecimal.ZERO);
        summary.setTotalRecurring(totalRecurring);
        summary.setTotalWebsiteRevenue(totalWebsiteRevenue);
        summary.setTotalWebsiteExpenses(totalWebsiteExpenses);
        return summary;
    }

    @Override
    public List<FinanceTransaction> getFinanceTransactions() {
        return financeTransactionRepository.findAll();
    }

    @Override
    public FinanceTransaction createFinanceTransaction(FinanceTransaction transaction) {
        return financeTransactionRepository.save(transaction);
    }

    @Override
    public void deleteFinanceTransaction(Long id) {
        financeTransactionRepository.deleteById(id);
    }

    @Override
    public TrackingSummary getTrackingSummary() {
        List<FinanceTransaction> finance = financeTransactionRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate lastMonthDate = startOfMonth.minusDays(1);
        LocalDate lastMonthStart = lastMonthDate.withDayOfMonth(1);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal dailyExpense = BigDecimal.ZERO;
        BigDecimal weeklyExpense = BigDecimal.ZERO;
        BigDecimal monthlyExpense = BigDecimal.ZERO;
        BigDecimal lastMonthExpense = BigDecimal.ZERO;
        BigDecimal lastMonthIncome = BigDecimal.ZERO;
        BigDecimal currentMonthIncome = BigDecimal.ZERO;

        for (FinanceTransaction t : finance) {
            BigDecimal amount = safeAmount(t.getAmount());
            String type = valueOrEmpty(t.getType()).toLowerCase();
            LocalDate date = t.getDate();
            if ("income".equals(type)) {
                totalIncome = totalIncome.add(amount);
                if (date != null) {
                    if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                        currentMonthIncome = currentMonthIncome.add(amount);
                    }
                    if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                        lastMonthIncome = lastMonthIncome.add(amount);
                    }
                }
            } else {
                totalExpense = totalExpense.add(amount);
                if (date != null) {
                    if (date.equals(today)) {
                        dailyExpense = dailyExpense.add(amount);
                    }
                    if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                        weeklyExpense = weeklyExpense.add(amount);
                    }
                    if (!date.isBefore(startOfMonth) && !date.isAfter(today)) {
                        monthlyExpense = monthlyExpense.add(amount);
                    }
                    if (!date.isBefore(lastMonthStart) && !date.isAfter(lastMonthDate)) {
                        lastMonthExpense = lastMonthExpense.add(amount);
                    }
                }
            }
        }

        BigDecimal totalBalance = totalIncome.subtract(totalExpense);
        BigDecimal lastMonthSaving = lastMonthIncome.subtract(lastMonthExpense);
        BigDecimal currentMonthSaving = currentMonthIncome.subtract(monthlyExpense);
        BigDecimal savingDiff = currentMonthSaving.subtract(lastMonthSaving);

        TrackingSummary summary = new TrackingSummary();
        summary.setTotalBalance(totalBalance);
        summary.setTotalIncome(totalIncome);
        summary.setTotalLoanExpenses(BigDecimal.ZERO);
        summary.setTotalOtherExpenses(totalExpense);
        summary.setMonthlyEmiCommitment(BigDecimal.ZERO);
        summary.setTotalLoanPrincipal(BigDecimal.ZERO);
        summary.setTotalOutstandingLoan(BigDecimal.ZERO);
        summary.setTotalPending(BigDecimal.ZERO);
        summary.setDailyExpense(dailyExpense);
        summary.setWeeklyExpense(weeklyExpense);
        summary.setMonthlyExpense(monthlyExpense);
        summary.setLastMonthExpense(lastMonthExpense);
        summary.setLastMonthSaving(lastMonthSaving);
        summary.setSavingDiff(savingDiff);
        return summary;
    }

    private BigDecimal safeAmount(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }
}

