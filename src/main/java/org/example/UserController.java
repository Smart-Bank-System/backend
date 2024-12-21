package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Handles user-related endpoints like login and registration.
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/login".equals(action)) {
            String tckn = request.getParameter("tckn");
            String password = request.getParameter("password");
            try {
                boolean success = userService.loginUser(tckn, password);
                response.getWriter().println(success ? "Login successful" : "Invalid credentials");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during login.");
            }
        } else if ("/register".equals(action)) {
            String fullName = request.getParameter("fullName");
            String tckn = request.getParameter("tckn");
            String password = request.getParameter("password");
            try {
                userService.registerUser(fullName, tckn, password);
                response.getWriter().println("Registration successful");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during registration.");
            }
        }
    }
}
