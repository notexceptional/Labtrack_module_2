package labtrack.users;

import labtrack.inventory.InventoryService;

import java.util.Scanner;

public class Technician extends User {
    public Technician(String id, String name) {
        super(id, name, "Technician");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add Inventory Item\n2. View Inventory\n0. Logout");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        InventoryService service = new InventoryService();
        if (choice == 1) {
            service.addItem(sc);
            return;
        }
        if (choice == 2) {
            service.viewInventory();
            return;
        }
        System.out.println("Invalid choice");
    }
}