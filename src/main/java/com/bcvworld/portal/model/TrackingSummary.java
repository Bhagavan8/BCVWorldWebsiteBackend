package com.bcvworld.portal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingSummary {

    private BigDecimal totalBalance;

    private BigDecimal totalIncome;

    private BigDecimal totalLoanExpenses;

    private BigDecimal totalOtherExpenses;

    private BigDecimal monthlyEmiCommitment;

    private BigDecimal totalLoanPrincipal;

    private BigDecimal totalOutstandingLoan;

    private BigDecimal totalPending;

    private BigDecimal dailyExpense;

    private BigDecimal weeklyExpense;

    private BigDecimal monthlyExpense;

    private BigDecimal lastMonthExpense;

    private BigDecimal lastMonthSaving;

    private BigDecimal savingDiff;

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalLoanExpenses() {
		return totalLoanExpenses;
	}

	public void setTotalLoanExpenses(BigDecimal totalLoanExpenses) {
		this.totalLoanExpenses = totalLoanExpenses;
	}

	public BigDecimal getTotalOtherExpenses() {
		return totalOtherExpenses;
	}

	public void setTotalOtherExpenses(BigDecimal totalOtherExpenses) {
		this.totalOtherExpenses = totalOtherExpenses;
	}

	public BigDecimal getMonthlyEmiCommitment() {
		return monthlyEmiCommitment;
	}

	public void setMonthlyEmiCommitment(BigDecimal monthlyEmiCommitment) {
		this.monthlyEmiCommitment = monthlyEmiCommitment;
	}

	public BigDecimal getTotalLoanPrincipal() {
		return totalLoanPrincipal;
	}

	public void setTotalLoanPrincipal(BigDecimal totalLoanPrincipal) {
		this.totalLoanPrincipal = totalLoanPrincipal;
	}

	public BigDecimal getTotalOutstandingLoan() {
		return totalOutstandingLoan;
	}

	public void setTotalOutstandingLoan(BigDecimal totalOutstandingLoan) {
		this.totalOutstandingLoan = totalOutstandingLoan;
	}

	public BigDecimal getTotalPending() {
		return totalPending;
	}

	public void setTotalPending(BigDecimal totalPending) {
		this.totalPending = totalPending;
	}

	public BigDecimal getDailyExpense() {
		return dailyExpense;
	}

	public void setDailyExpense(BigDecimal dailyExpense) {
		this.dailyExpense = dailyExpense;
	}

	public BigDecimal getWeeklyExpense() {
		return weeklyExpense;
	}

	public void setWeeklyExpense(BigDecimal weeklyExpense) {
		this.weeklyExpense = weeklyExpense;
	}

	public BigDecimal getMonthlyExpense() {
		return monthlyExpense;
	}

	public void setMonthlyExpense(BigDecimal monthlyExpense) {
		this.monthlyExpense = monthlyExpense;
	}

	public BigDecimal getLastMonthExpense() {
		return lastMonthExpense;
	}

	public void setLastMonthExpense(BigDecimal lastMonthExpense) {
		this.lastMonthExpense = lastMonthExpense;
	}

	public BigDecimal getLastMonthSaving() {
		return lastMonthSaving;
	}

	public void setLastMonthSaving(BigDecimal lastMonthSaving) {
		this.lastMonthSaving = lastMonthSaving;
	}

	public BigDecimal getSavingDiff() {
		return savingDiff;
	}

	public void setSavingDiff(BigDecimal savingDiff) {
		this.savingDiff = savingDiff;
	}
    
}

