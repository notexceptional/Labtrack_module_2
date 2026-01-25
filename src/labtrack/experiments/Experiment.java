package labtrack.experiments;

public class Experiment {

    // existing fields
    private String experimentId;
    private String title;
    private String description;

    // UML-aligned additions (non-breaking)
    private String data;        // for experimental data
    private int version = 1;    // simple versioning

    public Experiment(String experimentId, String title, String description) {
        this.experimentId = experimentId;
        this.title = title;
        this.description = description;
        this.data = "";
    }

    // UML: updateData()
    public void updateData(String data) {
        this.data = data;
        version++; // simple version increment
    }

    // UML: status()
    public String status() {
        return "Version: " + version;
    }

    @Override
    public String toString() {
        return experimentId + "," + title + "," + description + "," + version;
    }
}
