package labtrack.users;

import labtrack.sample.SampleService;
import java.util.Scanner;

public class LabAssistant extends User {

    public LabAssistant(String id, String name) {
        super(id, name, "Lab Assistant");
    }

    @Override
    public void showMenu() {
        System.out.println("+----------------------------------------------+");
        System.out.println("|          LAB ASSISTANT DASHBOARD             |");
        System.out.println("+----------------------------------------------+");
        System.out.println();
        System.out.println("  ~~~ Sample Management ~~~");
        System.out.println("  [1] Register New Sample (Chemical/Reagent)");
        System.out.println("  [2] View All Samples");
        System.out.println("  [3] Delete a Sample");
        System.out.println();
        System.out.println("  ~~~ Usage & Tracking ~~~");
        System.out.println("  [4] Log Sample Usage (Update Quality)");
        System.out.println("  [5] Check Experiment Eligibility");
        System.out.println();
    }

    @Override
    public void handleChoice(int choice, Scanner sc) {
        SampleService sampleService = new SampleService();
        switch (choice) {
            case 1:
                sampleService.addSample(sc);
                break;
            case 2:
                sampleService.viewSamples();
                break;
            case 3:
                sampleService.deleteSample(sc); // NEW CASE
                break;
            case 4:
                sampleService.trackUsage(sc);
                break;
            case 5:
                sampleService.checkGradeEligibility(sc);
                break;
            default:
                break;
        }
    }
}