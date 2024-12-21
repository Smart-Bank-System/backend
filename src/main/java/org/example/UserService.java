package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean loginUser(String tckn, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM users WHERE tckn = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, tckn);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        }
        return false;
    }

    public void registerUser(String fullName, String tckn, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // TCKN kontrolü
            String checkQuery = "SELECT COUNT(*) FROM users WHERE tckn = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, tckn);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Bu TCKN ile bir kullanıcı zaten mevcut: " + tckn);
            }

            // Yeni kullanıcı kaydı
            String query = "INSERT INTO users (full_name, tckn, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, fullName);
            stmt.setString(2, tckn);
            stmt.setString(3, password);
            stmt.executeUpdate();
        }
    }

}
