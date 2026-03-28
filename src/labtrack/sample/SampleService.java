package labtrack.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;

public class SampleService {
    private static final String FILE_NAME = "samples.csv";

    public void addSample(Scanner sc) {
        String id = InputHelper.readLine("Enter Sample ID: ");

        List<String> existing = FileManager.readAllLines(FILE_NAME);
        for (String line : existing) {
            String[] p = line.split(",");
            if (p.length >= 1 && p[0].trim().equals(id)) {
                System.out.println("\n  [ERROR] Sample ID '" + id + "' already exists. Registration cancelled.");
                return;
            }
        }

        String name = InputHelper.readLine("Enter Sample Name: ");
        String type = InputHelper.readLine("Enter Type (Chemical/Reagent/Catalyst): ");

        Sample sample = new Sample(id, name, type, 0, 100.0, "High Sensitivity");
        FileManager.write(FILE_NAME, sample.toString());

        System.out.println("\n  >>> Sample registered successfully! <<<");
    }

    public void deleteSample(Scanner sc) {
        String targetId = InputHelper.readLine("Enter Sample ID to delete: ");
        List<String> lines = FileManager.readAllLines(FILE_NAME);
        List<String> keptLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            String[] p = line.split(",");
            if (p[0].equals(targetId)) {
                found = true;
                continue;
            }
            keptLines.add(line);
        }

        if (found) {
            FileManager.overwrite(FILE_NAME, keptLines);
            System.out.println("\n  >>> Sample '" + targetId + "' deleted successfully. <<<");
        } else {
            System.out.println("\n  [ERROR] Sample ID not found.");
        }
    }

    public void viewSamples() {
        List<String> lines = FileManager.readAllLines(FILE_NAME);
        if (lines.isEmpty()) {
            System.out.println("  [!] No samples in inventory.");
            return;
        }

        System.out.println("\n--- Current Inventory ---");
        for (String line : lines) {
            String[] p = line.split(",");
            System.out.printf("ID: %s | Name: %s | Uses: %s | Quality: %s%% | Grade: %s\n",
                    p[0], p[1], p[3], p[4], p[5]);
        }
    }

    public void trackUsage(Scanner sc) {
        String id = InputHelper.readLine("Enter Sample ID used: ");
        List<String> lines = FileManager.readAllLines(FILE_NAME);
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split(",");
            if (p[0].equals(id)) {
                found = true;
                int newCount = Integer.parseInt(p[3]) + 1;
                double newQuality = Double.parseDouble(p[4]) - 5.0; 

                
                String newGrade = "High Sensitivity";
                if (newQuality < 50.0) newGrade = "Low Sensitivity";
                else if (newQuality < 80.0) newGrade = "Medium Sensitivity";

                Sample updated = new Sample(p[0], p[1], p[2], newCount, Math.max(0, newQuality), newGrade);
                lines.set(i, updated.toString());
                break;
            }
        }

        if (found) {
            FileManager.overwrite(FILE_NAME, lines);
            System.out.println("  >>> Usage logged. Sample quality updated. <<<");
        } else {
            System.out.println("  [ERROR] Sample ID not found.");
        }
    }

    public void checkGradeEligibility(Scanner sc) {
        String id = InputHelper.readLine("Enter Sample ID: ");
        List<String> lines = FileManager.readAllLines(FILE_NAME);

        for (String line : lines) {
            String[] p = line.split(",");
            if (p[0].equals(id)) {
                System.out.println("\n  Result: This sample is currently graded for [" + p[5] + "] experiments.");
                return;
            }
        }
        System.out.println("  [ERROR] Sample not found.");
    }
}