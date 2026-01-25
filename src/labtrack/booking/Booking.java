package labtrack.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Booking {
    private String bookingID;
    private String date;
    private String startTime;
    private String endTime;
    private String roomID;

    public Booking(String bookingID, String date, String startTime, String endTime, String roomID) {
        this.bookingID = bookingID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getDateString() {
        return date;
    }

    public String getStartTimeString() {
        return startTime;
    }

    public String getEndTimeString() {
        return endTime;
    }

    public String getRoomID() {
        return roomID;
    }
    public LocalDate getDate() {
        if (date == null || date.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    public LocalTime getStartTime() {
        return parseTime(startTime);
    }
    public LocalTime getEndTime() {
        return parseTime(endTime);
    }

    public boolean isValidTimeRange() {
        LocalTime s = getStartTime();
        LocalTime e = getEndTime();
        return s != null && e != null && s.isBefore(e);
    }

    public boolean overlaps(Booking other) {
        if (other == null) return false;
        if (roomID == null || other.roomID == null) return false;
        if (!roomID.equals(other.roomID)) return false;

        LocalDate d1 = getDate();
        LocalDate d2 = other.getDate();
        if (d1 == null || d2 == null || !d1.equals(d2)) return false;

        LocalTime s1 = getStartTime();
        LocalTime e1 = getEndTime();
        LocalTime s2 = other.getStartTime();
        LocalTime e2 = other.getEndTime();
        if (s1 == null || e1 == null || s2 == null || e2 == null) return false;
        return s1.isBefore(e2) && s2.isBefore(e1);
    }
    @Override
    public String toString() {
        return safe(bookingID) + "," + safe(date) + "," + safe(startTime) + "," + safe(endTime) + "," + safe(roomID);
    }

    public static Booking fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 5);
        if (parts.length != 5) return null;
        return new Booking(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    private static LocalTime parseTime(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return LocalTime.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
