package com.project.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {
    final UUID id;
    private TransactionType type;
    private BigDecimal amount;
    private Category category;
    private String description;
    private LocalDate date;

    @Override
    public String toString() {
        return "Transaction{" + "id=" + id + ", type=" + type + ", amount=" + amount + ", category=" + category + ", description='" + description + '\'' + ", date=" + date + '}';
    }

    public Transaction(TransactionType type, BigDecimal amount, Category category, String description, LocalDate date) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Transaction(UUID id, TransactionType type, BigDecimal amount, Category category, String description, LocalDate date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


