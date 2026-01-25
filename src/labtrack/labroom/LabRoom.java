package labtrack.labroom;

public class LabRoom {
    private String roomID;
    private int capacity;

    public LabRoom(String roomID, int capacity) {
        this.roomID = roomID;
        this.capacity = capacity;
    }

    public String getRoomID() {
        return roomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void updateCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean canAccommodate(int peopleCount) {
        return peopleCount <= capacity;
    }
    @Override
    public String toString() {
        return safe(roomID) + "," + capacity;
    }

    public static LabRoom fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 2);
        if (parts.length != 2) return null;
        int cap;
        try {
            cap = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return new LabRoom(parts[0], cap);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
