package com.project.expensetracker.repository;

import com.project.expensetracker.model.Transaction;

import java.util.*;

public class TransactionRepository {
    private final Map<UUID, Transaction> transactions;

    public TransactionRepository(Map<UUID, Transaction> transactions) {
        this.transactions = transactions;
    }

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(UUID id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public boolean update(Transaction transaction) {
        UUID id = transaction.getId();
        if (!transactions.containsKey(id)) {
            return false;
        }
        transactions.put(id, transaction);
        return true;
    }

    public boolean deleteById(UUID id) {
        return transactions.remove(id) != null;
    }
}
