package labtrack.booking;

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
}
