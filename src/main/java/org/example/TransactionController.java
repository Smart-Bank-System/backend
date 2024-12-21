package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles money transfer and transaction history.
 */
@WebServlet("/transaction/*")
public class TransactionController extends HttpServlet {
    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String action = request.getPathInfo();

        try {
            if (role == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized: Please login.");
                return;
            }

            if ("/transfer".equals(action) && "user".equals(role)) {
                int senderId = Integer.parseInt(request.getParameter("senderId"));
                int receiverId = Integer.parseInt(request.getParameter("receiverId"));
                double amount = Double.parseDouble(request.getParameter("amount"));

                if (amount > 40000) {
                    transactionService.placeTransferOnHold(senderId, receiverId, amount);
                    response.getWriter().println("Transfer placed on hold for admin approval.");
                } else {
                    boolean success = transactionService.transferMoney(senderId, receiverId, amount);
                    response.getWriter().println(success ? "Transfer successful." : "Transfer failed.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized action.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
