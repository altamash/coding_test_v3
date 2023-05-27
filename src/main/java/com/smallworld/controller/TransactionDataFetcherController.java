package com.smallworld.controller;

import com.smallworld.data.Transaction;
import com.smallworld.service.TransactionDataFetcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class TransactionDataFetcherController {

    private final TransactionDataFetcher transactionDataFetcher;

    public TransactionDataFetcherController(TransactionDataFetcher transactionDataFetcher) {
        this.transactionDataFetcher = transactionDataFetcher;
    }

    @GetMapping("/totalTransactionAmount")
    public double getTotalTransactionAmount() {
        return transactionDataFetcher.getTotalTransactionAmount();
    }

    @GetMapping("/totalTransactionAmountSentBy/{senderFullName}")
    public double getTotalTransactionAmountSentBy(@PathVariable String senderFullName) {
        return transactionDataFetcher.getTotalTransactionAmountSentBy(senderFullName);
    }

    @GetMapping("/maxTransactionAmount")
    public double getMaxTransactionAmount() {
        return transactionDataFetcher.getMaxTransactionAmount();
    }

    @GetMapping("/countUniqueClients")
    public double countUniqueClients() {
        return transactionDataFetcher.countUniqueClients();
    }

    @GetMapping("/hasOpenComplianceIssues/{clientFullName}")
    public boolean hasOpenComplianceIssues(@PathVariable String clientFullName) {
        return transactionDataFetcher.hasOpenComplianceIssues(clientFullName);
    }

    @GetMapping("/transactionsByBeneficiaryName")
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        return transactionDataFetcher.getTransactionsByBeneficiaryName();
    }

    @GetMapping("/unsolvedIssueIds")
    public Set<Integer> getUnsolvedIssueIds() {
        return transactionDataFetcher.getUnsolvedIssueIds();
    }

    @GetMapping("/allSolvedIssueMessages")
    public List<String> getAllSolvedIssueMessages() {
        return transactionDataFetcher.getAllSolvedIssueMessages();
    }

    @GetMapping("/top3TransactionsByAmount")
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactionDataFetcher.getTop3TransactionsByAmount();
    }

    @GetMapping("/topSender")
    public Optional<String> getTopSender() {
        return transactionDataFetcher.getTopSender();
    }
}
