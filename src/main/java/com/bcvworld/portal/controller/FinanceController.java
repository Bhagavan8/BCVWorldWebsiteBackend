package com.bcvworld.portal.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcvworld.portal.model.AdSummary;
import com.bcvworld.portal.model.AdTransaction;
import com.bcvworld.portal.model.FinanceTransaction;
import com.bcvworld.portal.model.Subscription;
import com.bcvworld.portal.model.TotalFinanceSummary;
import com.bcvworld.portal.model.TrackingSummary;
import com.bcvworld.portal.model.WebsiteSummary;
import com.bcvworld.portal.model.WebsiteTransaction;
import com.bcvworld.portal.service.FinanceService;

@RestController
@RequestMapping("/api/admin/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/ad-transactions")
    public List<AdTransaction> getAdTransactions() {
        return financeService.getAdTransactions();
    }

    @PostMapping("/ad-transactions")
    public AdTransaction createAdTransaction(@RequestBody AdTransaction transaction) {
        return financeService.createAdTransaction(transaction);
    }

    @DeleteMapping("/ad-transactions/{id}")
    public void deleteAdTransaction(@PathVariable Long id) {
        financeService.deleteAdTransaction(id);
    }

    @GetMapping("/ad-summary")
    public AdSummary getAdSummary() {
        return financeService.getAdSummary();
    }

    @GetMapping("/subscriptions")
    public List<Subscription> getSubscriptions() {
        return financeService.getSubscriptions();
    }

    @PostMapping("/subscriptions")
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return financeService.createSubscription(subscription);
    }

    @DeleteMapping("/subscriptions/{id}")
    public void deleteSubscription(@PathVariable Long id) {
        financeService.deleteSubscription(id);
    }

    @GetMapping("/website-transactions")
    public List<WebsiteTransaction> getWebsiteTransactions() {
        return financeService.getWebsiteTransactions();
    }

    @PostMapping("/website-transactions")
    public WebsiteTransaction createWebsiteTransaction(@RequestBody WebsiteTransaction transaction) {
        return financeService.createWebsiteTransaction(transaction);
    }

    @DeleteMapping("/website-transactions/{id}")
    public void deleteWebsiteTransaction(@PathVariable Long id) {
        financeService.deleteWebsiteTransaction(id);
    }

    @GetMapping("/website-summary")
    public WebsiteSummary getWebsiteSummary() {
        return financeService.getWebsiteSummary();
    }

    @GetMapping("/total-summary")
    public TotalFinanceSummary getTotalSummary() {
        return financeService.getTotalFinanceSummary();
    }

    @GetMapping("/transactions")
    public List<FinanceTransaction> getFinanceTransactions() {
        return financeService.getFinanceTransactions();
    }

    @PostMapping("/transactions")
    public FinanceTransaction createFinanceTransaction(@RequestBody FinanceTransaction transaction) {
        return financeService.createFinanceTransaction(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public void deleteFinanceTransaction(@PathVariable Long id) {
        financeService.deleteFinanceTransaction(id);
    }

    @GetMapping("/tracking-summary")
    public TrackingSummary getTrackingSummary() {
        return financeService.getTrackingSummary();
    }
}

