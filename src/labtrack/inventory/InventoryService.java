package labtrack.inventory;

import labtrack.util.FileManager;

import java.util.List;
import java.util.Scanner;

public class InventoryService {
    private static final String INVENTORY_FILE = "inventory.csv";

    public void addItem(Scanner sc) {
        sc.nextLine();

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

        InventoryItem item = new InventoryItem(name, qty);
        FileManager.write(INVENTORY_FILE, item.toString());
        System.out.println("Inventory item added.");
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
