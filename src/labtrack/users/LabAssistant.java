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
        System.out.println();
        System.out.println("  ~~~ Usage & Tracking ~~~");
        System.out.println("  [3] Log Sample Usage (Update Quality)");
        System.out.println("  [4] Check Experiment Eligibility");
        System.out.println();
        System.out.println("  [0] Logout");
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
                sampleService.trackUsage(sc);
                break;
            case 4:
                sampleService.checkGradeEligibility(sc);
                break;
            default:
                break;
        }
    }
}