package labtrack.reports;

import java.util.List;
import labtrack.util.FileManager;

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

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|              SYSTEM REPORT                   |");
        System.out.println("+----------------------------------------------+");
        System.out.println("+------------------------------------------+");
        System.out.println("| Experiments Saved:    " + experiments);
        System.out.println("| Inventory Items:      " + inventoryItems);
        System.out.println("| Bookings Pending:     " + pendingBookings);
        System.out.println("| Bookings Approved:    " + approvedBookings);
        System.out.println("+------------------------------------------+");
    }

    private int countLines(String file) {
        List<String> lines = FileManager.readAllLines(file);
        int count = lines.size();
        return count;
    }
}
