package labtrack.users;

import java.util.Scanner;
import labtrack.inventory.InventoryService;
import labtrack.inventory.ItemService;

import labtrack.util.Colors;

/**
 * Represents a Technician in the laboratory.
 * Technicians are responsible for inventory management, stock updates,
 * and approving borrow requests from researchers.
 */
public class Technician extends User {
    /**
     * Constructs a new Technician with the specified ID and name.
     *
     * @param id The unique identifier for the technician.
     * @param name The name of the technician.
     */
    public Technician(String id, String name) {
        super(id, name, "Technician");
    }

    /**
     * Displays the menu options available to a Technician.
     * This includes inventory management, stock updates, and borrow request approvals.
     */
    @Override
    public void showMenu() {
        Colors.header("Technician Dashboard");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Inventory ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("1", Colors.YELLOW_BOLD) + "] Add Inventory Item");
        System.out.println("  [" + Colors.colorize("2", Colors.YELLOW_BOLD) + "] Update Item Quantity");
        System.out.println("  [" + Colors.colorize("3", Colors.YELLOW_BOLD) + "] Mark Item Out of Stock");
        System.out.println("  [" + Colors.colorize("4", Colors.YELLOW_BOLD) + "] View Out-of-Stock Items");
        System.out.println("  [" + Colors.colorize("5", Colors.YELLOW_BOLD) + "] View Inventory");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Borrow Requests ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("6", Colors.YELLOW_BOLD) + "] View Borrow Requests");
        System.out.println("  [" + Colors.colorize("7", Colors.YELLOW_BOLD) + "] Approve Borrow Request");
        System.out.println("  [" + Colors.colorize("8", Colors.YELLOW_BOLD) + "] View Borrowed Items");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        InventoryService invService = new InventoryService();
        ItemService itemService = new ItemService();

        if (choice == 1) {
            invService.addItemWithType(sc);
            return;
        }
        if (choice == 2) {
            invService.updateItemQuantity(sc);
            return;
        }
        if (choice == 3) {
            invService.markItemOut(sc);
            return;
        }
        if (choice == 4) {
            invService.viewOutOfStockItems();
            return;
        }
        if (choice == 5) {
            invService.viewInventory();
            return;
        }
        if (choice == 6) {
            itemService.viewBorrowRequests();
            return;
        }
        if (choice == 7) {
            itemService.approveBorrowRequest(sc);
            return;
        }
        if (choice == 8) {
            itemService.viewBorrowedItems();
            return;
        }
    }
}