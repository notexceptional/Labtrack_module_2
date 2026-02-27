package labtrack.users;

import java.util.Scanner;
import labtrack.experiments.ExperimentService;

public class Researcher extends User {

    public Researcher(String id, String name) {
        super(id, name, "Researcher");
    }

    @Override
    public void showMenu() {
        System.out.println("1. Add Experiment");
        System.out.println("2. View Experiments");
        System.out.println("3. Modify Experiment");
        System.out.println("--- Version Control ---");
        System.out.println("6. View Version History");
        System.out.println("7. Restore Version");
        System.out.println("--- Other ---");
        System.out.println("8. Delete Experiment");
        System.out.println("9. Make Reservation");
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        ExperimentService service = new ExperimentService();
        switch (choice) {
            case 1:
                service.addExperiment(sc);
                break;
            case 2:
                service.viewExperiments();
                break;
            case 3:
                service.modifyExperiment(sc);
                break;
            case 6:
                service.viewVersionHistory(sc);
                break;
            case 7:
                service.restoreVersion(sc);
                break;
            case 8:
                service.deleteExperiment(sc);
                break;
            case 9:
                new labtrack.booking.BookingService().makeReservation(sc);
                break;
            default:
                break;
        }
    }
}
