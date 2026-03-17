package labtrack.users;

import java.util.Scanner;
import labtrack.inventory.InventoryService;
import labtrack.inventory.ItemService;

public class Technician extends User {
    public Technician(String id, String name) {
        super(id, name, "Technician");
    }

    @Override
    public void showMenu() {
        System.out.println("+----------------------------------------------+");
        System.out.println("|           TECHNICIAN DASHBOARD               |");
        System.out.println("+----------------------------------------------+");
        System.out.println();
        System.out.println("  ~~~ Inventory ~~~");
        System.out.println("  [1] Add Inventory Item");
        System.out.println("  [2] Update Item Quantity");
        System.out.println("  [3] Mark Item Out of Stock");
        System.out.println("  [4] View Out-of-Stock Items");
        System.out.println("  [5] View Inventory");
        System.out.println();
        System.out.println("  ~~~ Borrow Requests ~~~");
        System.out.println("  [6] View Borrow Requests");
        System.out.println("  [7] Approve Borrow Request");
        System.out.println("  [8] View Borrowed Items");
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