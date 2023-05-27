package com.smallworld.service;

import com.smallworld.data.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface TransactionDataFetcher {

    /**
     * Returns the sum of the amounts of all transactions
     */
    double getTotalTransactionAmount();

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    double getTotalTransactionAmountSentBy(String senderFullName);

    /**
     * Returns the highest transaction amount
     */
    double getMaxTransactionAmount();

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    long countUniqueClients();

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    boolean hasOpenComplianceIssues(String clientFullName);

    /**
     * Returns all transactions indexed by beneficiary name
     */
    Map<String, Transaction> getTransactionsByBeneficiaryName();

    /**
     * Returns the identifiers of all open compliance issues
     */
    Set<Integer> getUnsolvedIssueIds();

    /**
     * Returns a list of all solved issue messages
     */
    List<String> getAllSolvedIssueMessages();

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    List<Transaction> getTop3TransactionsByAmount();

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    Optional<String> getTopSender();
}
