package labtrack.reports;

import labtrack.util.FileManager;

import java.util.List;

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

        System.out.println("=== Reports ===");
        System.out.println("Experiments (saved): " + experiments);
        System.out.println("Inventory items (saved): " + inventoryItems);
        System.out.println("Bookings pending: " + pendingBookings);
        System.out.println("Bookings approved: " + approvedBookings);
    }

    private int countLines(String file) {
        List<String> lines = FileManager.readAllLines(file);
        return lines.size();
    }
}
