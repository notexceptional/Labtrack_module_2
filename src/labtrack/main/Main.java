package labtrack.main;

import labtrack.auth.AuthService;
import labtrack.users.User;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            User user = auth.login(sc);
            if (user == null) continue;

            boolean loggedIn = true;
            while (loggedIn) {
                user.showMenu();
                int choice = 10;
                try {
                    choice = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.");
                    sc.nextLine();
                    continue;
                }
                sc.nextLine();
                if (choice == 4) {
                    loggedIn = false;
                } else {
                    user.handleChoice(choice, sc);

                }

            }

        }
    }
}
