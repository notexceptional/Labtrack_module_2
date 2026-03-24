package labtrack.users;

import java.util.Scanner;
import labtrack.booking.BookingService;
import labtrack.experiments.ExperimentService;
import labtrack.inventory.ItemService;

import labtrack.util.Colors;

public class Researcher extends User {

    public Researcher(String id, String name) {
        super(id, name, "Researcher");
    }

    @Override
    public void showMenu() {
        Colors.header("Researcher Dashboard");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Experiments ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("1", Colors.YELLOW_BOLD) + "] Add Experiment");
        System.out.println("  [" + Colors.colorize("2", Colors.YELLOW_BOLD) + "] View Experiments");
        System.out.println("  [" + Colors.colorize("3", Colors.YELLOW_BOLD) + "] Modify Experiment");
        System.out.println("  [" + Colors.colorize("20", Colors.YELLOW_BOLD) + "] Delete Experiment");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Version Control ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("6", Colors.YELLOW_BOLD) + "] View Version History");
        System.out.println("  [" + Colors.colorize("7", Colors.YELLOW_BOLD) + "] Restore Version");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Items ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("10", Colors.YELLOW_BOLD) + "] View Available Items");
        System.out.println("  [" + Colors.colorize("11", Colors.YELLOW_BOLD) + "] Borrow Item");
        System.out.println("  [" + Colors.colorize("12", Colors.YELLOW_BOLD) + "] Request New Item");
        System.out.println();
        System.out.println(Colors.colorize("  ~~~ Reservations ~~~", Colors.CYAN_BOLD));
        System.out.println("  [" + Colors.colorize("21", Colors.YELLOW_BOLD) + "] Make Reservation");
        System.out.println("  [" + Colors.colorize("22", Colors.YELLOW_BOLD) + "] View Booked Rooms");
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
