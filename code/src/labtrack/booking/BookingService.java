package labtrack.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.labroom.LabRoom;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;
import labtrack.util.TablePrinter;

/**
 * Service class for managing laboratory room reservations.
 * This class handles booking requests, pending approval queues, 
 * and finalized reservation records.
 */
public class BookingService {
    private static final String PENDING_FILE = "bookings_pending.csv";
    private static final String APPROVED_FILE = "bookings_approved.csv";
    private static final String LAB_ROOMS_FILE = "lab_rooms.csv";

    public void listPending() {
        List<String> lines = FileManager.readAllLines(PENDING_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No pending reservations.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            Booking booking = Booking.fromString(line);
            if (booking == null) continue;

            rows.add(new String[]{
                booking.getBookingID(),
                booking.getRoomID(),
                booking.getDateString(),
                booking.getStartTimeString() + " - " + booking.getEndTimeString(),
                booking.getBookedBy()
            });
        }

        String[] headers = {"ID", "Room", "Date", "Duration", "Booked By"};
        TablePrinter.printTable("Pending Reservations", headers, rows);
    }

    public void approveById(Scanner sc) {
        List<String> lines = FileManager.readAllLines(PENDING_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No pending reservations.");
            return;
        }

        String confirm = InputHelper.readLine("Approve (Y/N): ");
        boolean confirmed = confirm.equalsIgnoreCase("Y");
        if (!confirmed) {
            return;
        }

        String targetId = InputHelper.readLine("Enter Booking ID to approve: ");
        if (targetId.isEmpty()) {
            Colors.error("Invalid booking ID.");
            return;
        }

        List<String> kept = new ArrayList<>();
        String approvedLine = null;

        for (String line : lines) {
            Booking booking = Booking.fromString(line);
            if (booking == null) {
                kept.add(line);
                continue;
            }

            String bookingId = booking.getBookingID();
            boolean matchesTarget = bookingId != null && bookingId.trim().equals(targetId);

            if (matchesTarget) {
                approvedLine = line;
            } else {
                kept.add(line);
            }
        }

        if (approvedLine == null) {
            Colors.error("Booking ID not found in pending list.");
            return;
        }

        FileManager.overwrite(PENDING_FILE, kept);
        FileManager.write(APPROVED_FILE, approvedLine);

        Booking approved = Booking.fromString(approvedLine);
        if (approved != null) {
            LabRoom labRoom = new LabRoom(
                approved.getRoomID(),
                approved.getBookingID(),
                approved.getBookedBy(),
                approved.getDateString()
            );
            FileManager.write(LAB_ROOMS_FILE, labRoom.toString());
            System.out.println();
            Colors.success("Lab Room " + approved.getRoomID() + " is RESERVED!");
        }
        System.out.println();
        Colors.success("Reservation approved successfully!");
        System.out.println();
    }

    public int countPending() {
        List<String> lines = FileManager.readAllLines(PENDING_FILE);
        return lines.size();
    }

    public int countApproved() {
        List<String> lines = FileManager.readAllLines(APPROVED_FILE);
        return lines.size();
    }

    /**
     * Researchers use this to submit a new room reservation request for approval.
     */
    public void makeReservation(Scanner sc, String username) {
        String bookingID = InputHelper.readLine("Enter Booking ID(use last 3 digits from your userID): ");
        String date = InputHelper.readLine("Enter Date (yyyy-MM-dd): ");
        String startTime = InputHelper.readLine("Enter Start Time (HH:mm): ");
        String endTime = InputHelper.readLine("Enter End Time (HH:mm): ");
        String roomID = InputHelper.readLine("Enter Room ID: ");

        boolean anyMissing = bookingID.isEmpty()
                || date.isEmpty()
                || startTime.isEmpty()
                || endTime.isEmpty()
                || roomID.isEmpty();

        if (anyMissing) {
            System.out.println("  [ERROR] All fields are required. Reservation not created.");
            return;
        }

        Booking booking = new Booking(bookingID, date, startTime, endTime, roomID, username);
        FileManager.write(PENDING_FILE, booking.toString());
        System.out.println();
        Colors.success("Reservation submitted for approval!");
        System.out.println();
    }

    public void viewBookedRooms() {
        List<String> lines = FileManager.readAllLines(LAB_ROOMS_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No booked rooms.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            LabRoom room = LabRoom.fromString(line);
            if (room == null) continue;

            rows.add(new String[]{
                room.getRoomID(),
                room.getBookingID(),
                room.getBookedBy(),
                room.getDateBooked()
            });
        }

        String[] headers = {"Room ID", "Booking ID", "Booked By", "Date"};
        TablePrinter.printTable("Booked Lab Rooms", headers, rows);
        InputHelper.pressEnterToContinue();
    }
}
