package labtrack.inventory;

import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class InventoryService {
    private static final String INVENTORY_FILE = "inventory.csv";



    public void addItemWithType(Scanner sc) {
        System.out.println("Select item type:");
        System.out.println("1. Necessary Item (must have daily)");
        System.out.println("2. Other Item");
        String typeChoice = InputHelper.readLine("Enter choice: ");
        String type = "other";
        if (typeChoice.equals("1")) type = "necessary";

        String name = InputHelper.readLine("Enter Item Name: ");
        String qtyRaw = InputHelper.readLine("Enter Quantity: ");

        int qty;
        try {
            qty = Integer.parseInt(qtyRaw);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        InventoryItem item = new InventoryItem(name, qty, type);
        FileManager.write(INVENTORY_FILE, item.toString());
        System.out.println("Inventory item added.");
    }

    public void updateItemQuantity(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }
        String name = InputHelper.readLine("Enter Item Name to update: ");
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(lines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(name)) {
                String qtyRaw = InputHelper.readLine("Enter new quantity: ");
                int qty;
                try {
                    qty = Integer.parseInt(qtyRaw);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity.");
                    return;
                }
                item.updateQuantity(qty);
                lines.set(i, item.toString());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println("Item quantity updated.");
    }

    public void markItemOut(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }
        String name = InputHelper.readLine("Enter Item Name to mark as out of inventory: ");
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(lines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(name)) {
                item.updateQuantity(0);
                lines.set(i, item.toString());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println("Item marked as out of inventory (quantity set to 0).");
    }

    public void viewOutOfStockItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        boolean any = false;
        System.out.println("=== Out-of-Stock Items ===");
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getQuantity() == 0) {
                String tag = item.getType().equals("necessary") ? " (necessary)" : "";
                System.out.println("Name: " + item.getName() + tag);
                any = true;
            }
        }
        if (!any) {
            System.out.println("No items are out of stock.");
        }
    }

    public void viewInventory() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }

        System.out.println("=== Inventory ===");
        for (String line : lines) {
            String[] p = line.split(",", 2);
            if (p.length < 2) continue;

            System.out.println("Name: " + p[0]);
            System.out.println("Quantity: " + p[1]);
            System.out.println("--------------------");
        }
    }
}
