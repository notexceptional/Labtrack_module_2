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
}
