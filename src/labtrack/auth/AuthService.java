package labtrack.auth;
import java.util.*;
import labtrack.users.*;
import labtrack.util.FileManager;

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
            System.out.print("Enter role (admin only): ");
            String role = sc.next();
            if (!role.equalsIgnoreCase("admin")) {
                System.out.println("Only admin can login .");
                return null;
            }
            String pass = readPasswordMasked(sc, "Enter password: ");
            if (!pass.equals(PASSWORD)) {
                System.out.println("Wrong password! Access denied.");
                return null;
            }
            return new Admin("A1", "Admin");
        }

        
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter role: ");
        String role = sc.next();
        String pass = readPasswordMasked(sc, "Enter password: ");

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

    private static String readPasswordMasked(Scanner sc, String prompt) {
        System.out.print(prompt);
        String pass = sc.next();
        System.out.print("\033[1A\r" + prompt + "*".repeat(pass.length()) + "\n");
        return pass;
    }
}
