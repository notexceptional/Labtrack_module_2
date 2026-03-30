package labtrack.reports;

import java.util.ArrayList;
import java.util.List;
import labtrack.util.FileManager;
import labtrack.util.TablePrinter;
import labtrack.util.InputHelper;

/**
 * Service class for generating summarized reports about the laboratory system.
 * This class aggregates data from experiments, inventory, and bookings 
 * to provide a high-level overview for managers.
 */
public class ReportService {
    private static final String EXPERIMENTS_FILE = "experiments.csv";
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String PENDING_BOOKINGS_FILE = "bookings_pending.csv";
    private static final String APPROVED_BOOKINGS_FILE = "bookings_approved.csv";

    /**
     * Generates and prints a summary report of the system's current state.
     * This includes counts of experiments, inventory items, and booking statuses.
     */
    public void printSummaryReport() {
        int experiments = countLines(EXPERIMENTS_FILE);
        int inventoryItems = countLines(INVENTORY_FILE);
        int pendingBookings = countLines(PENDING_BOOKINGS_FILE);
        int approvedBookings = countLines(APPROVED_BOOKINGS_FILE);

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Experiments Saved", String.valueOf(experiments)});
        rows.add(new String[]{"Inventory Items", String.valueOf(inventoryItems)});
        rows.add(new String[]{"Bookings Pending", String.valueOf(pendingBookings)});
        rows.add(new String[]{"Bookings Approved", String.valueOf(approvedBookings)});

        String[] headers = {"Metric", "Count"};
        TablePrinter.printTable("System Summary Report", headers, rows);
        InputHelper.pressEnterToContinue();
    }

    /**
     * Counts the number of lines in a given file, which typically represents the number of records.
     * @param file The path to the file to count lines from.
     * @return The number of lines in the file.
     */
    private int countLines(String file) {
        List<String> lines = FileManager.readAllLines(file);
        return lines.size();
    }
}
