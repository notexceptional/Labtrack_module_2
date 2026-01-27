package labtrack.users;

import labtrack.experiments.ExperimentService;
import java.util.Scanner;

public class Researcher extends User {

    public Researcher(String id, String name) {
        super(id, name, "Researcher");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add Experiment");
        System.out.println("2. View Experiment");
        System.out.println("3. Modify Experiment");
        System.out.println("4. Delete Experiment");
        System.out.println("5. Make Reservation");
        System.out.println("0. Logout");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        ExperimentService service = new ExperimentService();
        if (choice == 1) {
            service.addExperiment();
        }
        if (choice == 2) {
            service.viewExperiments();
        }
        if (choice == 3) {
            service.modifyExperiment();
        }
        if (choice == 4) {
            service.deleteExperiment(sc);
        }
        if (choice == 5) {
            new labtrack.booking.BookingService().makeReservation(sc);
        }
    }
}
