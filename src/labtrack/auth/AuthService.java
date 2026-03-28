package labtrack.auth;

import java.util.List;
import java.util.Scanner;

import labtrack.users.*;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;

/**
 * Handles user authentication and role-based login for the system.
 * This class validates credentials against CSV data and manages initial admin setup.
 */
public class AuthService {
    private static final String PASSWORD = "123456";

    public User login() {
        return login(null);
    }

    public User login(Scanner sc) {
        List<String> users = FileManager.readAllLines("users.csv");
        boolean firstRun = users.isEmpty();

        // Initial setup for first-time use: handles mandatory Admin creation
        if (firstRun) {
            String role = InputHelper.readLine("Enter role (admin only): ");
            if (!role.equalsIgnoreCase("admin")) {
                Colors.error("Only admin can login.");
                return null;
            }
            String pass = InputHelper.readPassword("Enter password: ");
            if (!pass.equals(PASSWORD)) {
                Colors.error("Wrong password! Access denied.");
                return null;
            }
            return new Admin("A1", "Admin");
        }

        String username = InputHelper.readLine("Enter username: ");
        String role = InputHelper.readLine("Enter role: ");
        String pass = InputHelper.readPassword("Enter password: ");

        // Special handling for the main Admin role (static credentials)
        if (role.equalsIgnoreCase("admin")) {
            if (!pass.equals(PASSWORD)) {
                Colors.error("Wrong password! Access denied.");
                return null;
            }
            return new Admin("A1", username);
        }

        // Standard user lookup: iterates through registered users
        for (String line : users) {
            String[] parts = line.split(",");
            if (parts.length < 4) continue;
            String id = parts[0];
            String user = parts[1];
            String userRole = parts[2];
            String userPass = parts[3];
            if (user.equalsIgnoreCase(username) && userRole.equalsIgnoreCase(role) && userPass.equals(pass)) {

                System.out.println();
                System.out.println("  >>> Welcome, " + user + "! <<<");
                System.out.println();
                switch (userRole.toLowerCase()) {
                    case "researcher" -> { return new Researcher(id, user); }
                    case "technician" -> { return new Technician(id, user); }
                    case "labmanager" -> { return new LabManager(id, user); }
                    case "labassistant" -> { return new LabAssistant(id, user); }

                }
            }
        }
        Colors.error("User not found or credentials incorrect.");
        return null;
    }
}
