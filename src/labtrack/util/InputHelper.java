package labtrack.util;

import java.io.Console;
import java.util.Scanner;

public class InputHelper {
    private static Console console = System.console();
    private static Scanner scanner;

    public static void setScanner(Scanner sc) {
        scanner = sc;
    }

    public static String readLine(String prompt) {
        String coloredPrompt = Colors.colorize(prompt, Colors.YELLOW_BOLD);
        if (console != null) {
            String input = console.readLine(coloredPrompt);
            if (input == null) {
                return "";
            }
            return input.trim();
        }
        System.out.print(coloredPrompt);

        if (!scanner.hasNextLine()) return "";
        String input = scanner.nextLine();
        return input.trim();
    }

    public static String readPassword(String prompt) {
        String coloredPrompt = Colors.colorize(prompt, Colors.YELLOW_BOLD);
        if (console != null) {
            char[] pass = console.readPassword(coloredPrompt);
            if (pass == null) {
                return "";
            }
            String password = new String(pass);
            return password.trim();
        }
        System.out.print(coloredPrompt);

        if (!scanner.hasNextLine()) return "";
        String password = scanner.nextLine();
        return password.trim();
    }

    public static void pressEnterToContinue() {
        System.out.println(Colors.colorize("\nPress Enter to continue...", Colors.CYAN));
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
