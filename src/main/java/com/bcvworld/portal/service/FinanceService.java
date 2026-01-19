package com.bcvworld.portal.service;


import java.util.List;

import com.bcvworld.portal.model.AdSummary;
import com.bcvworld.portal.model.AdTransaction;
import com.bcvworld.portal.model.FinanceTransaction;
import com.bcvworld.portal.model.Subscription;
import com.bcvworld.portal.model.TotalFinanceSummary;
import com.bcvworld.portal.model.TrackingSummary;
import com.bcvworld.portal.model.WebsiteSummary;
import com.bcvworld.portal.model.WebsiteTransaction;

public interface FinanceService {

    List<AdTransaction> getAdTransactions();

    AdTransaction createAdTransaction(AdTransaction transaction);

    void deleteAdTransaction(Long id);

    AdSummary getAdSummary();

    List<Subscription> getSubscriptions();

    Subscription createSubscription(Subscription subscription);

    void deleteSubscription(Long id);

    List<WebsiteTransaction> getWebsiteTransactions();

    WebsiteTransaction createWebsiteTransaction(WebsiteTransaction transaction);

    void deleteWebsiteTransaction(Long id);

    WebsiteSummary getWebsiteSummary();

    TotalFinanceSummary getTotalFinanceSummary();

    List<FinanceTransaction> getFinanceTransactions();

    FinanceTransaction createFinanceTransaction(FinanceTransaction transaction);

    void deleteFinanceTransaction(Long id);

    TrackingSummary getTrackingSummary();
}

