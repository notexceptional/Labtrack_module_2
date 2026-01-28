package labtrack.users;

import labtrack.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private static final String USERS_FILE = "users.csv";

    public Admin(String id, String name) {
        super(id, name, "Admin");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Create User");
        System.out.println("2. Delete User");
        System.out.println("3. View All Users");
        System.out.println("0. Logout");
        System.out.println("10. Exit");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        if (choice == 1) {
            createUser(sc);
            return;
        }
        if (choice == 2) {
            deleteUser(sc);
            return;
        }
        if (choice == 3) {
            viewAllUsers();
            return;
        }
        if (choice == 10) {
            System.out.println("Exiting application. Thank you for using Labtrack!");
            System.exit(0);
        }
        System.out.println("Invalid choice");
    }

    private void createUser(Scanner sc) {
        System.out.print("Enter new user ID (digits only): ");
        String id = sc.next();
        if (!id.matches("\\d+")) {
            System.out.println("User ID must be digits only.");
            return;
        }

        System.out.print("Enter username (letters only): ");
        String username = sc.next();
        if (!username.matches("[A-Za-z]+")) {
            System.out.println("Username must be letters only.");
            return;
        }

        
        List<String> lines = FileManager.readAllLines(USERS_FILE);
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length < 2) continue;
            if (p[0].equals(id)) {
                System.out.println("User ID already exists. User not created.");
                return;
            }
            if (p[1].equalsIgnoreCase(username)) {
                System.out.println("Username already exists. User not created.");
                return;
            }
        }

        System.out.print("Enter role (researcher/technician/labmanager): ");
        String role = sc.next();
        if (!isValidRole(role) || role.equalsIgnoreCase("admin")) {
            System.out.println("Invalid role. User not created.");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.next();
        if (password.length() < 4) {
            System.out.println("Password invalid. Must be at least 4 characters.");
            return;
        }

        FileManager.write(USERS_FILE, id + "," + username + "," + role.toLowerCase() + "," + password);
        System.out.println("User saved to " + USERS_FILE);
    }

    private void viewAllUsers() {
        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.println("===========All Users==========");
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length < 3) continue;
            System.out.println("ID: " + p[0] + ", Username: " + p[1] + ", Role: " + p[2]);
        }
    }

    private void deleteUser(Scanner sc) {
        System.out.print("Enter user ID to delete: ");
        String targetId = sc.next();

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            System.out.println("No users found in " + USERS_FILE);
            return;
        }

        List<String> kept = new ArrayList<>();
        boolean removed = false;
        for (String line : lines) {
            String[] parts = line.split(",", 3);
            if (parts.length >= 1 && parts[0].trim().equals(targetId)) {
                removed = true;
                continue;
            }
            kept.add(line);
        }

        if (!removed) {
            System.out.println("User ID not found.");
            return;
        }

        FileManager.overwrite(USERS_FILE, kept);
        System.out.println("User deleted from " + USERS_FILE);
    }

    private boolean isValidRole(String role) {
        if (role == null) return false;
        switch (role.toLowerCase()) {
            case "researcher":
            case "technician":
            case "labmanager":
            case "admin":
                return true;
            default:
                return false;
        }
    }
}