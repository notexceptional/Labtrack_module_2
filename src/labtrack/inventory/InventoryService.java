package labtrack.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;
import labtrack.util.TablePrinter;

public class InventoryService {
    private static final String INVENTORY_FILE = "inventory.csv";

    public void addItemWithType(Scanner sc) {
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Select Item Type ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("1", Colors.YELLOW_BOLD) + "] Necessary Item (must have daily)");
        System.out.println("  [" + Colors.colorize("2", Colors.YELLOW_BOLD) + "] Other Item");
        String typeChoice = InputHelper.readLine("Enter choice: ");
        String type = "other";
        if (typeChoice.equals("1")) {
            type = "necessary";
        }

        String name = InputHelper.readLine("Enter Item Name: ");
        String qtyRaw = InputHelper.readLine("Enter Quantity: ");

        int qty;
        try {
            qty = Integer.parseInt(qtyRaw);
        } catch (NumberFormatException e) {
            Colors.error("Invalid quantity.");
            return;
        }

        if (name.isEmpty()) {
            Colors.error("Item name cannot be empty.");
            return;
        }

        InventoryItem item = new InventoryItem(name, qty, type);
        FileManager.write(INVENTORY_FILE, item.toString());
        System.out.println();
        Colors.success("Inventory item added!");
        System.out.println();
    }

    public void updateItemQuantity(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No inventory items found.");
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
                    Colors.error("Invalid quantity.");
                    return;
                }
                item.updateQuantity(qty);
                lines.set(i, item.toString());
                found = true;
                break;
            }
        }
        if (!found) {
            Colors.error("Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println();
        Colors.success("Item quantity updated!");
        System.out.println();
    }

    public void markItemOut(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            Colors.warning("No inventory items found.");
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
            Colors.error("Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println();
        Colors.success("Item marked as out of inventory!");
        System.out.println();
    }

    public void viewOutOfStockItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        List<String[]> rows = new ArrayList<>();
        
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getQuantity() == 0) {
                String tag = item.getType().equals("necessary") ? "[NECESSARY]" : "";
                rows.add(new String[]{item.getName(), tag});
            }
        }
        
        String[] headers = {"Item Name", "Notes"};
        TablePrinter.printTable("Out-of-Stock Items", headers, rows);
        InputHelper.pressEnterToContinue();
    }

    public void viewInventory() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        List<String[]> rows = new ArrayList<>();

        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item == null) continue;

            String status = (item.getQuantity() > 0) ? Colors.colorize("In Stock", Colors.GREEN) : Colors.colorize("OUT OF STOCK", Colors.RED_BOLD);
            String tag = item.getType().equals("necessary") ? Colors.colorize("[NECESSARY]", Colors.PURPLE_BOLD) : "";
            
            rows.add(new String[]{item.getName() + " " + tag, String.valueOf(item.getQuantity()), status});
        }

        String[] headers = {"Item Name", "Qty", "Status"};
        TablePrinter.printTable("Current Inventory", headers, rows);
        InputHelper.pressEnterToContinue();
    }
}
