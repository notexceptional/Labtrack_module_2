package labtrack.inventory;

public class InventoryItem {
    private String name;
    private int quantity;

    public InventoryItem(String name, int qty) {
        this.name = name;
        this.quantity = qty;
    }

    public boolean isLowStock() {
        return quantity < 5;
    }
}