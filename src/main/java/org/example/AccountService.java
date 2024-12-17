package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {

    // Hesap bakiyesini sorgulama
    public static double getBalance(int accountId) throws SQLException {
        double balance = 0.0;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        }
        return balance;
    }

    // Hesap oluşturma
    public static void createAccount(String accountHolder, double initialDeposit) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO accounts (account_holder, balance) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, accountHolder);
            stmt.setDouble(2, initialDeposit);
            stmt.executeUpdate();
        }
    }
}
