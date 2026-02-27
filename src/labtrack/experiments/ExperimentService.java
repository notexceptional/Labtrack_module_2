package labtrack.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.version.VersionControl;

public class ExperimentService {
    private static final String VERSIONS_FILE = "experiment_versions.csv";

    public void addExperiment(Scanner sc) {
        System.out.print("Enter Experiment ID: ");
        String id = sc.nextLine().trim();

        List<String> existing = FileManager.readAllLines("experiments.csv");
        for (String line : existing) {
            String[] p = line.split(",", 3);
            if (p.length >= 1 && p[0].trim().equals(id)) {
                System.out.println("Experiment ID already exists. Experiment not created.");
                return;
            }
        }

        System.out.print("Enter Experiment Title: ");
        String title = sc.nextLine();

        System.out.print("Enter Description: ");
        String desc = sc.nextLine();

        Experiment exp = new Experiment(id, title, desc);
        FileManager.write("experiments.csv", exp.toString());

        saveVersion(id, "system", "title: " + title + "\ndescription: " + desc, "Initial creation");
        System.out.println("");
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

    public void modifyExperiment(Scanner sc) {
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

                    System.out.print("Enter change log message: ");
                    String changeLog = sc.nextLine();
                    if (changeLog.isBlank()) changeLog = "Modified experiment";

                    Experiment updated = new Experiment(id, newTitle, newDesc);
                    lines.set(i, updated.toString());

                    saveVersion(id, "user", "title: " + newTitle + "\ndescription: " + newDesc, changeLog);
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
        deleteVersions(targetId);
        System.out.println("Experiment deleted successfully.");
    }

    private void saveVersion(String experimentId, String modifiedBy, String snapshot, String changeLog) {
        VersionControl vc = new VersionControl(modifiedBy, snapshot, changeLog);
        String line = experimentId + "~" + vc.toString();
        FileManager.write(VERSIONS_FILE, line);
    }

    private void deleteVersions(String experimentId) {
        List<String> allVersions = FileManager.readAllLines(VERSIONS_FILE);
        List<String> kept = new ArrayList<>();
        for (String line : allVersions) {
            if (!line.startsWith(experimentId + "~")) {
                kept.add(line);
            }
        }
        FileManager.overwrite(VERSIONS_FILE, kept);
    }

    private List<VersionControl> loadVersions(String experimentId) {
        List<VersionControl> versions = new ArrayList<>();
        List<String> lines = FileManager.readAllLines(VERSIONS_FILE);
        for (String line : lines) {
            if (line.startsWith(experimentId + "~")) {
                String versionData = line.substring(experimentId.length() + 1);
                VersionControl vc = VersionControl.fromString(versionData);
                if (vc != null) versions.add(vc);
            }
        }
        return versions;
    }

    public void viewVersionHistory(Scanner sc) {
        System.out.print("Enter Experiment ID to view history: ");
        String expId = sc.next();
        sc.nextLine();

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            System.out.println("No versions yet.");
            return;
        }

        System.out.println("=== Version History for " + expId + " ===");
        for (VersionControl v : versions) {
            System.out.println("---");
            System.out.println("Version: " + v.getVersionID());
            System.out.println("Modified by: " + v.getModifiedBy());
            System.out.println("When: " + v.getTimestamp());
            System.out.println("Change: " + v.getChangeLog());
        }
    }

    public void restoreVersion(Scanner sc) {
        System.out.print("Enter Experiment ID: ");
        String expId = sc.next();
        sc.nextLine();

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            System.out.println("No versions found for this experiment.");
            return;
        }

        System.out.println("Available versions:");
        for (int i = 0; i < versions.size(); i++) {
            VersionControl v = versions.get(i);
            System.out.println((i + 1) + ". " + v.getVersionID() + " - " + v.getChangeLog() + " (" + v.getTimestamp() + ")");
        }

        System.out.print("Enter version number to restore: ");
        int versionNum;
        try {
            versionNum = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }

        if (versionNum < 1 || versionNum > versions.size()) {
            System.out.println("Invalid version number.");
            return;
        }

        VersionControl selected = versions.get(versionNum - 1);
        String snapshot = selected.getDataSnapshot();

        String newTitle = "";
        String newDesc = "";
        String[] lines = snapshot.split("\n");
        for (String line : lines) {
            if (line.startsWith("title: ")) {
                newTitle = line.substring(7);
            } else if (line.startsWith("description: ")) {
                newDesc = line.substring(13);
            }
        }

        List<String> expLines = FileManager.readAllLines("experiments.csv");
        boolean found = false;
        for (int i = 0; i < expLines.size(); i++) {
            String[] p = expLines.get(i).split(",", 3);
            if (p.length >= 1 && p[0].trim().equals(expId)) {
                Experiment restored = new Experiment(expId, newTitle, newDesc);
                expLines.set(i, restored.toString());
                found = true;
                break;
            }
        }

        if (found) {
            FileManager.overwrite("experiments.csv", expLines);
            saveVersion(expId, "user", snapshot, "Restored to version " + selected.getVersionID());
            System.out.println("Restoring to version: " + selected.getVersionID());
            System.out.println("Experiment restored successfully.");
        } else {
            System.out.println("Experiment not found.");
        }
    }
}