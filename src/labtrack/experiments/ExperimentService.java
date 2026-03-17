package labtrack.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.version.VersionControl;

public class ExperimentService {
    private static final String VERSIONS_FILE = "experiment_versions.csv";

    public void addExperiment(Scanner sc) {
        String id = InputHelper.readLine("Enter Experiment ID: ");

        List<String> existing = FileManager.readAllLines("experiments.csv");
        for (String line : existing) {
            String[] p = line.split(",", 3);
            if (p.length >= 1 && p[0].trim().equals(id)) {
                System.out.println("  [ERROR] Experiment ID already exists. Experiment not created.");
                return;
            }
        }

        String title = InputHelper.readLine("Enter Experiment Title: ");
        String desc = InputHelper.readLine("Enter Description: ");

        Experiment exp = new Experiment(id, title, desc);
        FileManager.write("experiments.csv", exp.toString());

        saveVersion(id, "system", "title: " + title + "\ndescription: " + desc, "Initial creation");
        System.out.println();
        System.out.println("  >>> Experiment added successfully! <<<");
        System.out.println();
    }

    public void viewExperiments() {
        List<String> lines = FileManager.readAllLines("experiments.csv");

        if (lines.isEmpty()) {
            System.out.println("  [!] No experiments found.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|               EXPERIMENTS                    |");
        System.out.println("+----------------------------------------------+");
        for (String line : lines) {
            String[] p = line.split(",", 3);
            if (p.length < 3) continue;

            System.out.println("+------------------------------------------+");
            System.out.println("| ID:          " + p[0]);
            System.out.println("| Title:       " + p[1]);
            System.out.println("| Description: " + p[2]);
            System.out.println("+------------------------------------------+");
        }
    }

    public void modifyExperiment(Scanner sc) {
        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            System.out.println("  [!] No experiments found.");
            return;
        }

        String targetId = InputHelper.readLine("Enter Experiment ID to modify: ");

        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",", 3);
            if (parts.length < 3) {
                continue;
            }

            String id = parts[0].trim();
            String currentTitle = parts[1];
            String currentDesc = parts[2];

            if (!id.equals(targetId)) {
                continue;
            }

            found = true;

            System.out.println("Current Title: " + currentTitle);
            String newTitle = InputHelper.readLine("New Title (Enter to keep): ");
            if (newTitle.trim().isEmpty()) {
                newTitle = currentTitle;
            }

            System.out.println("Current Description: " + currentDesc);
            String newDesc = InputHelper.readLine("New Description (Enter to keep): ");
            if (newDesc.trim().isEmpty()) {
                newDesc = currentDesc;
            }

            String changeLog = InputHelper.readLine("Enter change log message: ");
            if (changeLog.trim().isEmpty()) {
                changeLog = "Modified experiment";
            }

            Experiment updated = new Experiment(id, newTitle, newDesc);
            lines.set(i, updated.toString());

            saveVersion(id, "user", "title: " + newTitle + "\ndescription: " + newDesc, changeLog);
            break;
        }

        if (!found) {
            System.out.println("  [ERROR] Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", lines);
        System.out.println();
        System.out.println("  >>> Experiment updated successfully! <<<");
        System.out.println();
    }

    public void deleteExperiment(Scanner sc) {
        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            System.out.println("  [!] No experiments found.");
            return;
        }

        String targetId = InputHelper.readLine("Enter Experiment ID to delete: ");

        boolean found = false;
        List<String> kept = new ArrayList<>();
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
            System.out.println("  [ERROR] Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", kept);
        deleteVersions(targetId);
        System.out.println();
        System.out.println("  >>> Experiment deleted successfully! <<<");
        System.out.println();
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
        String expId = InputHelper.readLine("Enter Experiment ID to view history: ");

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            System.out.println("  [!] No versions yet.");
            return;
        }

        System.out.println();
        System.out.println("+----------------------------------------------+");
        System.out.println("|        VERSION HISTORY: " + expId);
        System.out.println("+----------------------------------------------+");
        for (VersionControl v : versions) {
            System.out.println("+------------------------------------------+");
            System.out.println("| Version:     " + v.getVersionID());
            System.out.println("| Modified By: " + v.getModifiedBy());
            System.out.println("| Timestamp:   " + v.getTimestamp());
            System.out.println("| Change:      " + v.getChangeLog());
            System.out.println("+------------------------------------------+");
        }
    }

    public void restoreVersion(Scanner sc) {
        String expId = InputHelper.readLine("Enter Experiment ID: ");

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            System.out.println("  [!] No versions found for this experiment.");
            return;
        }

        System.out.println();
        System.out.println("  ~~~ Available Versions ~~~");
        for (int i = 0; i < versions.size(); i++) {
            VersionControl v = versions.get(i);
            System.out.println("  [" + (i + 1) + "] " + v.getVersionID() + " - " + v.getChangeLog());
        }

        int versionNum;
        try {
            versionNum = Integer.parseInt(InputHelper.readLine("Enter version number to restore: "));
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid input.");
            return;
        }

        if (versionNum < 1 || versionNum > versions.size()) {
            System.out.println("  [ERROR] Invalid version number.");
            return;
        }

        VersionControl selected = versions.get(versionNum - 1);
        String snapshot = selected.getDataSnapshot();

        String newTitle = "";
        String newDesc = "";
        String[] snapshotLines = snapshot.split("\n");
        for (String snapshotLine : snapshotLines) {
            if (snapshotLine.startsWith("title: ")) {
                newTitle = snapshotLine.substring(7);
            } else if (snapshotLine.startsWith("description: ")) {
                newDesc = snapshotLine.substring(13);
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
            System.out.println();
            System.out.println("  *****************************************");
            System.out.println("  *   Restoring to: " + selected.getVersionID() + "   *");
            System.out.println("  *****************************************");
            System.out.println();
            System.out.println("  >>> Experiment restored successfully! <<<");
            System.out.println();
        } else {
            System.out.println("  [ERROR] Experiment not found.");
        }
    }
}