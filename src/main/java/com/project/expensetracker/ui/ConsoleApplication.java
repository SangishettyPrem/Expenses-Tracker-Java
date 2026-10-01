package com.project.expensetracker.ui;

import com.project.expensetracker.exception.InvalidTransactionException;
import com.project.expensetracker.exception.TransactionNotFoundException;
import com.project.expensetracker.model.Category;
import com.project.expensetracker.model.Transaction;
import com.project.expensetracker.model.TransactionType;
import com.project.expensetracker.repository.TransactionRepository;
import com.project.expensetracker.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleApplication {

    private final Scanner scanner;
    private final TransactionService transactionService;

    public ConsoleApplication() {
        Map<UUID, Transaction> transactionStore = new HashMap<>();
        TransactionRepository transactionRepository = new TransactionRepository(transactionStore);
        this.transactionService = new TransactionService(transactionRepository);
        this.scanner = new Scanner(System.in);
    }

    private Category selectCategory() {
        while (true) {
            System.out.println("Select the Category:");
            int index = 1;

            Category[] categories = Category.values();

            for (Category value : categories) {
                System.out.println(index + ". " + value);
                index++;
            }
            int choice = readInt("Enter your choice: ");
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            }
            System.out.println("Invalid Category. Please try again.");
        }
    }

    private TransactionType selectTransactionType() {
        while (true) {
            System.out.println("Select Transaction Type:");
            int index = 1;
            TransactionType[] transactionTypes = TransactionType.values();
            for (TransactionType type : transactionTypes) {
                System.out.println(index + ". " + type);
                index++;
            }
            int choice = readInt("Enter your choice: ");
            if (choice >= 1 && choice <= transactionTypes.length) {
                return transactionTypes[choice - 1];
            }
            System.out.println("Invalid Choice. Please try again.");
        }
    }

    private void addIncome() {
        try {
            System.out.println("\n========== ADD INCOME ==========");
            BigDecimal amount = readBigDecimal("Amount: ");
            System.out.println("Category:");
            Category category = selectCategory();
            String description = readString("Description: ");
            Transaction transaction = new Transaction(TransactionType.INCOME, amount, category, description, LocalDate.now());
            Transaction addedTransaction = transactionService.addTransaction(transaction);

            System.out.println("\nIncome Added Successfully.");
            System.out.println(addedTransaction);

        } catch (InvalidTransactionException e) {

            System.out.println(e.getMessage());
        }
    }

    private void addExpense() {
        try {
            System.out.println("\n========== ADD EXPENSE ==========");
            BigDecimal amount = readBigDecimal("Amount: ");
            System.out.println("Category:");
            Category category = selectCategory();
            String description = readString("Description: ");
            Transaction transaction = new Transaction(TransactionType.EXPENSE, amount, category, description, LocalDate.now());
            Transaction addedTransaction = transactionService.addTransaction(transaction);
            System.out.println("\nExpense Added Successfully.");
            System.out.println(addedTransaction);
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n========== ALL TRANSACTIONS ==========");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private void showTransactionById() {
        try {
            System.out.println("\n========== FIND TRANSACTION ==========");
            String id = readString("Enter the ID to View: ");
            UUID uuid = UUID.fromString(id);
            Transaction transaction = transactionService.getTransactionById(uuid);
            System.out.println(transaction);
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID Format.");
        }
    }

    private void deleteTransaction() {
        try {
            System.out.println("\n========== DELETE TRANSACTION ==========");
            String id = readString("Enter the ID to Delete: ");
            UUID uuid = UUID.fromString(id);
            transactionService.deleteTransaction(uuid);
            System.out.println("Transaction Deleted Successfully.");
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID Format.");
        }
    }

    private void updateTransaction() {
        try {
            System.out.println("\n========== UPDATE TRANSACTION ==========");
            String id = readString("Enter the ID to Update the Transaction: ");
            UUID uuid = UUID.fromString(id);

            Transaction transaction = transactionService.getTransactionById(uuid);

            System.out.println("\nCurrent Transaction:");
            System.out.println(transaction);

            if (askYesNo("Want to Update Transaction Type?")) {
                transaction.setType(selectTransactionType());
            }

            if (askYesNo("Want to Update Amount?")) {
                BigDecimal amount = readBigDecimal("Enter the New Amount: ");
                transaction.setAmount(amount);
            }

            if (askYesNo("Want to Update Category?")) {
                Category category = selectCategory();
                transaction.setCategory(category);
            }

            if (askYesNo("Want to Update Description?")) {
                String description = readString("Enter the New Description: ");
                transaction.setDescription(description);
            }

            transactionService.updateTransaction(transaction);
            System.out.println("Transaction Updated Successfully.");
        } catch (InvalidTransactionException | TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID Format.");
        }
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid number.");
            }
        }
    }

    private BigDecimal readBigDecimal(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }

    private String readString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private boolean askYesNo(String message) {
        while (true) {
            System.out.print(message + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            }
            if (input.equals("N")) {
                return false;
            }
            System.out.println("Invalid choice. Please enter Y or N.");
        }
    }

    public void showMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       PERSONAL EXPENSE TRACKER");
        System.out.println("========================================");

        System.out.println();
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. View All Transactions");
        System.out.println("4. View Transaction By ID");
        System.out.println("5. Update Transaction");
        System.out.println("6. Delete Transaction");
        System.out.println("7. Exit");

        System.out.println();
    }

    public void start() {
        while (true) {
            showMenu();
            int choice = readInt("Enter your Choice: ");
            switch (choice) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    showTransactions();
                    break;
                case 4:
                    showTransactionById();
                    break;
                case 5:
                    updateTransaction();
                    break;
                case 6:
                    deleteTransaction();
                    break;
                case 7:
                    System.out.println("Thank you for using Personal Expense Tracker.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid Choice. Please select 1-7.");
            }
        }
    }

    public static void main(String[] args) {
        ConsoleApplication application = new ConsoleApplication();
        application.start();
    }
}