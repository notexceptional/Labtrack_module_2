package labtrack.inventory;

public class InventoryItem {
    private String name;
    private int quantity;

    public InventoryItem(String name, int qty) {
        this.name = name;
        this.quantity = qty;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isLowStock() {
        return quantity < 5;
    }
    @Override
    public String toString() {
        return safe(name) + "," + quantity;
    }

    public static InventoryItem fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 2);
        if (parts.length != 2) return null;
        int qty;
        try {
            qty = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return new InventoryItem(parts[0], qty);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}