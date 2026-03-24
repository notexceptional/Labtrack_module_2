package labtrack.main;

import java.util.Scanner;
import labtrack.auth.AuthService;
import labtrack.users.User;
import labtrack.util.Colors;
import labtrack.util.InputHelper;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHelper.setScanner(scanner);
        AuthService authService = new AuthService();

        printWelcomeBanner();

        while (true) {
            Colors.header("Login");
            User user = authService.login(scanner);
            if (user == null) {
                Colors.error("Login failed. Please try again.");
                continue;
            }

            Colors.success("Welcome, " + user.getUsername() + "!");

            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println();
                user.showMenu();
                System.out.println();
                System.out.println(Colors.colorize("----------------------------------------------", Colors.CYAN));
                System.out.println("  [" + Colors.colorize("0", Colors.YELLOW_BOLD) + "] Logout");
                System.out.println("  [" + Colors.colorize("999", Colors.RED_BOLD) + "] Exit");
                System.out.println(Colors.colorize("----------------------------------------------", Colors.CYAN));
                System.out.println();
                
                String choiceStr = InputHelper.readLine("Enter choice: ");
                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    Colors.error("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 0:
                        System.out.println();
                        Colors.success("Logged out successfully!");
                        System.out.println();
                        loggedIn = false;
                        break;
                    case 999:
                        System.out.println();
                        System.out.println(Colors.colorize("**************************************************", Colors.CYAN));
                        System.out.println(Colors.colorize("*      Thank you for using LABTRACK!            *", Colors.CYAN_BOLD));
                        System.out.println(Colors.colorize("**************************************************", Colors.CYAN));
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

    private static void printWelcomeBanner() {
        System.out.println(Colors.colorize("                                                        ", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize("  _           _      ____    _____   ____       _       ____   _  __", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize(" | |         / \\    | __ )  |_   _| |  _ \\     / \\     / ___| | |/ /", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize(" | |        / _ \\   |  _ \\    | |   | |_) |   / _ \\   | |     | ' / ", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize(" | |___    / ___ \\  | |_) |   | |   |  _ <   / ___ \\  | |___  | . \\ ", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize(" |_____|  /_/   \\_\\ |____/    |_|   |_| \\_\\ /_/   \\_\\  \\____| |_|\\_\\", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize("                                                                    ", Colors.BLUE_BOLD));
        System.out.println(Colors.colorize("           Research Laboratory Management System            ", Colors.CYAN));
        System.out.println();
    }
}
