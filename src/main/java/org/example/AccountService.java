package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    public void createAccount(String accountHolder, double initialDeposit) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO accounts (account_holder, balance, status) VALUES (?, ?, 'pending')";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountHolder);
            stmt.setDouble(2, initialDeposit);
            stmt.executeUpdate();
        }
    }

    public void approveAccount(int accountId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE accounts SET status = 'approved' WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        }
    }

    public double getAccountBalance(int accountId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0.0;
    }

    public void depositMoney(int accountId, double amount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDouble(1, amount);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    public boolean withdrawMoney(int accountId, double amount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getDouble("balance") >= amount) {
                String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, accountId);
                updateStmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public List<String> getPendingAccounts() throws SQLException {
        List<String> pendingAccounts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM accounts WHERE status = 'pending'";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pendingAccounts.add("Account ID: " + rs.getInt("account_id") + ", Holder: " + rs.getString("account_holder"));
            }
        }
        return pendingAccounts;
    }
}
