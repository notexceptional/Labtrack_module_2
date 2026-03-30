package labtrack.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;
import labtrack.util.TablePrinter;


public class Admin extends User {
    private static final String USERS_FILE = "users.csv";

    public Admin(String id, String name) {
        super(id, name, "Admin");
    }

    @Override
    public void showMenu() {
        Colors.header("Admin Panel");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ User Management ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("1", Colors.YELLOW_BOLD) + "] Create User");
        System.out.println("  [" + Colors.colorize("2", Colors.YELLOW_BOLD) + "] Delete User");
        System.out.println("  [" + Colors.colorize("3", Colors.YELLOW_BOLD) + "] View All Users");
        System.out.println("  [" + Colors.colorize("4", Colors.YELLOW_BOLD) + "] Update User Password");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        switch (choice) {
            case 1:
                createUser();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                viewAllUsers();
                break;
            case 4:
                updatePassword();
                break;
            default:
                Colors.error("Invalid choice");
                break;
        }
    }

    private void createUser() {
        String id = InputHelper.readLine("Enter new user ID (digits only): ");
        if (!id.matches("\\d+")) {
            Colors.error("User ID must be digits only.");
            return;
        }

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        for (String line : lines) {
            String[] p = line.split(",");
            if (p.length < 2) continue;
            if (p[0].equals(id)) {
                Colors.error("User ID already exists. User not created.");
                return;
            }
        }

        String newUsername = InputHelper.readLine("Enter username (letters only): ");
        if (!newUsername.matches("[A-Za-z]+")) {
            Colors.error("Username must be letters only.");
            return;
        }


        String role = InputHelper.readLine("Enter role (researcher/technician/labmanager/labassistant): ");
        if (!isValidRole(role) || role.equalsIgnoreCase("admin")) {
            System.out.println("  [ERROR] Invalid role. User not created.");

            return;
        }

        String newPassword = InputHelper.readPassword("Enter password: ");
        if (newPassword.length() < 4) {
            Colors.error("Password invalid. Must be at least 4 characters.");
            return;
        }

        FileManager.write(USERS_FILE, id + "," + newUsername + "," + role.toLowerCase() + "," + newPassword);
        System.out.println();
        Colors.success("User created successfully!");
        System.out.println();
    }

    private void viewAllUsers() {
        String[] headers = {"ID", "Username", "Role"};
        TablePrinter.printCsvAsTable("All Users", USERS_FILE, headers);
        InputHelper.pressEnterToContinue();
    }

    private void deleteUser() {
        String targetId = InputHelper.readLine("Enter user ID to delete: ");

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No users found.");
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
            Colors.error("User ID not found.");
            return;
        }

        FileManager.overwrite(USERS_FILE, kept);
        System.out.println();
        Colors.success("User deleted successfully!");
        System.out.println();
    }

    private void updatePassword() {
        String targetId = InputHelper.readLine("Enter user ID to update password: ");

        List<String> lines = FileManager.readAllLines(USERS_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No users found.");
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
                    Colors.error("Password must be at least 4 characters.");
                    return;
                }
                lines.set(i, parts[0] + "," + parts[1] + "," + parts[2] + "," + newPassword);
                break;
            }
        }

        if (!found) {
            Colors.error("User ID not found.");
            return;
        }

        FileManager.overwrite(USERS_FILE, lines);
        System.out.println();
        Colors.success("Password updated successfully!");
        System.out.println();
    }

    private boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }

        String normalizedRole = role.toLowerCase();
        switch (normalizedRole) {
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