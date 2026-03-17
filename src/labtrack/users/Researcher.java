package labtrack.users;

import java.util.Scanner;
import labtrack.booking.BookingService;
import labtrack.experiments.ExperimentService;
import labtrack.inventory.ItemService;

public class Researcher extends User {

    public Researcher(String id, String name) {
        super(id, name, "Researcher");
    }

    @Override
    public void showMenu() {
        System.out.println("+----------------------------------------------+");
        System.out.println("|           RESEARCHER DASHBOARD               |");
        System.out.println("+----------------------------------------------+");
        System.out.println();
        System.out.println("  ~~~ Experiments ~~~");
        System.out.println("  [1] Add Experiment");
        System.out.println("  [2] View Experiments");
        System.out.println("  [3] Modify Experiment");
        System.out.println();
        System.out.println("  ~~~ Version Control ~~~");
        System.out.println("  [6] View Version History");
        System.out.println("  [7] Restore Version");
        System.out.println();
        System.out.println("  ~~~ Items ~~~");
        System.out.println("  [10] View Available Items");
        System.out.println("  [11] Borrow Item");
        System.out.println("  [12] Request New Item");
        System.out.println();
        System.out.println("  ~~~ Reservations ~~~");
        System.out.println("  [20] Delete Experiment");
        System.out.println("  [21] Make Reservation");
        System.out.println("  [22] View Booked Rooms");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        ExperimentService expService = new ExperimentService();
        ItemService itemService = new ItemService();
        BookingService bookingService = new BookingService();

        if (choice == 1) {
            expService.addExperiment(sc);
            return;
        }
        if (choice == 2) {
            expService.viewExperiments();
            return;
        }
        if (choice == 3) {
            expService.modifyExperiment(sc);
            return;
        }
        if (choice == 6) {
            expService.viewVersionHistory(sc);
            return;
        }
        if (choice == 7) {
            expService.restoreVersion(sc);
            return;
        }
        if (choice == 10) {
            itemService.viewAvailableItems();
            return;
        }
        if (choice == 11) {
            itemService.borrowItem(sc, username);
            return;
        }
        if (choice == 12) {
            itemService.requestNewItem(sc, username);
            return;
        }
        if (choice == 20) {
            expService.deleteExperiment(sc);
            return;
        }
        if (choice == 21) {
            bookingService.makeReservation(sc, username);
            return;
        }
        if (choice == 22) {
            bookingService.viewBookedRooms();
            return;
        }
    }
}
