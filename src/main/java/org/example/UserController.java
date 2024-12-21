package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles user-related endpoints like login and registration.
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();

        try {
            if ("/login".equals(action)) {
                String tckn = request.getParameter("tckn");
                String password = request.getParameter("password");
                String role = userService.loginUser(tckn, password);

                if (role != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("role", role);
                    response.getWriter().println("Login successful as " + role + ".");
                } else {
                    response.getWriter().println("Invalid credentials.");
                }
            } else if ("/register".equals(action)) {
                String fullName = request.getParameter("fullName");
                String tckn = request.getParameter("tckn");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                userService.registerUser(fullName, tckn, password, role);
                response.getWriter().println("Registration successful.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
