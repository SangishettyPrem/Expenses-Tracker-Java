package com.project.expensetracker.service;

import com.project.expensetracker.exception.InvalidTransactionException;
import com.project.expensetracker.exception.TransactionNotFoundException;
import com.project.expensetracker.model.Transaction;
import com.project.expensetracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    private void validateTransaction(Transaction transaction) throws InvalidTransactionException {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidTransactionException("Invalid Amount. Amount Should be Greater than Zero.");

        if (transaction.getCategory() == null) throw new InvalidTransactionException("Invalid Category");

        if (transaction.getType() == null) throw new InvalidTransactionException("Invalid Transaction Type");

        if (transaction.getDate() == null) throw new InvalidTransactionException("Invalid Date");
    }

    public Transaction addTransaction(Transaction transaction) throws InvalidTransactionException {
        validateTransaction(transaction);
        return repository.save(transaction);
    }

    public Transaction getTransactionById(UUID id) throws TransactionNotFoundException {
        return repository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction Not Found."));
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public boolean updateTransaction(Transaction transaction) throws InvalidTransactionException, TransactionNotFoundException {
        validateTransaction(transaction);
        if (!repository.update(transaction)) throw new TransactionNotFoundException("Transaction Not Found.");
        return true;
    }

    public boolean deleteTransaction(UUID id) throws TransactionNotFoundException {
        boolean deleted = repository.deleteById(id);

        if (!deleted) {
            throw new TransactionNotFoundException("Transaction Not Found.");
        }

        return true;
    }
}
