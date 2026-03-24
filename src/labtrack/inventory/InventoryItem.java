package labtrack.inventory;

/**
 * Model class representing an item in the laboratory inventory.
 * Stores item details and provides CSV serialization/deserialization logic.
 */
public class InventoryItem {
    private String name;
    private int quantity;
    private String type;

    /**
     * Constructs a new InventoryItem with the specified name, quantity, and type.
     * If the type is null, it defaults to "other".
     *
     * @param name The name of the inventory item.
     * @param qty The quantity of the inventory item.
     * @param type The type of the inventory item (e.g., "chemical", "glassware").
     */
    public InventoryItem(String name, int qty, String type) {
        this.name = name;
        this.quantity = qty;
        if (type == null) {
            this.type = "other";
        } else {
            this.type = type.toLowerCase();
        }
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
        if (line == null) {
            return null;
        }
        String[] parts = line.split(",", 3);
        if (parts.length < 2) {
            return null;
        }
        int qty;
        try {
            qty = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return null;
        }

        String parsedType;
        if (parts.length == 3) {
            parsedType = parts[2];
        } else {
            parsedType = "other";
        }

        return new InventoryItem(parts[0], qty, parsedType);
    }

    private static String safe(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }
}