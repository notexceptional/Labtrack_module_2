package labtrack.util;

import java.io.Console;
import java.util.Scanner;

/**
 * Central input handling utility for standardized terminal prompts.
 * Manages Scanner lifecycle and provides helpers for masked password entry
 * using the system console when available.
 */
public class InputHelper {
    private static Console console = System.console();
    private static Scanner scanner;

    /**
     * Initializes the shared Scanner instance.
     */
    public static void setScanner(Scanner sc) {
        scanner = sc;
    }

    /**
     * Reads a line of input from the console with a colored prompt.
     * Uses System.console() for secure handling if available,
     * otherwise falls back to standard System.in via Scanner.
     * @param prompt The prompt message to display.
     * @return The trimmed input string.
     */
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

    /**
     * Reads a password from the console with masking.
     * Uses System.console().readPassword() for secure, masked input if available.
     * Falls back to standard line reading if Console is unavailable.
     * @param prompt The prompt message to display.
     * @return The trimmed password string.
     */
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

    /**
     * Utility to pause execution and wait for user acknowledgment.
     */
    public static void pressEnterToContinue() {
        System.out.println(Colors.colorize("\nPress Enter to continue...", Colors.CYAN));
        if (scanner != null && scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
