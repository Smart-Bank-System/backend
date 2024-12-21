package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionService {

    public boolean transferMoney(int senderId, int receiverId, double amount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            AccountService accountService = new AccountService();
            if (accountService.withdrawMoney(senderId, amount)) {
                accountService.depositMoney(receiverId, amount);
                String logQuery = "INSERT INTO transactions (sender_id, receiver_id, amount) VALUES (?, ?, ?)";
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
}
