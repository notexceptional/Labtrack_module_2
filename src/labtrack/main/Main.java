package labtrack.main;

import java.util.Scanner;
import labtrack.auth.AuthService;
import labtrack.users.User;
import labtrack.util.InputHelper;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InputHelper.setScanner(sc);
        AuthService auth = new AuthService();

        System.out.println("---------Welcome to LABTRACK---------");
        while (true) {
            User user = auth.login(sc);
            if (user == null) continue;

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("");
                user.showMenu();
                System.out.println("000. Logout");
                System.out.println("999. Exit");
                System.out.println("");
                int choice;
                try {
                    choice = Integer.parseInt(InputHelper.readLine("").trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }
                if (choice == 0) {
                    loggedIn = false;
                } else if (choice == 999) {
                    System.out.println("Exiting NOW. Thank you for using LABTRACK!");
                    sc.close();
                    System.exit(0);
                } else {
                    user.handleChoice(choice, sc);
                }
            }
        }
    }
}
