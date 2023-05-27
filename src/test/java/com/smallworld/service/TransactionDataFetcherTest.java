package com.smallworld.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDataFetcherTest {

    private TransactionDataFetcher transactionDataFetcher;
    private List<Transaction> transactions;

    private static final String SENDER_FULL_NAME = "Tom Shelby";
    private static final String CLIENT_FULL_NAME = "Arthur Shelby";

    @BeforeEach
    public void init() throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource("transactions.json"); // From test resource
        if (resource == null) {
            throw new IllegalArgumentException("File not found");
        }
        this.transactions = List.of(new ObjectMapper().readValue(new File(resource.toURI()), Transaction[].class));
        transactionDataFetcher = new TransactionDataFetcherImpl(this.transactions);
    }

    @Test
    void getTotalTransactionAmountTest() {
        double totalTransactionAmount = transactionDataFetcher.getTotalTransactionAmount();
        assertThat(totalTransactionAmount).isEqualTo(2889.17);
    }

    @Test
    void getTotalTransactionAmountSentByTest() {
        double totalTransactionAmountSentBy = transactionDataFetcher.getTotalTransactionAmountSentBy(SENDER_FULL_NAME);
        assertThat(totalTransactionAmountSentBy).isEqualTo(678.06);
    }

    @Test
    void getMaxTransactionAmountTest() {
        double maxTransactionAmount = transactionDataFetcher.getMaxTransactionAmount();
        assertThat(maxTransactionAmount).isEqualTo(985.0);
    }

    @Test
    void countUniqueClientsTest() {
        long countUniqueClients = transactionDataFetcher.countUniqueClients();
        assertThat(countUniqueClients).isEqualTo(15);
    }

    @Test
    void hasOpenComplianceIssuesTest() {
        boolean hasOpenComplianceIssues = transactionDataFetcher.hasOpenComplianceIssues(CLIENT_FULL_NAME);
        assertThat(hasOpenComplianceIssues).isEqualTo(true);
    }

    @Test
    void getTransactionsByBeneficiaryNameTest() {
        Map<String, Transaction> transactionsByBeneficiaryName = transactionDataFetcher.getTransactionsByBeneficiaryName();
        Iterator<Map.Entry<String, Transaction>> iterator = transactionsByBeneficiaryName.entrySet().iterator();
        String first = iterator.next().getKey();
        String last = null;
        while (iterator.hasNext()) {
            last = iterator.next().getKey();
        }
        assertThat(first).isEqualTo("Aberama Gold");
        assertThat(last).isEqualTo("Winston Churchill");
    }

    @Test
    void getUnsolvedIssueIdsTest() {
        Set<Integer> unsolvedIssueIds = transactionDataFetcher.getUnsolvedIssueIds();
        assertThat(unsolvedIssueIds.size()).isEqualTo(5);
        assertThat(unsolvedIssueIds).contains(1, 3, 99, 54, 15);
    }

    @Test
    void getAllSolvedIssueMessagesTest() {
        List<String> allSolvedIssueMessages = transactionDataFetcher.getAllSolvedIssueMessages();
        assertThat(allSolvedIssueMessages.size()).isEqualTo(8);
        assertThat(allSolvedIssueMessages).contains( "Never gonna give you up",
                null,
                "Never gonna let you down",
                null,
                "Never gonna run around and desert you",
                null,
                null,
                null);
    }

    @Test
    void getTop3TransactionsByAmountTest() {
        List<Transaction> top3TransactionsByAmount = transactionDataFetcher.getTop3TransactionsByAmount();
        assertThat(top3TransactionsByAmount.size()).isEqualTo(3);
        assertThat(top3TransactionsByAmount.get(0).getAmount()).isEqualTo(985.0);
        assertThat(top3TransactionsByAmount.get(1).getAmount()).isEqualTo(666.0);
        assertThat(top3TransactionsByAmount.get(2).getAmount()).isEqualTo(430.2);
    }

    @Test
    void getTopSenderTest() {
        Optional<String> topSender = transactionDataFetcher.getTopSender();
        assertThat(topSender.isPresent()).isEqualTo(true);
        assertThat(topSender.get()).isEqualTo(CLIENT_FULL_NAME);
    }
}
