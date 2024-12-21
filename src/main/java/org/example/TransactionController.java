package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles money transfer and transaction history.
 */
@WebServlet("/transaction/*")
public class TransactionController extends HttpServlet {
    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/transfer".equals(action)) {
            int senderId = Integer.parseInt(request.getParameter("senderId"));
            int receiverId = Integer.parseInt(request.getParameter("receiverId"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            try {
                boolean success = transactionService.transferMoney(senderId, receiverId, amount);
                response.getWriter().println(success ? "Transfer successful" : "Transfer failed");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing transfer.");
            }
        }
    }
}
