package labtrack.users;

import java.util.Scanner;
import labtrack.booking.BookingService;
import labtrack.inventory.ItemService;
import labtrack.reports.ReportService;

import labtrack.util.Colors;

/**
 * Represents a Laboratory Manager.
 * Managers oversee high-level operations, approve specialized item requests,
 * generate system reports, and manage facility reservations.
 */
public class LabManager extends User {
    /**
     * Constructs a new LabManager user.
     * @param id The unique identifier for the lab manager.
     * @param name The name of the lab manager.
     */
    public LabManager(String id, String name) {
        super(id, name, "LabManager");
    }

    @Override
    public void showMenu() {
        Colors.header("Lab Manager Panel");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Reservations ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("1", Colors.YELLOW_BOLD) + "] Approve Reservation");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Reports ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("2", Colors.YELLOW_BOLD) + "] Generate Report");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Item Requests ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("3", Colors.YELLOW_BOLD) + "] View Item Requests");
        System.out.println("  [" + Colors.colorize("4", Colors.YELLOW_BOLD) + "] Approve Item Request");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        BookingService bookingService = new BookingService();
        ReportService reportService = new ReportService();
        ItemService itemService = new ItemService();

        switch (choice) {
            case 1:
                bookingService.listPending();
                bookingService.approveById(sc);
                break;
            case 2:
                reportService.printSummaryReport();
                break;
            case 3:
                itemService.viewAllRequests();
                break;
            case 4:
                itemService.approveRequest(sc);
                break;
            default:
                break;
        }
    }
}