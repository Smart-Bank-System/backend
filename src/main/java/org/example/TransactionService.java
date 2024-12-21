package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    public boolean transferMoney(int senderId, int receiverId, double amount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            AccountService accountService = new AccountService();
            if (accountService.withdrawMoney(senderId, amount)) {
                accountService.depositMoney(receiverId, amount);
                String logQuery = "INSERT INTO transactions (sender_id, receiver_id, amount, status) VALUES (?, ?, ?, 'completed')";
                PreparedStatement logStmt = connection.prepareStatement(logQuery);
                logStmt.setInt(1, senderId);
                logStmt.setInt(2, receiverId);
                logStmt.setDouble(3, amount);
                logStmt.executeUpdate();
                connection.commit();
                return true;
            }
            connection.rollback();
        }
        return false;
    }

    public void placeTransferOnHold(int senderId, int receiverId, double amount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO transactions (sender_id, receiver_id, amount, status) VALUES (?, ?, ?, 'pending')";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }

    public List<String> getLargeTransactions() throws SQLException {
        List<String> largeTransactions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM transactions WHERE amount > 40000 AND status = 'pending'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                largeTransactions.add("Transaction ID: " + rs.getInt("transaction_id") + ", Amount: " + rs.getDouble("amount"));
            }
        }
        return largeTransactions;
    }

    public List<String> getAllTransactions() throws SQLException {
        List<String> allTransactions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM transactions";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                allTransactions.add("Transaction ID: " + rs.getInt("transaction_id") + ", Sender ID: " + rs.getInt("sender_id") +
                        ", Receiver ID: " + rs.getInt("receiver_id") + ", Amount: " + rs.getDouble("amount") +
                        ", Status: " + rs.getString("status"));
            }
        }
        return allTransactions;
    }

    public List<String> getUserTransactions(int accountId) throws SQLException {
        List<String> userTransactions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM transactions WHERE sender_id = ? OR receiver_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, accountId);
            stmt.setInt(2, accountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userTransactions.add("Transaction ID: " + rs.getInt("transaction_id") + ", Amount: " + rs.getDouble("amount") +
                        ", Status: " + rs.getString("status"));
            }
        }
        return userTransactions;
    }
}
