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
        if (console != null) {
            String input = console.readLine(prompt);
            if (input == null) {
                return "";
            }
            return input.trim();
        }
        System.out.print(prompt);

        String input = scanner.nextLine();
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    public static String readPassword(String prompt) {
        if (console != null) {
            char[] pass = console.readPassword(prompt);
            if (pass == null) {
                return "";
            }
            String password = new String(pass);
            return password.trim();
        }
        System.out.print(prompt);

        String password = scanner.nextLine();
        if (password == null) {
            return "";
        }
        return password.trim();
    }
}
