package labtrack.equipment;

public class Equipment {
    private String id;
    private boolean reserved;

    public Equipment(String id) {
        this.id = id;
        this.reserved = false;
    }

    public boolean reserve() {
        if (reserved) return false;
        reserved = true;
        return true;
    }

    public String getId() {
        return id;
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean release() {
        if (!reserved) return false;
        reserved = false;
        return true;
    }
    @Override
    public String toString() {
        return safe(id) + "," + reserved;
    }

    public static Equipment fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 2);
        if (parts.length == 0) return null;
        Equipment e = new Equipment(parts[0]);
        if (parts.length == 2) {
            e.reserved = Boolean.parseBoolean(parts[1].trim());
        }
        return e;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}