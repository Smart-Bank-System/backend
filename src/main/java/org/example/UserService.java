package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public String loginUser(String tckn, String password) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE tckn = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, tckn);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        }
        return null;
    }

    public void registerUser(String fullName, String tckn, String password, String role) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM users WHERE tckn = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, tckn);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("User with TCKN already exists: " + tckn);
            }

            String query = "INSERT INTO users (full_name, tckn, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, fullName);
            stmt.setString(2, tckn);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.executeUpdate();
        }
    }

    public void updateUserRole(String tckn, String newRole) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE users SET role = ? WHERE tckn = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newRole);
            stmt.setString(2, tckn);
            stmt.executeUpdate();
        }
    }
}
