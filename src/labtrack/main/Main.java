package labtrack.main;

import labtrack.auth.AuthService;
import labtrack.users.User;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            User user = auth.login();
            if (user == null) continue;

            boolean loggedIn = true;
            while (loggedIn) {
                user.showMenu();
                int choice = sc.nextInt();
                if (choice == 0) loggedIn = false;
            }
        }
    }
}
//Need to work on the exit-Tiran