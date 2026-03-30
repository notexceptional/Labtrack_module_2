package labtrack.labroom;

public class LabRoom {
    private String roomID;
    private String bookingID;
    private String bookedBy;
    private String dateBooked;

    public LabRoom(String roomID, String bookingID, String bookedBy, String dateBooked) {
        this.roomID = roomID;
        this.bookingID = bookingID;
        this.bookedBy = bookedBy;
        this.dateBooked = dateBooked;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    @Override
    public String toString() {
        return safe(roomID) + "," + safe(bookingID) + "," + safe(bookedBy) + "," + safe(dateBooked);
    }

    public static LabRoom fromString(String line) {
        if (line == null) {
            return null;
        }
        String[] parts = line.split(",", 4);
        if (parts.length != 4) {
            return null;
        }
        return new LabRoom(parts[0], parts[1], parts[2], parts[3]);
    }

    private static String safe(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}
