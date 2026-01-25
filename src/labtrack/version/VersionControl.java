package labtrack.version;

public class VersionControl {
    private String versionID;
    private String timestamp;
    private String modifiedBy;
    private String changeLog;

    public VersionControl(String versionID, String modifiedBy, String changeLog) {
        this.versionID = versionID;
        this.modifiedBy = modifiedBy;
        this.changeLog = changeLog;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getVersionID() {
        return versionID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getChangeLog() {
        return changeLog;
    }
    @Override
    public String toString() {
        return safe(versionID) + "," + safe(timestamp) + "," + safe(modifiedBy) + "," + safe(changeLog);
    }

    public static VersionControl fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;

        VersionControl vc = new VersionControl(parts[0], parts[2], parts.length == 4 ? parts[3] : "");
        if (parts.length >= 2) {
            vc.timestamp = parts[1];
        }
        return vc;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
