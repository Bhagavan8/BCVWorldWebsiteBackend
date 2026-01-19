package com.bcvworld.portal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSummary {

    private BigDecimal totalSpend;

    private BigDecimal thisMonthSpend;

    private BigDecimal lastMonthSpend;

    private BigDecimal thisWeekSpend;

    private BigDecimal todaySpend;
    

	public AdSummary(BigDecimal totalSpend, BigDecimal thisMonthSpend, BigDecimal lastMonthSpend,
			BigDecimal thisWeekSpend, BigDecimal todaySpend) {
		super();
		this.totalSpend = totalSpend;
		this.thisMonthSpend = thisMonthSpend;
		this.lastMonthSpend = lastMonthSpend;
		this.thisWeekSpend = thisWeekSpend;
		this.todaySpend = todaySpend;
	}

	public BigDecimal getTotalSpend() {
		return totalSpend;
	}

	public void setTotalSpend(BigDecimal totalSpend) {
		this.totalSpend = totalSpend;
	}

	public BigDecimal getThisMonthSpend() {
		return thisMonthSpend;
	}

	public void setThisMonthSpend(BigDecimal thisMonthSpend) {
		this.thisMonthSpend = thisMonthSpend;
	}

	public BigDecimal getLastMonthSpend() {
		return lastMonthSpend;
	}

	public void setLastMonthSpend(BigDecimal lastMonthSpend) {
		this.lastMonthSpend = lastMonthSpend;
	}

	public BigDecimal getThisWeekSpend() {
		return thisWeekSpend;
	}

	public void setThisWeekSpend(BigDecimal thisWeekSpend) {
		this.thisWeekSpend = thisWeekSpend;
	}

	public BigDecimal getTodaySpend() {
		return todaySpend;
	}

	public void setTodaySpend(BigDecimal todaySpend) {
		this.todaySpend = todaySpend;
	}
    
}

