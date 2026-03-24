package labtrack.version;

import java.util.Date;
import java.util.UUID;

/**
 * Model class representing a historical version of a laboratory experiment or data record.
 * Stores a snapshot of the data, the modifier, and a change log entry.
 */
public class VersionControl {
    private String versionID;
    private String dataSnapshot;
    private Date timestamp;
    private String modifiedBy;
    private String changeLog;

    public VersionControl(String modifiedBy, String dataSnapshot, String changeLog) {
        this.versionID = UUID.randomUUID().toString();
        this.dataSnapshot = dataSnapshot;
        this.timestamp = new Date();
        this.modifiedBy = modifiedBy;
        this.changeLog = changeLog;
    }

    
    public VersionControl(String versionID, String modifiedBy, String dataSnapshot, String changeLog, Date timestamp) {
        this.versionID = versionID;
        this.dataSnapshot = dataSnapshot;
        this.timestamp = timestamp;
        this.modifiedBy = modifiedBy;
        this.changeLog = changeLog;
    }

    public String getVersionID() {
        return versionID;
    }

    public String getDataSnapshot() {
        return dataSnapshot;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public static String getDiff(VersionControl v1, VersionControl v2) {
        String[] oldLines = v1.getDataSnapshot().split("\n");
        String[] newLines = v2.getDataSnapshot().split("\n");

        StringBuilder diff = new StringBuilder();
        diff.append("=== DIFF ===\n");

        int maxLines = Math.max(oldLines.length, newLines.length);
        for (int i = 0; i < maxLines; i++) {
            String oldLine;
            if (i < oldLines.length) {
                oldLine = oldLines[i];
            } else {
                oldLine = "(deleted)";
            }

            String newLine;
            if (i < newLines.length) {
                newLine = newLines[i];
            } else {
                newLine = "(added)";
            }

            if (!oldLine.equals(newLine)) {
                diff.append("- ");
                diff.append(oldLine);
                diff.append("\n");

                diff.append("+ ");
                diff.append(newLine);
                diff.append("\n");
            }
        }
        return diff.toString();
    }

    @Override
    public String toString() {
        String escapedSnapshot = dataSnapshot.replace("\n", "\\n").replace(",", "\\,");
        return versionID + "|" + modifiedBy + "|" + changeLog + "|" + timestamp.getTime() + "|" + escapedSnapshot;
    }

    public static VersionControl fromString(String line) {
        String[] parts = line.split("\\|", 5);
        if (parts.length < 5) {
            return null;
        }

        String versionID = parts[0];
        String modifiedBy = parts[1];
        String changeLog = parts[2];
        long ts = Long.parseLong(parts[3]);
        String dataSnapshot = parts[4].replace("\\n", "\n").replace("\\,", ",");

        Date timestamp = new Date(ts);
        return new VersionControl(versionID, modifiedBy, dataSnapshot, changeLog, timestamp);
    }
}