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
}
