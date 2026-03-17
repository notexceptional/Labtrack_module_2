package labtrack.main;

import java.util.Scanner;
import labtrack.auth.AuthService;
import labtrack.users.User;
import labtrack.util.InputHelper;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHelper.setScanner(scanner);
        AuthService authService = new AuthService();

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("*                                                *");
        System.out.println("*           Welcome to LABTRACK                  *");
        System.out.println("*                                                *");
        System.out.println("**************************************************");
        System.out.println();
        while (true) {
            User user = authService.login(scanner);
            if (user == null) {
                continue;
            }

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println();
                user.showMenu();
                System.out.println();
                System.out.println("----------------------------------------------");
                System.out.println("  [0] Logout");
                System.out.println("  [999] Exit");
                System.out.println("----------------------------------------------");
                System.out.println();
                int choice;
                try {
                    choice = Integer.parseInt(InputHelper.readLine(""));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 0:
                        System.out.println();
                        System.out.println(">>> Logged out successfully! <<<");
                        System.out.println();
                        loggedIn = false;
                        break;
                    case 999:
                        System.out.println();
                        System.out.println("**************************************************");
                        System.out.println("*      Thank you for using LABTRACK!            *");
                        System.out.println("**************************************************");
                        System.out.println();
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        user.handleChoice(choice, scanner);
                        break;
                }
            }

        }
    }
}
