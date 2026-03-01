package labtrack.inventory;

import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;

public class ItemService {
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String BORROW_REQUESTS_FILE = "borrow_requests.csv";
    private static final String REQUESTS_FILE = "item_requests.csv";
    private static final String BORROWED_ITEMS_FILE = "borrowed_items.csv";

    public void viewAvailableItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }

        System.out.println("=== Available Inventory Items ===");
        boolean any = false;
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item == null) continue;
            String status = item.getQuantity() > 0 ? "In Stock (" + item.getQuantity() + ")" : "Out of Stock";
            String tag = item.getType().equals("necessary") ? " [necessary]" : "";
            System.out.println("- " + item.getName() + tag + " : " + status);
            any = true;
        }
        if (!any) {
            System.out.println("No items available.");
        }
    }

    public void borrowItem(Scanner sc, String borrowerName) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }

        System.out.print("Enter item name to borrow: ");
        String itemName = sc.nextLine().trim();

        System.out.print("Enter quantity to borrow: ");
        int borrowQty;
        try {
            borrowQty = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (borrowQty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        boolean found = false;
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                found = true;
                if (item.getQuantity() == 0) {
                    System.out.println("Item is out of stock.");
                    return;
                }
                if (item.getQuantity() < borrowQty) {
                    System.out.println("Not enough stock. Available: " + item.getQuantity());
                    return;
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Item not found in inventory.");
            return;
        }

        String borrowRequest = borrowerName + "|" + itemName + "|" + borrowQty + "|pending|" + System.currentTimeMillis();
        FileManager.write(BORROW_REQUESTS_FILE, borrowRequest);
        System.out.println("Borrow request submitted. Waiting for technician approval.");
    }

    public void viewBorrowRequests() {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("No borrow requests found.");
            return;
        }

        System.out.println("=== Borrow Requests ===");
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) continue;
            System.out.println("Requester: " + parts[0]);
            System.out.println("Item: " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("Status: " + parts[3]);
            System.out.println("--------------------");
        }
    }

    public void approveBorrowRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("No borrow requests found.");
            return;
        }

        viewBorrowRequests();

        System.out.print("Approve request? (Y/N): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        System.out.print("Enter requester name: ");
        String requesterName = sc.nextLine().trim();
        System.out.print("Enter item name: ");
        String itemName = sc.nextLine().trim();

        boolean found = false;
        int approvedQty = 0;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", 5);
            if (parts.length < 4) continue;
            if (parts[0].equalsIgnoreCase(requesterName) && parts[1].equalsIgnoreCase(itemName) && parts[3].equals("pending")) {
                found = true;
                approvedQty = Integer.parseInt(parts[2]);
                lines.set(i, parts[0] + "|" + parts[1] + "|" + parts[2] + "|approved|" + parts[4]);
                break;
            }
        }

        if (!found) {
            System.out.println("Pending request not found.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (int i = 0; i < invLines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(invLines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() < approvedQty) {
                    System.out.println("Not enough stock to approve. Available: " + item.getQuantity());
                    return;
                }
                item.updateQuantity(item.getQuantity() - approvedQty);
                invLines.set(i, item.toString());
                break;
            }
        }

        System.out.print("Enter return date (yyyy-MM-dd): ");
        String returnDate = sc.nextLine().trim();
        if (returnDate.isEmpty()) {
            System.out.println("Return date cannot be empty.");
            return;
        }

        FileManager.overwrite(BORROW_REQUESTS_FILE, lines);
        FileManager.overwrite(INVENTORY_FILE, invLines);

        String borrowRecord = requesterName + "|" + itemName + "|" + approvedQty + "|" + returnDate + "|" + System.currentTimeMillis();
        FileManager.write(BORROWED_ITEMS_FILE, borrowRecord);

        System.out.println("Borrow request approved. Return date: " + returnDate);
    }

    public void viewBorrowedItems() {
        List<String> lines = FileManager.readAllLines(BORROWED_ITEMS_FILE);

        if (lines.isEmpty()) {
            System.out.println("No borrowed items found.");
            return;
        }

        System.out.println("=== Borrowed Items ===");
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) continue;
            System.out.println("Borrower: " + parts[0]);
            System.out.println("Item: " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("Return Date: " + parts[3]);
            System.out.println("--------------------");
        }
    }

    public void requestNewItem(Scanner sc, String requesterName) {
        System.out.print("Enter item name to request: ");
        String itemName = sc.nextLine().trim();

        if (itemName.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (String line : invLines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                System.out.println("Item already exists in inventory. Use borrow instead.");
                return;
            }
        }

        System.out.print("Enter quantity needed: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        System.out.print("Enter reason for request: ");
        String reason = sc.nextLine().trim();

        String request = requesterName + "|" + itemName + "|" + qty + "|" + reason + "|pending|" + System.currentTimeMillis();
        FileManager.write(REQUESTS_FILE, request);
        System.out.println("Item request submitted successfully.");
    }

    public void viewAllRequests() {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("No item requests found.");
            return;
        }

        System.out.println("=== All Item Requests ===");
        for (String line : lines) {
            String[] parts = line.split("\\|", 6);
            if (parts.length < 5) continue;
            System.out.println("Requester: " + parts[0]);
            System.out.println("Item: " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("Reason: " + parts[3]);
            System.out.println("Status: " + parts[4]);
            System.out.println("--------------------");
        }
    }

    public void approveRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("No item requests found.");
            return;
        }

        viewAllRequests();

        System.out.print("Approve request? (Y/N): ");
        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        System.out.print("Enter item name to approve: ");
        String itemName = sc.nextLine().trim();

        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", 6);
            if (parts.length < 5) continue;
            if (parts[1].equalsIgnoreCase(itemName) && parts[4].equals("pending")) {
                found = true;
                lines.set(i, parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|approved|" + parts[5]);
                break;
            }
        }

        if (!found) {
            System.out.println("Pending request not found for this item.");
            return;
        }

        FileManager.overwrite(REQUESTS_FILE, lines);
        System.out.println("Request approved.");
    }
}
