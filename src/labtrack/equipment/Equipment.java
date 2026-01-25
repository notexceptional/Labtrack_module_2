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
}