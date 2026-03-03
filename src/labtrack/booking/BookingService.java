package labtrack.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class BookingService {
    private static final String PENDING_FILE = "bookings_pending.csv";
    private static final String APPROVED_FILE = "bookings_approved.csv";

    public void listPending() {
        List<String> lines = FileManager.readAllLines(PENDING_FILE);
        if (lines.isEmpty()) {
            System.out.println("No pending reservations.");
            return;
        }

        System.out.println("=== Pending Reservations ===");
        for (String line : lines) {
            Booking b = Booking.fromString(line);
            if (b == null) continue;

            System.out.println("Booking ID: " + b.getBookingID());
            System.out.println("Room: " + b.getRoomID());
            System.out.println("Date: " + b.getDateString());
            System.out.println("Time: " + b.getStartTimeString() + " - " + b.getEndTimeString());
            System.out.println("--------------------");
        }
    }
    public void approveById(Scanner sc) {
        List<String> lines = FileManager.readAllLines(PENDING_FILE);
        if (lines.isEmpty()) {
            System.out.println("No pending reservations.");
            return;
        }

        String confirm = InputHelper.readLine("Approve (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        String targetId = InputHelper.readLine("Enter Booking ID to approve: ");
        if (targetId.isEmpty()) {
            System.out.println("Invalid booking ID.");
            return;
        }

        List<String> kept = new ArrayList<>();
        String approvedLine = null;

        for (String line : lines) {
            Booking b = Booking.fromString(line);
            if (b == null) {
                kept.add(line);
                continue;
            }

            if (b.getBookingID() != null && b.getBookingID().trim().equals(targetId)) {
                approvedLine = line;
            } else {
                kept.add(line);
            }
        }

        if (approvedLine == null) {
            System.out.println("Booking ID not found in pending list.");
            return;
        }

        FileManager.overwrite(PENDING_FILE, kept);
        FileManager.write(APPROVED_FILE, approvedLine);
        System.out.println("Reservation approved.");
    }

    public int countPending() {
        return FileManager.readAllLines(PENDING_FILE).size();
    }

    public int countApproved() {
        return FileManager.readAllLines(APPROVED_FILE).size();
    }

    public void makeReservation(Scanner sc) {
        String bookingID = InputHelper.readLine("Enter Booking ID: ");
        String date = InputHelper.readLine("Enter Date (yyyy-MM-dd): ");
        String startTime = InputHelper.readLine("Enter Start Time (HH:mm): ");
        String endTime = InputHelper.readLine("Enter End Time (HH:mm): ");
        String roomID = InputHelper.readLine("Enter Room ID: ");

        if (bookingID.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || roomID.isEmpty()) {
            System.out.println("All fields are required. Reservation not created.");
            return;
        }

        Booking booking = new Booking(bookingID, date, startTime, endTime, roomID);
        FileManager.write(PENDING_FILE, booking.toString());
        System.out.println("Reservation submitted for approval.");
    }
}
