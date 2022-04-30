package abstract_machine;

import helper.MachineLabelHelper;

import java.util.ArrayList;

public class Instruction {
    public int id;
    public String name;
    public String rawName;
    public ArrayList<Object> parameters;

    public Instruction(String name, String rawName, Object param1, Object param2) {
        this.id = MachineLabelHelper.generateInstructureIndex();
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
        this.parameters.add(param1);
        this.parameters.add(param2);
    }

    public Instruction(String name, String rawName, Object param1, Object param2, Object param3) {
        this.id = MachineLabelHelper.generateInstructureIndex();
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
        this.parameters.add(param1);
        this.parameters.add(param2);
        this.parameters.add(param3);
    }

    public Instruction(String name, String rawName, Object param1) {
        this.id = MachineLabelHelper.generateInstructureIndex();
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
        this.parameters.add(param1);
    }

    public Instruction(String name, String rawName) {
        this.id = MachineLabelHelper.generateInstructureIndex();
        this.name = name;
        this.rawName = rawName;
        this.parameters = new ArrayList<>();
    }

    public static Instruction createInstruction(String name, String rawName) {
        return new Instruction(name, rawName);
    }

    public static Instruction createInstruction(String name) {
        return new Instruction(name, name);
    }

    public static Instruction createInstruction(String name, String rawName, Object param1, Object param2) {
        return new Instruction(name, rawName, param1, param2);
    }

    public static Instruction createInstruction(String name, String rawName, Object param1,
                                                Object param2, Object param3) {
        return new Instruction(name, rawName, param1, param2, param3);
    }

    public static Instruction createInstruction(String name, String rawName, Object param1) {
        return new Instruction(name, rawName, param1);
    }

    public void addParameter(Object param1) {
        this.parameters.add(param1);
    }

    public void addParameter(Object param1, Object param2) {
        addParameter(param1);
        addParameter(param2);
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

    public static String addRawName(String name, String argument) {
        return name + " " + argument;
    }

    public static String addRawName(String name, String argument1, String argument2) {
        return name + " " + argument1 + " " + argument2;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "id= '" + this.id + "\'" +
                "rawName='" + rawName + '\'' +
                "Name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
//        return "";
    }
}