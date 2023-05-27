package com.smallworld.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.smallworld.Utility.distinctByKey;

@Service
public class TransactionDataFetcherImpl implements TransactionDataFetcher {

    private final List<Transaction> transactions;

    public TransactionDataFetcherImpl(List<Transaction> transactions) throws URISyntaxException, IOException {
        if (transactions.isEmpty()) {
            URL resource = getClass().getClassLoader().getResource("transactions.json");
            if (resource == null) {
                throw new IllegalArgumentException("File not found");
            }
            this.transactions = List.of(new ObjectMapper().readValue(new File(resource.toURI()), Transaction[].class));
        } else {
            this.transactions = transactions;
        }
    }

    // Returns the sum of the amounts of all transactions
    @Override
    public double getTotalTransactionAmount() {
        return transactions.parallelStream().filter(distinctByKey(Transaction::getMtn))
                .mapToDouble(Transaction::getAmount).sum();
    }

    // Returns the sum of the amounts of all transactions sent by the specified client
    @Override
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream().filter(trans -> senderFullName.equals(trans.getSenderFullName())).toList()
                .parallelStream().filter(distinctByKey(Transaction::getMtn)).mapToDouble(Transaction::getAmount).sum();
    }

    // Returns the highest transaction amount
    @Override
    public double getMaxTransactionAmount() {
        return transactions.parallelStream().mapToDouble(trans -> trans.getAmount()).max().getAsDouble();
    }

    // Counts the number of unique clients that sent or received a transaction
    @Override
    public long countUniqueClients() {
        return transactions.parallelStream().filter(distinctByKey(Transaction::getSenderFullName)).count() +
                transactions.parallelStream().filter(distinctByKey(Transaction::getBeneficiaryFullName)).count();
    }

    // Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
    // issue that has not been solved
    @Override
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.parallelStream().anyMatch(trans -> clientFullName.equals(trans.getSenderFullName()) ||
                clientFullName.equals(trans.getBeneficiaryFullName()) && !trans.isIssueSolved());
    }

    // Returns all transactions indexed by beneficiary name
    @Override
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> unsorted = new HashMap<>();
        transactions.forEach(trans -> unsorted.put(trans.getBeneficiaryFullName(), trans));
        return unsorted.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    // Returns the identifiers of all open compliance issues
    @Override
    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.parallelStream().filter(trans -> !trans.isIssueSolved()).map(m -> m.getIssueId())
                .collect(Collectors.toSet());
    }

    // Returns a list of all solved issue messages
    @Override
    public List<String> getAllSolvedIssueMessages() {
        return transactions.parallelStream().filter(trans -> trans.isIssueSolved()).map(m -> m.getIssueMessage())
                .toList();
    }

    // Returns the 3 transactions with the highest amount sorted by amount descending
    @Override
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.parallelStream().filter(distinctByKey(Transaction::getMtn))
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed()).toList().subList(0, 3);
    }

    // Returns the senderFullName of the sender with the most total sent amount
    @Override
    public Optional<String> getTopSender() {
        Optional<Map.Entry<String, Double>> val =
                transactions.parallelStream()
                        .filter(distinctByKey(Transaction::getMtn))
                        .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)))
                        .entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new))
                        .entrySet().stream().findFirst();
        Optional<String> topSender;
        if (val.isPresent()) {
            topSender = Optional.of(val.get().getKey());
        } else {
            topSender = Optional.of(null);
        }
        return topSender;
    }

}
