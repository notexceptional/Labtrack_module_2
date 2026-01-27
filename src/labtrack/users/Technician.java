package labtrack.users;

import labtrack.inventory.InventoryService;

import java.util.Scanner;

public class Technician extends User {
    public Technician(String id, String name) {
        super(id, name, "Technician");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add Inventory Item");
        System.out.println("2. Update Inventory Item Quantity");
        System.out.println("3. Mark Item as Out of Inventory");
        System.out.println("4. View Out-of-Stock Necessary Items");
        System.out.println("5. View Inventory");
        System.out.println("0. Logout");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        InventoryService service = new InventoryService();
        switch (choice) {
            case 1:
                service.addItemWithType(sc);
                break;
            case 2:
                service.updateItemQuantity(sc);
                break;
            case 3:
                service.markItemOut(sc);
                break;
            case 4:
                service.viewOutOfStockNecessaryItems();
                break;
            case 5:
                service.viewInventory();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}