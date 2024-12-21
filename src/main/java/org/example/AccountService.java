package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {

    public void createAccount(String accountHolder, double initialDeposit) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO accounts (account_holder, balance) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountHolder);
            stmt.setDouble(2, initialDeposit);
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
}
