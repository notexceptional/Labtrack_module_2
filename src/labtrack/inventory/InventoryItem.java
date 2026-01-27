package labtrack.inventory;


public class InventoryItem {
    private String name;
    private int quantity;
    private String type;

    public InventoryItem(String name, int qty, String type) {
        this.name = name;
        this.quantity = qty;
        this.type = type == null ? "other" : type.toLowerCase();
    }


    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }


    public boolean isLowStock() {
        return quantity < 5;
    }

    @Override
    public String toString() {
        return safe(name) + "," + quantity + "," + safe(type);
    }

    public static InventoryItem fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 3);
        if (parts.length < 2) return null;
        int qty;
        try {
            qty = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        String type = parts.length == 3 ? parts[2] : "other";
        return new InventoryItem(parts[0], qty, type);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}