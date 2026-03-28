package labtrack.util;

import java.util.ArrayList;
import java.util.List;


public class TablePrinter {

    public static void printTable(String title, String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) return;


        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < Math.min(row.length, widths.length); i++) {
                if (row[i] != null && row[i].length() > widths[i]) {
                    widths[i] = row[i].length();
                }
            }
        }


        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w + 2)).append("+");
        }
        String separator = sb.toString();


        if (title != null && !title.isEmpty()) {
            Colors.header(title);
        }

        System.out.println(Colors.colorize(separator, Colors.CYAN));
        System.out.print(Colors.colorize("|", Colors.CYAN));
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(Colors.colorize(" %-" + widths[i] + "s ", Colors.YELLOW_BOLD) + Colors.colorize("|", Colors.CYAN), headers[i]);
        }
        System.out.println();
        System.out.println(Colors.colorize(separator, Colors.CYAN));


        if (rows.isEmpty()) {
            System.out.print(Colors.colorize("|", Colors.CYAN));
            int totalWidth = separator.length() - 2;
            System.out.printf(" %-" + totalWidth + "s ", "No data available");
            System.out.println(Colors.colorize("|", Colors.CYAN));
        } else {
            for (String[] row : rows) {
                System.out.print(Colors.colorize("|", Colors.CYAN));
                for (int i = 0; i < headers.length; i++) {
                    String val = (i < row.length) ? row[i] : "";
                    System.out.printf(" %-" + widths[i] + "s " + Colors.colorize("|", Colors.CYAN), val);
                }
                System.out.println();
            }
        }
        System.out.println(Colors.colorize(separator, Colors.CYAN));
        System.out.println();
    }

    public static void printCsvAsTable(String title, String fileName, String[] headers) {
        List<String> lines = FileManager.readAllLines(fileName);
        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            rows.add(line.split(","));
        }
        printTable(title, headers, rows);
    }
}
