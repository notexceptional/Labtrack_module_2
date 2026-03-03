package labtrack.auth;
import java.util.*;
import labtrack.users.*;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class AuthService {
    private static final String PASSWORD = "123456";

    public User login() {
        Scanner sc = new Scanner(System.in);
        return login(sc);
    }

    public User login(Scanner sc) {
        List<String> users = FileManager.readAllLines("users.csv");
        boolean firstRun = users.isEmpty();

        if (firstRun) {
            String role = InputHelper.readLine("Enter role (admin only): ");
            if (!role.equalsIgnoreCase("admin")) {
                System.out.println("Only admin can login.");
                return null;
            }
            String pass = InputHelper.readPassword("Enter password: ");
            if (!pass.equals(PASSWORD)) {
                System.out.println("Wrong password! Access denied.");
                return null;
            }
            return new Admin("A1", "Admin");
        }

        String username = InputHelper.readLine("Enter username: ");
        String role = InputHelper.readLine("Enter role: ");
        String pass = InputHelper.readPassword("Enter password: ");

        if (role.equalsIgnoreCase("admin")) {
            if (!pass.equals(PASSWORD)) {
                System.out.println("Wrong password! Access denied.");
                return null;
            }
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
                switch (userRole.toLowerCase()) {
                    case "researcher" -> { return new Researcher(id, user); }
                    case "technician" -> { return new Technician(id, user); }
                    case "labmanager" -> { return new LabManager(id, user); }
                }
            }
        }
        System.out.println("User not found or credentials incorrect.");
        return null;
    }
}