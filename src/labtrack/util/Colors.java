package labtrack.util;


public class Colors {

    public static final String RESET = "\033[0m";


    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";


    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";



    public static String colorize(String text, String color) {
        return color + text + RESET;
    }

    public static void print(String text, String color) {
        System.out.print(colorize(text, color));
    }

    public static void println(String text, String color) {
        System.out.println(colorize(text, color));
    }


    public static void error(String msg) {
        System.out.println(colorize("  [ERROR] " + msg, RED_BOLD));
    }


    public static void success(String msg) {
        System.out.println(colorize("  >>> " + msg + " <<<", GREEN_BOLD));
    }

    public static void warning(String msg) {
        System.out.println(colorize("  [!] " + msg, YELLOW_BOLD));
    }


    public static void header(String title) {
        String separator = "+----------------------------------------------+";
        System.out.println(colorize(separator, CYAN));
        System.out.printf(colorize("| %-44s |\n", CYAN), title.toUpperCase());
        System.out.println(colorize(separator, CYAN));
    }
}
