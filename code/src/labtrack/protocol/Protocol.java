package labtrack.protocol;

public class Protocol {
    private String protocolID;
    private String title;
    private String steps;
    private boolean approved;

    public Protocol(String protocolID, String title, String steps) {
        this.protocolID = protocolID;
        this.title = title;
        this.steps = steps;
        this.approved = false;
    }

    public void approve() {
        approved = true;
    }

    public void updateSteps(String steps) {
        this.steps = steps;
    }

    public String getProtocolID() {
        return protocolID;
    }

    public String getTitle() {
        return title;
    }

    public String getSteps() {
        return steps;
    }

    public boolean isApproved() {
        return approved;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void revokeApproval() {
        approved = false;
    }
    @Override
    public String toString() {
        return safe(protocolID) + "," + safe(title) + "," + safe(steps) + "," + approved;
    }

    public static Protocol fromString(String line) {
        if (line == null) return null;

        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;

        Protocol p = new Protocol(parts[0], parts[1], parts[2]);
        if (parts.length == 4) {
            p.approved = Boolean.parseBoolean(parts[3]);
        }
        return p;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
