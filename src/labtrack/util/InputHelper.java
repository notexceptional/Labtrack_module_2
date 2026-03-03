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
            return input != null ? input.trim() : "";
        }
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readPassword(String prompt) {
        if (console != null) {
            char[] pass = console.readPassword(prompt);
            return pass != null ? new String(pass).trim() : "";
        }
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
