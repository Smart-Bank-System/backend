package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles account-related endpoints like balance check, deposit, and withdrawal.
 */
@WebServlet("/account/*")
public class AccountController extends HttpServlet {
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/balance".equals(action)) {
            int accountId = Integer.parseInt(request.getParameter("accountId"));
            try {
                double balance = accountService.getAccountBalance(accountId);
                response.setContentType("text/plain");
                response.getWriter().println("Balance: " + balance);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching balance.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if ("/deposit".equals(action)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                double amount = Double.parseDouble(request.getParameter("amount"));
                accountService.depositMoney(accountId, amount);
                response.getWriter().println("Deposit successful");
            } else if ("/withdraw".equals(action)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                double amount = Double.parseDouble(request.getParameter("amount"));
                boolean success = accountService.withdrawMoney(accountId, amount);
                response.getWriter().println(success ? "Withdrawal successful" : "Insufficient balance");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request.");
        }
    }
}
