package labtrack.reports;

import java.util.ArrayList;
import java.util.List;
import labtrack.util.FileManager;
import labtrack.util.TablePrinter;
import labtrack.util.InputHelper;

public class ReportService {
    private static final String EXPERIMENTS_FILE = "experiments.csv";
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String PENDING_BOOKINGS_FILE = "bookings_pending.csv";
    private static final String APPROVED_BOOKINGS_FILE = "bookings_approved.csv";

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

    private int countLines(String file) {
        List<String> lines = FileManager.readAllLines(file);
        return lines.size();
    }
}
