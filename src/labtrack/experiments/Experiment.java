package labtrack.experiments;

public class Experiment {
    private String experimentId;
    private String title;
    private String description;

    public Experiment(String experimentId, String title, String description) {
        this.experimentId = experimentId;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return experimentId + "," + title + "," + description;
    }
}
