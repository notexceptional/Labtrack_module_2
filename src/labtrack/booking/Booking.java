package labtrack.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Model class representing a room reservation.
 * Stores booking details including ID, user, item, and date.
 */
public class Booking {
    private String bookingID;
    private String date;
    private String startTime;
    private String endTime;
    private String roomID;
    private String bookedBy;

    public Booking(String bookingID, String date, String startTime, String endTime, String roomID, String bookedBy) {
        this.bookingID = bookingID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.bookedBy = bookedBy;
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

    public String getBookedBy() {
        return bookedBy;
    }

    public LocalDate getDate() {
        if (date == null) {
            return null;
        }

        String trimmedDate = date.trim();
        if (trimmedDate.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(trimmedDate);
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
        LocalTime start = getStartTime();
        LocalTime end = getEndTime();
        if (start == null) {
            return false;
        }
        if (end == null) {
            return false;
        }
        return start.isBefore(end);
    }

    public boolean overlaps(Booking other) {
        if (other == null) {
            return false;
        }

        if (roomID == null) {
            return false;
        }
        if (other.roomID == null) {
            return false;
        }

        boolean sameRoom = roomID.equals(other.roomID);
        if (!sameRoom) {
            return false;
        }

        LocalDate thisDate = getDate();
        LocalDate otherDate = other.getDate();
        if (thisDate == null) {
            return false;
        }
        if (otherDate == null) {
            return false;
        }

        boolean sameDate = thisDate.equals(otherDate);
        if (!sameDate) {
            return false;
        }

        LocalTime thisStart = getStartTime();
        LocalTime thisEnd = getEndTime();
        LocalTime otherStart = other.getStartTime();
        LocalTime otherEnd = other.getEndTime();

        if (thisStart == null || thisEnd == null || otherStart == null || otherEnd == null) {
            return false;
        }

        boolean thisStartsBeforeOtherEnds = thisStart.isBefore(otherEnd);
        boolean otherStartsBeforeThisEnds = otherStart.isBefore(thisEnd);
        return thisStartsBeforeOtherEnds && otherStartsBeforeThisEnds;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(safe(bookingID));
        builder.append(",");
        builder.append(safe(date));
        builder.append(",");
        builder.append(safe(startTime));
        builder.append(",");
        builder.append(safe(endTime));
        builder.append(",");
        builder.append(safe(roomID));
        builder.append(",");
        builder.append(safe(bookedBy));
        return builder.toString();
    }

    public static Booking fromString(String line) {
        if (line == null) {
            return null;
        }
        String[] parts = line.split(",", 6);
        if (parts.length != 6) {
            return null;
        }
        return new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    private static LocalTime parseTime(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(trimmedValue);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String safe(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}
