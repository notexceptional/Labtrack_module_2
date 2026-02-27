package labtrack.main;

import java.util.Scanner;
import labtrack.auth.AuthService;
import labtrack.users.User;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        System.out.println("---------Welcome to LABTRACK---------");
        while (true) {
            User user = auth.login(sc);
            if (user == null) continue;

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("");
                user.showMenu();
                System.out.println("4. Logout");
                System.out.println("5. Exit");
                System.out.println("");
                int choice;
                try {
                    choice = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.");
                    if (sc.hasNextLine()) {
                        sc.nextLine();
                    } else {
                        System.out.println("No input available. Exiting.");
                        System.exit(0);
                    }
                    continue;
                }
                if (sc.hasNextLine()) {
                    sc.nextLine();
                }
                if (choice == 4) {
                    loggedIn = false;
                } else if (choice == 5) {
                    System.out.println("Exiting NOW.Thank you for using Labtrack!");
                    sc.close();
                    System.exit(0);
                } else {
                    user.handleChoice(choice, sc);
                }
            }

        }
    }
}
