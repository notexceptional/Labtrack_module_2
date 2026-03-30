package labtrack.experiments;

import java.util.ArrayList;
import java.util.List;
import labtrack.version.VersionControl;

/**
 * Model class representing a laboratory experiment.
 * Encapsulates experiment details including ID, title, and description.
 */
public class Experiment {
    private String experimentId;
    private String title;
    private String description;
    private List<VersionControl> versionHistory;

    public Experiment(String experimentId, String title, String description) {
        this.experimentId = experimentId;
        this.title = title;
        this.description = description;
        this.versionHistory = new ArrayList<>();
    }

    public String getExperimentId() {
        return experimentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<VersionControl> getVersionHistory() {
        return versionHistory;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public void addVersion(String modifiedBy, String changeLog) {
        String snapshot = "title: " + title + "\ndescription: " + description;
        VersionControl newVersion = new VersionControl(modifiedBy, snapshot, changeLog);
        versionHistory.add(newVersion);
        System.out.println("Version saved! ID: " + newVersion.getVersionID());
    }

    public void viewHistory() {
        if (versionHistory.isEmpty()) {
            System.out.println("No versions yet.");
            return;
        }
        for (VersionControl v : versionHistory) {
            System.out.println("---");
            System.out.println("Version: " + v.getVersionID());
            System.out.println("Modified by: " + v.getModifiedBy());
            System.out.println("When: " + v.getTimestamp());
            System.out.println("Change: " + v.getChangeLog());
        }
    }

    
    public boolean restoreVersion(String versionID) {
        for (VersionControl v : versionHistory) {
            if (v.getVersionID().equals(versionID)) {
                System.out.println("Restoring to version: " + versionID);
                String snapshot = v.getDataSnapshot();
                String[] snapshotLines = snapshot.split("\n");
                for (String snapshotLine : snapshotLines) {
                    if (snapshotLine.startsWith("title: ")) {
                        this.title = snapshotLine.substring(7);
                    } else if (snapshotLine.startsWith("description: ")) {
                        this.description = snapshotLine.substring(13);
                    }
                }
                return true;
            }
        }
        System.out.println("Version not found!");
        return false;
    }

    @Override
    public String toString() {
        return experimentId + "," + title + "," + description;
    }
}
