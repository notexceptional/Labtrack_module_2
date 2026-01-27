package labtrack.inventory;

import labtrack.util.FileManager;

import java.util.List;
import java.util.Scanner;

public class InventoryService {
    private static final String INVENTORY_FILE = "inventory.csv";



    public void addItemWithType(Scanner sc) {
        System.out.println("Select item type:");
        System.out.println("1. Necessary Item (must have daily)");
        System.out.println("2. Other Item");
        System.out.print("Enter choice: ");
        String typeChoice = sc.nextLine().trim();
        String type = "other";
        if (typeChoice.equals("1")) type = "necessary";

        System.out.print("Enter Item Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Quantity: ");
        String qtyRaw = sc.nextLine().trim();

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
        System.out.print("Enter Item Name to update: ");
        String name = sc.nextLine().trim();
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(lines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new quantity: ");
                String qtyRaw = sc.nextLine().trim();
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
        System.out.print("Enter Item Name to mark as out of inventory: ");
        String name = sc.nextLine().trim();
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

    public void viewOutOfStockNecessaryItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        boolean any = false;
        System.out.println("=== Out-of-Stock Necessary Items ===");
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getType().equals("necessary") && item.getQuantity() == 0) {
                System.out.println("Name: " + item.getName());
                any = true;
            }
        }
        if (!any) {
            System.out.println("No necessary items are out of stock.");
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
