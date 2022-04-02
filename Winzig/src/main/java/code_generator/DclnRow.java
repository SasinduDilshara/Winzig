package code_generator;

public class DclnRow {
    private String name;
    private int location;
    private String type;

    public DclnRow(String name, int location, String type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DclnRow{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", type='" + type + '\'' +
                '}';
    }
}
