package labtrack.inventory;

import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class ItemService {
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String BORROW_REQUESTS_FILE = "borrow_requests.csv";
    private static final String REQUESTS_FILE = "item_requests.csv";
    private static final String BORROWED_ITEMS_FILE = "borrowed_items.csv";

    public void viewAvailableItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No inventory items found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|              AVAILABLE ITEMS                 |");
        System.out.println("+----------------------------------------------+");
        boolean any = false;
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item == null) {
                continue;
            }

            String status;
            if (item.getQuantity() > 0) {
                status = "In Stock (" + item.getQuantity() + ")";
            } else {
                status = "Out of Stock";
            }

            String tag = "";
            if (item.getType().equals("necessary")) {
                tag = " [NECESSARY]";
            }
            System.out.println("  " + item.getName() + tag + " : " + status);
            any = true;
        }
        if (!any) {
            System.out.println("  [!] No items available.");
        }
    }

    public void borrowItem(Scanner sc, String borrowerName) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No inventory items found.");
            return;
        }

        String itemName = InputHelper.readLine("Enter item name to borrow: ");

        int borrowQty;
        try {
            borrowQty = Integer.parseInt(InputHelper.readLine("Enter quantity to borrow: "));
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid quantity.");
            return;
        }

        if (borrowQty <= 0) {
            System.out.println("  [ERROR] Quantity must be greater than 0.");
            return;
        }

        boolean found = false;
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                found = true;
                if (item.getQuantity() == 0) {
                    System.out.println("  [ERROR] Item is out of stock.");
                    return;
                }
                if (item.getQuantity() < borrowQty) {
                    System.out.println("  [ERROR] Not enough stock. Available: " + item.getQuantity());
                    return;
                }
                break;
            }
        }

        if (!found) {
            System.out.println("  [ERROR] Item not found in inventory.");
            return;
        }

        String borrowRequest = borrowerName + "|" + itemName + "|" + borrowQty + "|pending|" + System.currentTimeMillis();
        FileManager.write(BORROW_REQUESTS_FILE, borrowRequest);
        System.out.println();
        System.out.println("  >>> Borrow request submitted. Waiting for technician approval. <<<");
        System.out.println();
    }

    public void viewBorrowRequests() {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No borrow requests found.");
            return;
        }

        boolean hasPending = hasPendingBorrowRequests(lines);
        if (!hasPending) {
            System.out.println("  [!] No borrow requests found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|              BORROW REQUESTS                 |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) {
                continue;
            }

            String status = parts[3];
            if (!status.equals("pending")) {
                continue;
            }
            System.out.println("+------------------------------------------+");
            System.out.println("| Requester:  " + parts[0]);
            System.out.println("| Item:       " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("| Status:     " + parts[3]);
            System.out.println("+------------------------------------------+");
        }
    }

    private boolean hasPendingBorrowRequests(List<String> lines) {
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) {
                continue;
            }
            if (parts[3].equals("pending")) {
                return true;
            }
        }
        return false;
    }

    public void approveBorrowRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No borrow requests found.");
            return;
        }

        boolean hasPending = hasPendingBorrowRequests(lines);
        if (!hasPending) {
            System.out.println("  [!] No borrow requests found.");
            return;
        }

        viewBorrowRequests();

        String confirm = InputHelper.readLine("Approve request? (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        String requesterName = InputHelper.readLine("Enter requester name: ");
        String itemName = InputHelper.readLine("Enter item name: ");

        boolean found = false;
        int approvedQty = 0;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", 5);
            if (parts.length < 4) continue;

            boolean sameRequester = parts[0].equalsIgnoreCase(requesterName);
            boolean sameItem = parts[1].equalsIgnoreCase(itemName);
            boolean isPending = parts[3].equals("pending");

            if (sameRequester && sameItem && isPending) {
                found = true;
                approvedQty = Integer.parseInt(parts[2]);
                lines.set(i, parts[0] + "|" + parts[1] + "|" + parts[2] + "|approved|" + parts[4]);
                break;
            }
        }

        if (!found) {
            System.out.println("  [ERROR] Pending request not found.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (int i = 0; i < invLines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(invLines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() < approvedQty) {
                    System.out.println("  [ERROR] Not enough stock to approve. Available: " + item.getQuantity());
                    return;
                }
                int newQuantity = item.getQuantity() - approvedQty;
                item.updateQuantity(newQuantity);
                invLines.set(i, item.toString());
                break;
            }
        }

        String returnDate = InputHelper.readLine("Enter return date (yyyy-MM-dd): ");
        if (returnDate.isEmpty()) {
            System.out.println("  [ERROR] Return date cannot be empty.");
            return;
        }

        FileManager.overwrite(BORROW_REQUESTS_FILE, lines);
        FileManager.overwrite(INVENTORY_FILE, invLines);

        String borrowRecord = requesterName + "|" + itemName + "|" + approvedQty + "|" + returnDate + "|" + System.currentTimeMillis();
        FileManager.write(BORROWED_ITEMS_FILE, borrowRecord);

        System.out.println();
        System.out.println("  >>> Borrow request approved! Return date: " + returnDate + " <<<");
        System.out.println();
    }

    public void viewBorrowedItems() {
        List<String> lines = FileManager.readAllLines(BORROWED_ITEMS_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No borrowed items found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|              BORROWED ITEMS                  |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) continue;
            System.out.println("+------------------------------------------+");
            System.out.println("| Borrower:    " + parts[0]);
            System.out.println("| Item:        " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("| Return Date: " + parts[3]);
            System.out.println("+------------------------------------------+");
        }
    }

    public void requestNewItem(Scanner sc, String requesterName) {
        String itemName = InputHelper.readLine("Enter item name to request: ");

        if (itemName.isEmpty()) {
            System.out.println("  [ERROR] Item name cannot be empty.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (String line : invLines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                System.out.println("  [!] Item already exists in inventory. Use borrow instead.");
                return;
            }
        }

        int qty;
        try {
            qty = Integer.parseInt(InputHelper.readLine("Enter quantity needed: "));
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid quantity.");
            return;
        }

        String reason = InputHelper.readLine("Enter reason for request: ");

        String request = requesterName + "|" + itemName + "|" + qty + "|" + reason + "|pending|" + System.currentTimeMillis();
        FileManager.write(REQUESTS_FILE, request);
        System.out.println();
        System.out.println("  >>> Item request submitted successfully! <<<");
        System.out.println();
    }

    public void viewAllRequests() {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No item requests found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|               ITEM REQUESTS                  |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            String[] parts = line.split("\\|", 6);
            if (parts.length < 5) continue;
            System.out.println("+------------------------------------------+");
            System.out.println("| Requester:  " + parts[0]);
            System.out.println("| Item:       " + parts[1] + " (Qty: " + parts[2] + ")");
            System.out.println("| Reason:     " + parts[3]);
            System.out.println("| Status:     " + parts[4]);
            System.out.println("+------------------------------------------+");
        }
    }

    public void approveRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            System.out.println("  [!] No item requests found.");
            return;
        }

        viewAllRequests();

        String confirm = InputHelper.readLine("Approve request? (Y/N): ");
        if (!confirm.equalsIgnoreCase("Y")) {
            return;
        }

        String itemName = InputHelper.readLine("Enter item name to approve: ");

        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", 6);
            if (parts.length < 5) continue;
            boolean sameItem = parts[1].equalsIgnoreCase(itemName);
            boolean isPending = parts[4].equals("pending");
            if (sameItem && isPending) {
                found = true;
                lines.set(i, parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|approved|" + parts[5]);
                break;
            }
        }

        if (!found) {
            System.out.println("  [ERROR] Pending request not found for this item.");
            return;
        }

        FileManager.overwrite(REQUESTS_FILE, lines);
        System.out.println();
        System.out.println("  >>> Request approved! <<<");
        System.out.println();
    }
}
