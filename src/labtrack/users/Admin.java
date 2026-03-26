package labtrack.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class Admin extends User {
    private static final String USERS_FILE = "users.csv";

    public Admin(String id, String name) {
        super(id, name, "Admin");
    }

    @Override
    public void showMenu() {
        System.out.println("+----------------------------------------------+");
        System.out.println("|               ADMIN PANEL                    |");
        System.out.println("+----------------------------------------------+");
        System.out.println();
        System.out.println("  ~~~ User Management ~~~");
        System.out.println("  [1] Create User");
        System.out.println("  [2] Delete User");
        System.out.println("  [3] View All Users");
        System.out.println("  [4] Update User Password");
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
        if (choice == 4) {
            updatePassword(sc);
            return;
        }
        System.out.println("  [ERROR] Invalid choice");
    }

    private void createUser(Scanner sc) {
        String id = InputHelper.readLine("Enter new user ID (digits only): ");
        if (!id.matches("\\d+")) {
            System.out.println("  [ERROR] User ID must be digits only.");
            return;
        }

        String username = InputHelper.readLine("Enter username (letters only): ");
        if (!username.matches("[A-Za-z]+")) {
            System.out.println("  [ERROR] Username must be letters only.");
            return;
        }

        
        List<String> lines = FileManager.readAllLines(USERS_FILE);
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length < 2) continue;
            if (p[0].equals(id)) {
                System.out.println("  [ERROR] User ID already exists. User not created.");
                return;
            }
        }

        String role = InputHelper.readLine("Enter role (researcher/technician/labmanager/labassistant): ");
        if (!isValidRole(role) || role.equalsIgnoreCase("admin")) {
            System.out.println("  [ERROR] Invalid role. User not created.");
            return;
        }

        String password = InputHelper.readPassword("Enter password: ");
        if (password.length() < 4) {
            System.out.println("  [ERROR] Password invalid. Must be at least 4 characters.");
            return;
        }

        FileManager.write(USERS_FILE, id + "," + username + "," + role.toLowerCase() + "," + password);
        System.out.println();
        System.out.println("  >>> User created successfully! <<<");
        System.out.println();
    }

    private void viewAllUsers() {
        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            System.out.println("  [!] No users found.");
            return;
        }
        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|                ALL USERS                     |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length < 3) continue;
            System.out.println("+------------------------------------------+");
            System.out.println("| ID:       " + p[0]);
            System.out.println("| Username: " + p[1]);
            System.out.println("| Role:     " + p[2]);
            System.out.println("+------------------------------------------+");
        }
    }

    private void deleteUser(Scanner sc) {
        String targetId = InputHelper.readLine("Enter user ID to delete: ");

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            System.out.println("  [!] No users found.");
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
            System.out.println("  [ERROR] User ID not found.");
            return;
        }

        FileManager.overwrite(USERS_FILE, kept);
        System.out.println();
        System.out.println("  >>> User deleted successfully! <<<");
        System.out.println();
    }

    private void updatePassword(Scanner sc) {
        viewAllUsers();
        String targetId = InputHelper.readLine("Enter user ID to update password: ");

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            System.out.println("  [!] No users found.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",", 4);
            if (parts.length < 4) continue;
            if (parts[0].trim().equals(targetId)) {
                found = true;
                String newPassword = InputHelper.readPassword("Enter new password: ");
                if (newPassword.length() < 4) {
                    System.out.println("  [ERROR] Password must be at least 4 characters.");
                    return;
                }
                lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + newPassword);
                break;
            }
        }

        if (!found) {
            System.out.println("  [ERROR] User ID not found.");
            return;
        }

        FileManager.overwrite(USERS_FILE, lines);
        System.out.println();
        System.out.println("  >>> Password updated successfully! <<<");
        System.out.println();
    }

    private boolean isValidRole(String role) {
        if (role == null) return false;
        switch (role.toLowerCase()) {
            case "researcher":
            case "technician":
            case "labmanager":
            case "labassistant":
            case "admin":
                return true;
            default:
                return false;
        }
    }
}