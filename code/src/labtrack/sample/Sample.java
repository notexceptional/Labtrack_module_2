package labtrack.sample;

public class Sample {
    private String id;
    private String name;
    private String type;        
    private int useCount;       
    private double quality;     
    private String grade;       

    
    public Sample(String id, String name, String type, int useCount, double quality, String grade) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.useCount = useCount;
        this.quality = quality;
        this.grade = grade;
    }

    
    @Override
    public String toString() {
        return id + "," + name + "," + type + "," + useCount + "," + quality + "," + grade;
    }

   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}