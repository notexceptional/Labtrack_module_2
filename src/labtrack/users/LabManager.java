package labtrack.users;

import labtrack.booking.BookingService;
import labtrack.reports.ReportService;

import java.util.Scanner;

public class LabManager extends User {
    public LabManager(String id, String name) {
        super(id, name, "LabManager");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Approve Reservation\n2. View Reports\n0. Logout");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        BookingService bookingService = new BookingService();
        ReportService reportService = new ReportService();

        if (choice == 1) {
            bookingService.listPending();
            bookingService.approveById(sc);
            return;
        }
        if (choice == 2) {
            reportService.printSummaryReport();
            return;
        }
        System.out.println("Invalid choice");
    }
}