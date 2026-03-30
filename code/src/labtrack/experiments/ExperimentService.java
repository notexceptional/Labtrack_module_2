package labtrack.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import labtrack.util.FileManager;
import labtrack.util.InputHelper;
import labtrack.util.Colors;
import labtrack.util.TablePrinter;
import labtrack.version.VersionControl;

/**
 * Service class for managing laboratory experiments.
 * This class handles experiment CRUD operations and integrates with 
 * a custom Version Control system for tracking research history.
 */
public class ExperimentService {
    private static final String VERSIONS_FILE = "experiment_versions.csv";

    public void addExperiment(Scanner sc) {
        String id = InputHelper.readLine("Enter Experiment ID: ");

        List<String> existing = FileManager.readAllLines("experiments.csv");
        for (String line : existing) {
            String[] p = line.split(",", 3);
            if (p.length >= 1 && p[0].trim().equals(id)) {
                Colors.error("Experiment ID already exists. Experiment not created.");
                return;
            }
        }

        String title = InputHelper.readLine("Enter Experiment Title: ");
        String desc = InputHelper.readLine("Enter Description: ");

        Experiment exp = new Experiment(id, title, desc);
        FileManager.write("experiments.csv", exp.toString());

        saveVersion(id, "system", "title: " + title + "\ndescription: " + desc, "Initial creation");
        System.out.println();
        Colors.success("Experiment added successfully!");
        System.out.println();
    }

    public void viewExperiments() {
        String[] headers = {"ID", "Title", "Description"};
        TablePrinter.printCsvAsTable("All Experiments", "experiments.csv", headers);
        InputHelper.pressEnterToContinue();
    }

    public void modifyExperiment(Scanner sc) {
        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            Colors.warning("No experiments found.");
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

            System.out.println(Colors.colorize("Current Title: ", Colors.BLUE) + currentTitle);
            String newTitle = InputHelper.readLine("New Title (Enter to keep): ");
            if (newTitle.trim().isEmpty()) {
                newTitle = currentTitle;
            }

            System.out.println(Colors.colorize("Current Description: ", Colors.BLUE) + currentDesc);
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
            Colors.error("Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", lines);
        System.out.println();
        Colors.success("Experiment updated successfully!");
        System.out.println();
    }

    public void deleteExperiment(Scanner sc) {
        List<String> lines = FileManager.readAllLines("experiments.csv");
        if (lines.isEmpty()) {
            Colors.warning("No experiments found.");
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
            Colors.error("Experiment not found.");
            return;
        }

        FileManager.overwrite("experiments.csv", kept);
        deleteVersions(targetId);
        System.out.println();
        Colors.success("Experiment deleted successfully!");
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

    /**
     * Views the full version history for a specific experiment
     * and allows the user to restore a previous state.
     */
    public void viewVersionHistory(Scanner sc) {
        String expId = InputHelper.readLine("Enter Experiment ID to view history: ");

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            Colors.warning("No versions found for ID: " + expId);
            return;
        }

        String[] headers = {"Ver ID", "User", "Timestamp", "Log"};
        List<String[]> rows = new ArrayList<>();
        for (VersionControl v : versions) {
            rows.add(new String[]{v.getVersionID(), v.getModifiedBy(), String.valueOf(v.getTimestamp()), v.getChangeLog()});
        }
        TablePrinter.printTable("Version History: " + expId, headers, rows);
        InputHelper.pressEnterToContinue();
    }

    public void restoreVersion(Scanner sc) {
        String expId = InputHelper.readLine("Enter Experiment ID: ");

        List<VersionControl> versions = loadVersions(expId);
        if (versions.isEmpty()) {
            Colors.warning("No versions found for this experiment.");
            return;
        }

        System.out.println();
        System.out.println("  ~~~ Available Versions ~~~");
        for (int i = 0; i < versions.size(); i++) {
            VersionControl v = versions.get(i);
            System.out.println("  [" + (i + 1) + "] " + v.getVersionID() + " - " + v.getChangeLog());
        }

        String verRaw = InputHelper.readLine("Enter version number to restore: ");
        int ver;
        try {
            ver = Integer.parseInt(verRaw);
        } catch (NumberFormatException e) {
            Colors.error("Invalid input.");
            return;
        }

        if (ver < 1 || ver > versions.size()) {
            Colors.error("Invalid version number.");
            return;
        }

        VersionControl selected = versions.get(ver - 1);
        String snapshot = selected.getDataSnapshot();

        String newTitle = "";
        String newDesc = "";
        // Reconstructs the experiment object from a historical snapshot
        String[] snapParts = snapshot.split("\n");
        for (String snapshotLine : snapParts) {
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
            Colors.success("Experiment restored successfully!");
            System.out.println();
        } else {
            Colors.error("Experiment not found.");
        }
    }
}