package labtrack.auth;

import java.util.List;
import java.util.Scanner;
import labtrack.users.Admin;
import labtrack.users.LabManager;
import labtrack.users.Researcher;
import labtrack.users.Technician;
import labtrack.users.User;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class AuthService {
    private static final String PASSWORD = "123456";

    public User login() {
        return login(null);
    }

    public User login(Scanner sc) {
        List<String> users = FileManager.readAllLines("users.csv");
        boolean firstRun = users.isEmpty();

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|                    LOGIN                     |");
        System.out.println("+----------------------------------------------+");

        if (firstRun) {
            String role = InputHelper.readLine("Enter role (admin only): ");
            if (!role.equalsIgnoreCase("admin")) {
                System.out.println();
                System.out.println("  [ERROR] Only admin can login.");
                System.out.println();
                return null;
            }
            String pass = InputHelper.readPassword("Enter password: ");
            if (!pass.equals(PASSWORD)) {
                System.out.println();
                System.out.println("  [ERROR] Wrong password! Access denied.");
                System.out.println();
                return null;
            }
            System.out.println();
            System.out.println("  >>> Welcome, Admin! <<<");
            System.out.println();
            return new Admin("A1", "Admin");
        }

        String username = InputHelper.readLine("Enter username: ");
        String role = InputHelper.readLine("Enter role: ");
        String pass = InputHelper.readPassword("Enter password: ");

        if (role.equalsIgnoreCase("admin")) {
            if (!pass.equals(PASSWORD)) {
                System.out.println();
                System.out.println("  [ERROR] Wrong password! Access denied.");
                System.out.println();
                return null;
            }
            System.out.println();
            System.out.println("  >>> Welcome, " + username + "! <<<");
            System.out.println();
            return new Admin("A1", username);
        }

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
                String normalizedRole = userRole.toLowerCase();
                if (normalizedRole.equals("researcher")) {
                    return new Researcher(id, user);
                }
                if (normalizedRole.equals("technician")) {
                    return new Technician(id, user);
                }
                if (normalizedRole.equals("labmanager")) {
                    return new LabManager(id, user);
                }
            }
        }
        System.out.println();
        System.out.println("  [ERROR] User not found or credentials incorrect.");
        System.out.println();
        return null;
    }
}
