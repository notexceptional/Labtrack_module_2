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
}
