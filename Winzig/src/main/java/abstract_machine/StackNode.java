package abstract_machine;

public class StackNode {
    private String name;
    private Object value;
    private String type;

    public StackNode(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instruction convertToInstruction() {
        //TODO: Check
        return new Instruction(
            this.getName(),
            this.getName()
        );
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", type='" + type + '\'' +
                '}';
    }
}
