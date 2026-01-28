package labtrack.experiments;

import labtrack.util.FileManager;
import java.util.List;
import java.util.Scanner;

public class ExperimentService {

    public void addExperiment() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Experiment ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Experiment Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        Experiment exp = new Experiment(id, title, desc);
        FileManager.write("experiments.csv", exp.toString());

        System.out.println(" Experiment added successfully");
    }

    public void viewExperiments() {
        List<String> lines = FileManager.readAllLines("experiments.csv");

        if (lines.isEmpty()) {
            System.out.println("No experiments found.");
            return;
        }

        System.out.println("=== Experiments ===");
        for (String line : lines) {
            String[] p = line.split(",", 3);
            if (p.length < 3) continue;

            System.out.println("ID: " + p[0]);
            System.out.println("Title: " + p[1]);
            System.out.println("Description: " + p[2]);
            System.out.println("--------------------");
        }
    }

    public void modifyExperiment() {
        Scanner sc = new Scanner(System.in);

        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            System.out.println("No experiments found.");
            return;
        }

        System.out.print("Enter Experiment ID to modify: ");
        String targetId = sc.nextLine().trim();

        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",", 3);
            if (p.length < 3) continue;

            String id = p[0].trim();
            String currentTitle = p[1];
            String currentDesc = p[2];

            if (id.equals(targetId)) {
                found = true;

                System.out.println("Current Title: " + currentTitle);
                System.out.print("New Title (Enter to keep): ");
                String newTitle = sc.nextLine();
                if (newTitle.isBlank()) newTitle = currentTitle;

                System.out.println("Current Description: " + currentDesc);
                System.out.print("New Description (Enter to keep): ");
                String newDesc = sc.nextLine();
                if (newDesc.isBlank()) newDesc = currentDesc;

                Experiment updated = new Experiment(id, newTitle, newDesc);
                lines.set(i, updated.toString());
                break;
            }
        }

        if (!found) {
            System.out.println(" Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", lines);
        System.out.println(" Experiment updated successfully");
    }

    public void deleteExperiment(Scanner sc) {
        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            System.out.println("No experiments found.");
            return;
        }

        System.out.print("Enter Experiment ID to delete: ");
        String targetId = sc.next();

        boolean found = false;
        List<String> kept = new java.util.ArrayList<>();
        for (String line : lines) {
            String[] p = line.split(",", 3);
            if (p.length < 3) continue;
            String id = p[0].trim();
            if (id.equals(targetId)) {
                found = true;
                continue;
            }
            kept.add(line);
        }

        if (!found) {
            System.out.println("Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", kept);
        System.out.println("Experiment deleted successfully.");
    }
}