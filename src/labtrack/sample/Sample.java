package labtrack.sample;

public class Sample {
    private String sampleID;
    private String type;
    private String parentSampleID;

    public Sample(String sampleID, String type, String parentSampleID) {
        this.sampleID = sampleID;
        this.type = type;
        this.parentSampleID = parentSampleID;
    }

    public String getSampleID() {
        return sampleID;
    }

    public String getType() {
        return type;
    }

    public String getParentSampleID() {
        return parentSampleID;
    }

    public boolean hasParent() {
        return parentSampleID != null && !parentSampleID.trim().isEmpty();
    }

    public void updateType(String type) {
        this.type = type;
    }

    public void updateParentSampleID(String parentSampleID) {
        this.parentSampleID = parentSampleID;
    }
    @Override
    public String toString() {
        return safe(sampleID) + "," + safe(type) + "," + safe(parentSampleID);
    }

    public static Sample fromString(String line) {
        if (line == null) return null;
        String[] parts = line.split(",", 3);
        if (parts.length < 2) return null;
        String parent = parts.length == 3 ? parts[2] : "";
        return new Sample(parts[0], parts[1], parent);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
