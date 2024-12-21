package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("SmartBank System Starting...");

        try {
            System.out.println("Testing database connection...");
            if (DatabaseConnection.getConnection() != null) {
                System.out.println("Database connection successful!");
            }

            UserService userService = new UserService();
            AccountService accountService = new AccountService();
            TransactionService transactionService = new TransactionService();

            System.out.println("Registering a new user (Admin)...");
            try {
                userService.registerUser("Admin User", "11111111110", "adminpass", "admin");
                System.out.println("Admin user registered successfully.");
            } catch (Exception e) {
                System.err.println("Admin user already exists: " + e.getMessage());
            }

            System.out.println("Registering a new user (Customer)...");
            try {
                userService.registerUser("John Doe", "22222222220", "userpass", "user");
                System.out.println("Customer registered successfully.");
            } catch (Exception e) {
                System.err.println("Customer already exists: " + e.getMessage());
            }

            // Simulating user login
            String role = userService.loginUser("22222222220", "userpass");
            if ("user".equals(role)) {
                System.out.println("User login successful!");
                accountService.createAccount("John Doe", 1000.0);
                System.out.println("Account created for John Doe.");

                System.out.println("Depositing money...");
                accountService.depositMoney(1, 500.0);
                System.out.println("New balance: " + accountService.getAccountBalance(1));

                System.out.println("Withdrawing money...");
                boolean withdrawSuccess = accountService.withdrawMoney(1, 200.0);
                System.out.println(withdrawSuccess ? "Withdrawal successful." : "Withdrawal failed.");

                System.out.println("Transferring money...");
                transactionService.transferMoney(1, 2, 300.0);
                System.out.println("Transfer complete.");
            }

            // Simulating admin actions
            role = userService.loginUser("11111111110", "adminpass");
            if ("admin".equals(role)) {
                System.out.println("Admin login successful!");

                System.out.println("Viewing pending accounts...");
                accountService.getPendingAccounts().forEach(System.out::println);

                System.out.println("Viewing large transactions...");
                transactionService.getLargeTransactions().forEach(System.out::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("SmartBank System shutting down...");
    }
}
