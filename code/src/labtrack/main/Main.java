package labtrack.main;

import java.util.Scanner;
import labtrack.auth.AuthService;
import labtrack.users.User;
import labtrack.util.Colors;
import labtrack.util.InputHelper;

/**
 * Main entry point for the LABTRACK Research Laboratory Management System.
 * This class handles the primary application loop, user authentication,
 * and navigation between different user role dashboards.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InputHelper.setScanner(sc);
        AuthService auth = new AuthService();

        printWelcomeBanner();


        while (true) {
            User user = auth.login(sc);

            if (user == null) {
                System.out.println("  [!] Login failed. Please try again.");
                continue;
            }

            System.out.println("\n  >>> Welcome, " + user.getName() + " (" + user.getRole() + ") <<<");


            Colors.success("Welcome, " + user.getName() + "!");

            // Session loop: remains active until the user logs out or exits
            boolean loggedIn = true;
            while (loggedIn) {
                user.showMenu();

                System.out.println();

                System.out.println("  [0]   Logout");
                System.out.println("  [999] Exit System");
                System.out.println("----------------------------------------------");

                String input = InputHelper.readLine("Select an option: ");

                int choice;

                try {

                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("  [ERROR] Please enter a valid numerical option.");
                    continue;
                }

                if (choice == 0) {
                    System.out.println("\n  >>> Logging out... See you soon! <<<\n");
                    loggedIn = false;
                } else if (choice == 999) {
                    printExitMessage();
                    sc.close();
                    System.exit(0);
                } else {

                    user.handleChoice(choice, sc);

                }
            }
        }
    }

    private static void printWelcomeBanner() {

        System.out.println("\n**************************************************");
        System.out.println("* *");
        System.out.println("* Welcome to LABTRACK v2.0             *");
        System.out.println("* Research & Sample Management System      *");
        System.out.println("* *");
        System.out.println("**************************************************\n");
    }

    private static void printExitMessage() {
        System.out.println("\n**************************************************");
        System.out.println("* Thank you for using LABTRACK!            *");
        System.out.println("* System Shutting Down...               *");
        System.out.println("**************************************************\n");
    }
}
