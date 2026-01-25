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
        System.out.println("1. Create User\n2. Delete User\n0. Logout");
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

        System.out.println("Invalid choice");
    }

    private void createUser(Scanner sc) {
        System.out.print("Enter new user ID: ");
        String id = sc.next();

        System.out.print("Enter username: ");
        String username = sc.next();

        System.out.print("Enter role (researcher/technician/labmanager/admin): ");
        String role = sc.next();

        if (!isValidRole(role)) {
            System.out.println("Invalid role. User not created.");
            return;
        }

        FileManager.write(USERS_FILE, id + "," + username + "," + role.toLowerCase());
        System.out.println("User saved to " + USERS_FILE);
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