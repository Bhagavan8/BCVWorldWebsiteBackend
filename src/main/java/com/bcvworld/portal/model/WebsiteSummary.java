package com.bcvworld.portal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteSummary {

    private BigDecimal totalRevenue;

    private BigDecimal totalExpense;

    private BigDecimal netProfit;
    

	public WebsiteSummary(BigDecimal totalRevenue, BigDecimal totalExpense, BigDecimal netProfit) {
		super();
		this.totalRevenue = totalRevenue;
		this.totalExpense = totalExpense;
		this.netProfit = netProfit;
	}

	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public BigDecimal getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(BigDecimal totalExpense) {
		this.totalExpense = totalExpense;
	}

	public BigDecimal getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(BigDecimal netProfit) {
		this.netProfit = netProfit;
	}
    
}

