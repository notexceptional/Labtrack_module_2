package labtrack.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;
import labtrack.util.TablePrinter;


public class ItemService {
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String BORROW_REQUESTS_FILE = "borrow_requests.csv";
    private static final String REQUESTS_FILE = "item_requests.csv";
    private static final String BORROWED_ITEMS_FILE = "borrowed_items.csv";


    public void viewAvailableItems() {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No inventory items found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item == null) continue;

            String status = (item.getQuantity() > 0) 
                ? Colors.colorize("In Stock (" + item.getQuantity() + ")", Colors.GREEN) 
                : Colors.colorize("Out of Stock", Colors.RED_BOLD);

            String tag = item.getType().equals("necessary") ? Colors.colorize("[NECESSARY]", Colors.PURPLE_BOLD) : "";
            rows.add(new String[]{item.getName() + " " + tag, status});
        }

        String[] headers = {"Item Name", "Status"};
        TablePrinter.printTable("Available Items", headers, rows);
        InputHelper.pressEnterToContinue();
    }

    public void borrowItem(Scanner sc, String borrowerName) {
        List<String> lines = FileManager.readAllLines(INVENTORY_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No inventory items found.");
            return;
        }

        String itemName = InputHelper.readLine("Enter item name to borrow: ");

        int borrowQty;
        try {
            borrowQty = Integer.parseInt(InputHelper.readLine("Enter quantity to borrow: "));
        } catch (NumberFormatException e) {
            Colors.error("Invalid quantity.");
            return;
        }

        if (borrowQty <= 0) {
            Colors.error("Quantity must be greater than 0.");
            return;
        }

        boolean found = false;
        for (String line : lines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                found = true;
                if (item.getQuantity() == 0) {
                    Colors.error("Item is out of stock.");
                    return;
                }
                if (item.getQuantity() < borrowQty) {
                    Colors.error("Not enough stock. Available: " + item.getQuantity());
                    return;
                }
                break;
            }
        }

        if (!found) {
            Colors.error("Item not found in inventory.");
            return;
        }

        String borrowRequest = borrowerName + "|" + itemName + "|" + borrowQty + "|pending|" + System.currentTimeMillis();
        FileManager.write(BORROW_REQUESTS_FILE, borrowRequest);
        System.out.println();
        Colors.success("Borrow request submitted. Waiting for technician approval.");
        System.out.println();
    }

    public void viewBorrowRequests() {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No borrow requests found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4 || !parts[3].equals("pending")) continue;

            rows.add(new String[]{parts[0], parts[1], parts[2], Colors.colorize(parts[3], Colors.YELLOW_BOLD)});
        }

        if (rows.isEmpty()) {
            Colors.warning("No pending borrow requests found.");
            return;
        }

        String[] headers = {"Requester", "Item", "Qty", "Status"};
        TablePrinter.printTable("Pending Borrow Requests", headers, rows);
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

    /**
     * Technicians use this to approve item borrow requests from researchers.
     * Decrements inventory and adds to the borrowed_items record.
     */
    public void approveBorrowRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(BORROW_REQUESTS_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No borrow requests found.");
            return;
        }

        boolean hasPending = hasPendingBorrowRequests(lines);
        if (!hasPending) {
            Colors.warning("No borrow requests found.");
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
            Colors.error("Pending request not found.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (int i = 0; i < invLines.size(); i++) {
            InventoryItem item = InventoryItem.fromString(invLines.get(i));
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                if (item.getQuantity() < approvedQty) {
                    Colors.error("Not enough stock to approve. Available: " + item.getQuantity());
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
            Colors.error("Return date cannot be empty.");
            return;
        }

        FileManager.overwrite(BORROW_REQUESTS_FILE, lines);
        FileManager.overwrite(INVENTORY_FILE, invLines);

        String borrowRecord = requesterName + "|" + itemName + "|" + approvedQty + "|" + returnDate + "|" + System.currentTimeMillis();
        FileManager.write(BORROWED_ITEMS_FILE, borrowRecord);

        System.out.println();
        Colors.success("Borrow request approved! Return date: " + returnDate);
        System.out.println();
    }

    public void viewBorrowedItems() {
        List<String> lines = FileManager.readAllLines(BORROWED_ITEMS_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No borrowed items found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", 5);
            if (parts.length < 4) continue;
            rows.add(new String[]{parts[0], parts[1], parts[2], parts[3]});
        }

        String[] headers = {"Borrower", "Item", "Qty", "Return Date"};
        TablePrinter.printTable("Currently Borrowed Items", headers, rows);
        InputHelper.pressEnterToContinue();
    }

    public void requestNewItem(Scanner sc, String requesterName) {
        String itemName = InputHelper.readLine("Enter item name to request: ");

        if (itemName.isEmpty()) {
            Colors.error("Item name cannot be empty.");
            return;
        }

        List<String> invLines = FileManager.readAllLines(INVENTORY_FILE);
        for (String line : invLines) {
            InventoryItem item = InventoryItem.fromString(line);
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                Colors.warning("Item already exists in inventory. Use borrow instead.");
                return;
            }
        }

        int qty;
        try {
            qty = Integer.parseInt(InputHelper.readLine("Enter quantity needed: "));
        } catch (NumberFormatException e) {
            Colors.error("Invalid quantity.");
            return;
        }

        String reason = InputHelper.readLine("Enter reason for request: ");

        String request = requesterName + "|" + itemName + "|" + qty + "|" + reason + "|pending|" + System.currentTimeMillis();
        FileManager.write(REQUESTS_FILE, request);
        System.out.println();
        Colors.success("Item request submitted successfully!");
        System.out.println();
    }

    public void viewAllRequests() {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No item requests found.");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|", 6);
            if (parts.length < 5) continue;
            
            String status = parts[4].equals("pending") ? Colors.colorize(parts[4], Colors.YELLOW_BOLD) : Colors.colorize(parts[4], Colors.GREEN);
            rows.add(new String[]{parts[0], parts[1], parts[2], parts[3], status});
        }

        String[] headers = {"Requester", "Item", "Qty", "Reason", "Status"};
        TablePrinter.printTable("All Item Requests", headers, rows);
        InputHelper.pressEnterToContinue();
    }

    public void approveRequest(Scanner sc) {
        List<String> lines = FileManager.readAllLines(REQUESTS_FILE);

        if (lines.isEmpty()) {
            Colors.warning("No item requests found.");
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
            Colors.error("Pending request not found for this item.");
            return;
        }

        FileManager.overwrite(REQUESTS_FILE, lines);
        System.out.println();
        Colors.success("Request approved!");
        System.out.println();
    }
}
