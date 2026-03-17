package labtrack.inventory;

import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class InventoryService {
    private static final String INVENTORY_FILE = "inventory.csv";



    public void addItemWithType(Scanner sc) {
        System.out.println();
        System.out.println("  ~~~ Select Item Type ~~~");
        System.out.println("  [1] Necessary Item (must have daily)");
        System.out.println("  [2] Other Item");
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
            System.out.println("  [ERROR] Invalid quantity.");
            return;
        }

        if (name.isEmpty()) {
            System.out.println("  [ERROR] Item name cannot be empty.");
            return;
        }

        InventoryItem item = new InventoryItem(name, qty, type);
        FileManager.write(INVENTORY_FILE, item.toString());
        System.out.println();
        System.out.println("  >>> Inventory item added! <<<");
        System.out.println();
    }

    public void updateItemQuantity(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            System.out.println("  [!] No inventory items found.");
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
                    System.out.println("  [ERROR] Invalid quantity.");
                    return;
                }
                item.updateQuantity(qty);
                lines.set(i, item.toString());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("  [ERROR] Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println();
        System.out.println("  >>> Item quantity updated! <<<");
        System.out.println();
    }

    public void markItemOut(Scanner sc) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        if (lines.isEmpty()) {
            System.out.println("  [!] No inventory items found.");
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
            System.out.println("  [ERROR] Item not found.");
            return;
        }
        FileManager.overwrite(INVENTORY_FILE, lines);
        System.out.println();
        System.out.println("  >>> Item marked as out of inventory! <<<");
        System.out.println();
    }

    public void viewOutOfStockItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);
        boolean any = false;
        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|           OUT-OF-STOCK ITEMS                 |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getQuantity() == 0) {
                String tag = "";
                if (item.getType().equals("necessary")) {
                    tag = " (NECESSARY)";
                }
                System.out.println("  - " + item.getName() + tag);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  [!] No items are out of stock.");
        }
    }

    public void viewInventory() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No inventory items found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|                INVENTORY                     |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item == null) {
                continue;
            }

            String status;
            if (item.getQuantity() > 0) {
                status = "In Stock";
            } else {
                status = "OUT OF STOCK";
            }

            String tag = "";
            if (item.getType().equals("necessary")) {
                tag = " [NECESSARY]";
            }
            System.out.println("+------------------------------------------+");
            System.out.println("| Name:     " + item.getName() + tag);
            System.out.println("| Quantity: " + item.getQuantity());
            System.out.println("| Status:   " + status);
            System.out.println("+------------------------------------------+");
        }
    }
}
