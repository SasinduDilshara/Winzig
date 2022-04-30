package code_generator;

import exceptions.InvalidIdentifierException;

import java.util.HashMap;

public class DclnTable {
    private HashMap<String, DclnRow> rows;
    private HashMap<String, HashMap<String, DclnRow>> localVariables;
    private HashMap<String, DclnRow> localVarNames;
    private HashMap<String, String> funcLabels;
    private HashMap<String, String> funcReturnTypes;
    private HashMap<String, Integer> funcParameterCount;

    public DclnTable() {
        this.rows = new HashMap<>();
        this.localVariables = new HashMap<>();
        this.funcLabels = new HashMap<>();
        this.localVarNames = new HashMap<>();
        this.funcReturnTypes = new HashMap<>();
        this.funcParameterCount = new HashMap<>();
    }

    public HashMap<String, DclnRow> getRows() {
        return rows;
    }

    public HashMap<String, HashMap<String, DclnRow>> getLocalVariables() {
        return localVariables;
    }

    public HashMap<String, DclnRow> getLocalVarNames() {
        return localVarNames;
    }

    public HashMap<String, String> getFuncLabels() {
        return funcLabels;
    }

    public HashMap<String, String> getFuncReturnTypes() {
        return funcReturnTypes;
    }

    public void enter(String name, int location, String type) {
        this.rows.put(name, new DclnRow(
            name,
            location,
            type
        ));
    }

    public void enterLocalVariable(String name, int location, String type, String localFuncName) throws Exception {
        if (!this.localVariables.containsKey(localFuncName)) {
            throw new InvalidIdentifierException(localFuncName);
        }
        if (this.localVariables.get(localFuncName).containsKey(name)) {
            throw new InvalidIdentifierException(name + " in " + localFuncName);
        }
        this.localVariables.get(localFuncName).put(name, new DclnRow(
                name,
                location,
                type
        ));
        this.localVarNames.put(name, new DclnRow(
                name,
                location,
                type
            ));
    }

    public void enterFunctionName(String name, String label) {
        this.funcLabels.put(name, label);
        this.localVariables.put(name, new HashMap<>());
    }

    public String getFunctionLabel(String name) {
         if (this.funcLabels.containsKey(name)) {
             return this.funcLabels.get(name);
         }
         return null;
    }

    public DclnRow lookup(String name, boolean isLocal) {
        if (!isLocal){
            return this.rows.get(name);
        } else {
            return this.localVarNames.get(name);
        }
    }

    public void setFuncReturnType(String funcName, String type) {
        this.funcReturnTypes.put(funcName, type);
    }

    public boolean checkReturnType(String funcName, String type) throws Exception {
        if (funcReturnTypes.containsKey(funcName)) {
            return funcReturnTypes.get(funcName).equals(type);
        } else {
            throw new InvalidIdentifierException(funcName);
        }
    }

    public void setNumberParameters(String funcName, int parameterCount) {
        this.funcParameterCount.put(funcName, parameterCount);
    }

    public int getNumberParameters(String funcName) {
        return this.funcParameterCount.get(funcName);
    }

    @Override
    public String toString() {
        return "DclnTable{" +
                "\nrows=" + rows +
                "\n, localVariables=" + localVariables +
                "\n, localVarNames=" + localVarNames +
                "\n, funcLabels=" + funcLabels +
                "\n, funcReturnTypes=" + funcReturnTypes +
                '}';
    }
}
