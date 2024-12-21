package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles account-related endpoints for both user and admin roles.
 */
@WebServlet("/account/*")
public class AccountController extends HttpServlet {
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String action = request.getPathInfo();

        if (role == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized: Please login.");
            return;
        }

        try {
            if ("/balance".equals(action) && "user".equals(role)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                double balance = accountService.getAccountBalance(accountId);
                response.getWriter().println("Balance: " + balance);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid action or role.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String action = request.getPathInfo();

        if (role == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized: Please login.");
            return;
        }

        try {
            if ("/deposit".equals(action) && "user".equals(role)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                double amount = Double.parseDouble(request.getParameter("amount"));
                accountService.depositMoney(accountId, amount);
                response.getWriter().println("Deposit successful.");
            } else if ("/withdraw".equals(action) && "user".equals(role)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                double amount = Double.parseDouble(request.getParameter("amount"));
                boolean success = accountService.withdrawMoney(accountId, amount);
                response.getWriter().println(success ? "Withdrawal successful." : "Insufficient balance.");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid action or role.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
