package abstract_machine;

import java.util.ArrayList;

public class Instruction {
    public String name;
    public String rawName;
    public ArrayList<Object> parameters;

    public Instruction(String name, String rawName, Object param1, Object param2) {
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
        this.parameters.add(param1);
        this.parameters.add(param2);
    }

    public Instruction(String name, String rawName, Object param1) {
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
        this.parameters.add(param1);
    }

    public Instruction(String name, String rawName) {
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public ArrayList<Object> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Object> parameters) {
        this.parameters = parameters;
    }

    public Object getFirstArgument() {
        if (getParameters().size() > 0) {
            return getParameters().get(0);
        }
        return null;
    }

    public Object getSecondArgument() {
        if (getParameters().size() > 1) {
            return getParameters().get(1);
        }
        return null;
    }
}