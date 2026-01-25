package labtrack.users;

import labtrack.experiments.ExperimentService;
import java.util.Scanner;

public class Researcher extends User {

    public Researcher(String id, String name) {
        super(id, name, "123456","Researcher");
    }

    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        ExperimentService service = new ExperimentService();

        System.out.println("1. Add Experiment");
        System.out.println("2. Update Experiment Data");
        System.out.println("0. Logout");

        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                service.addExperiment();
                break;

            case 2:
                System.out.println("Feature placeholder (UML: editExperiment)");
                break;
        }
    }
}
