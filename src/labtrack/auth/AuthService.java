package labtrack.auth;

import labtrack.users.*;
import java.util.Scanner;

public class AuthService {
    private static final String PASSWORD = "123456";

    public User login() {
        Scanner sc = new Scanner(System.in);
        return login(sc);
    }

    public User login(Scanner sc) {

        System.out.print("Enter role (researcher/technician/labmanager/admin): ");
        String role = sc.next();

        System.out.print("Enter password: ");
        String pass = sc.next();

        if (!pass.equals(PASSWORD)) {
            System.out.println("Wrong password! Access denied.");
            return null;
        }


        switch (role.toLowerCase()) {
            case "researcher":
                return new Researcher("R1", "Shudipto");

            case "technician":
                return new Technician("T1", "Tiran");

            case "labmanager":
                return new LabManager("L1", "Rehan");

            case "admin":
                return new Admin("A1", "Mueeze Sir");

            default:
                System.out.println("Invalid role");
                return null;
        }
    }
}
