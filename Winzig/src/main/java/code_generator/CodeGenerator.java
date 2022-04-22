package code_generator;

import abstract_machine.AbstractMachine;
import abstract_machine.AttributeError;
import abstract_machine.Instruction;
import constants.StackConstants;
import constants.StackConstants.DataTypes;

import exceptions.*;
import helper.MachineLabelHelper;
import parser.TreeNode;
import parser.TreeStack;

import java.util.ArrayList;
import java.util.HashMap;

import static abstract_machine.Instruction.addRawName;
import static abstract_machine.Instruction.createInstruction;
import static code_generator.CodeGeneratorHelper.checkNodeAttributeType;
import static constants.StackConstants.Constants.Debug;

public class CodeGenerator {
    private AbstractMachine machine;
    private ArrayList<Instruction> instructions;
    private HashMap<String, Integer> instructionLabels;
    private HashMap<String, Integer> functionReturnLabels;
    private ArrayList<AttributeError> errors;
    private TreeStack ast;
    private boolean checkErrorAndContinue = false;
    private DclnTable dclnTable;
    private int next;
    private int top;
    private int localTop;
    private Boolean isLocalScope;
    private String currentProcessingFunction;
    private Boolean insideFunction;

    public CodeGenerator(TreeStack ast) {
        this.ast = ast;
        this.instructions = new ArrayList<>();
        this.machine = new AbstractMachine();
        this.dclnTable = new DclnTable();
        this.instructionLabels = new HashMap<>();
        this.functionReturnLabels = new HashMap<>();
        this.errors = new ArrayList<>();
        this.next = 0;
        this.top = 0;
        this.localTop = 0;
        this.isLocalScope = false;
        this.insideFunction = false;
    }

    public int getLocalTop() {
        return localTop;
    }

    public void setLocalTop(int localTop) {
        this.localTop = localTop;
    }

    public int getTopForSave() {
        if(getLocalScope()) {
            return this.localTop;
        }
        return this.top;
    }

    public Boolean getLocalScope() {
        return isLocalScope;
    }

    public void setLocalScope(Boolean localScope) {
        isLocalScope = localScope;
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
        incrementNext(1);
    }

    public void addInstruction(int index, Instruction instruction) {
        this.instructions.add(index, instruction);
        incrementNext(1);
    }

    public String generateLabel(int instructionIndex) {
        String label = "L" + MachineLabelHelper.generateLabel();
        instructionLabels.put(label, instructionIndex);
        return label;
    }

    public String updateLabel(String label, int index) {
        instructionLabels.put(label, index);
        return label;
    }

    public void addReturnLabel(String name, int index) {
        this.functionReturnLabels.put(name, index);
    }

    public void updateReturnLabel(String name, int index) {
        this.functionReturnLabels.put(name, index);
    }

    public ArrayList<AttributeError> getErrors() {
        return errors;
    }

    public String getCurrentProcessingFunction() {
        return currentProcessingFunction;
    }

    public void setCurrentProcessingFunction(String currentProcessingFunction) {
        this.currentProcessingFunction = currentProcessingFunction;
    }

    public void addErrors(ArrayList<AttributeError> errors) {
        getErrors().addAll(errors);
    }

    public void addError(AttributeError error) {
        getErrors().add(error);
    }

    public void incrementTop(int n) {
        this.top = this.top + n;
    }

    public void incrementTop() {
        incrementTop(1);
    }

    public void decrementTop(int n) {
        this.top = this.top - n;
    }

    public void decrementTop() {
        decrementTop(1);
    }

    public void incrementNext(int n) {
        this.next = this.next + n;
    }

    public String generateCode() throws Exception {
        generateInstructions(ast.pop());
        machine.setInstructions(this.instructions);
        machine.setInstructionLabels(this.instructionLabels);
        machine.setFunctionReturnLabels(this.functionReturnLabels);

        String key;
        Integer value;

        if (Debug) {
            System.out.println("--------------------------------------- Function Return Labels ----------------------------------------------------------");
            for (String name: this.functionReturnLabels.keySet()) {
                key = name;
                value = this.functionReturnLabels.get(name);
                System.out.println(key + " -> " + value);
            }
            System.out.println("--------------------------------------- Function Return Labels ----------------------------------------------------------");
        }

        if (Debug) {
            System.out.println("--------------------------------------- Instructions Labels ----------------------------------------------------------");
            for (String name: this.instructionLabels.keySet()) {
                key = name;
                value = this.instructionLabels.get(name);
                System.out.println(key + " -> " + value);
            }
            System.out.println("--------------------------------------- Instructions Labels ----------------------------------------------------------");
        }

        if (Debug) {
            System.out.println("===================================== DCLN Table =============================================================");
            System.out.println(dclnTable);
            System.out.println("===================================== DCLN Table =============================================================");
        }

        if (Debug) {
            System.out.println("--------------------------------------- Instructions ----------------------------------------------------------");
            for(Instruction in: this.instructions) {
                System.out.println(in);
            }
            System.out.println("--------------------------------------- Instructions ----------------------------------------------------------");
        }
        if (Debug) {
            System.out.println("-------------------------------------- Abstract Machines -----------------------------------------------------");
        }
        machine.next();
        if (Debug) {
            System.out.println("-------------------------------------- Abstract Machines -----------------------------------------------------");
        }
        return machine.getValue();
    }

    public void generateInstructions(TreeNode treeNode) throws Exception {
        if (treeNode.getChildren().isEmpty()) {
            process(treeNode);
        } else {
            if (isSpecialTreeNode(treeNode)) {
                handleSpecialNodes(treeNode);
            } else {
                for (TreeNode node : treeNode.getChildren()) {
                    generateInstructions(node);
                }
            }
            process(treeNode);
        }
    }

    private boolean isSpecialTreeNode(TreeNode treeNode) {
        if (treeNode.getName().equals(StackConstants.DataMemoryNodeNames.OutputNode
            )|| treeNode.getName().equals(StackConstants.DataMemoryNodeNames.ReadNode
            ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.IfNode
            ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.WhileNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.RepeatNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.ForNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.LoopNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.CaseNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.FunctionNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.SubProgsNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.CallNode
        ) || treeNode.getName().equals(StackConstants.DataMemoryNodeNames.AssignNode
            )) {
            return true;
        }
        return false;
    }

    private void handleSpecialNodes(TreeNode treeNode) throws Exception {
        switch (treeNode.getName()) {
            case StackConstants.DataMemoryNodeNames.OutputNode:
                processOutputNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.ReadNode:
                processReadNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.IfNode:
                processIfNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.WhileNode:
                processWhileNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.ForNode:
                processForNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.RepeatNode:
                processRepeatNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.LoopNode:
                processLoopNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.CaseNode:
                processCaseNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.FunctionNode:
                processFcnNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.SubProgsNode:
                processSubProgsNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.CallNode:
                processCallNode(treeNode);
                break;
            case StackConstants.DataMemoryNodeNames.AssignNode:
                processAssignNode(treeNode);
                break;
        }
    }

    public void process(TreeNode node) throws Exception {
        //TODO: Check Error
        if (Debug) {
            System.out.println("Executing " + node.getName());
        }
        switch (node.getName()) {
            case StackConstants.DataMemoryNodeNames.ProgramNode:
                processProgramNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ConstsNode:
                processConstsNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ConstNode:
                processConstNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.TypesNode:
                processTypesNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.TypeNode:
                processTypeNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.LitNode:
                processLitNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ParamsNode:
                processParamsNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.DclnsNode:
                processDclnsNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.VarNode:
                processVarNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.BlockNode:
                processBlockNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ExitNode:
                processExitNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ReturnNode:
                processReturnNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.NullNode:
                processNullNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.IntegerNode:
                processIntegerNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.RawIntegerNode:
                processRawIntegerNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.RawCharNode:
                processRawCharNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.StringNode:
                processStringNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.TwoDotNodeNode:
                processTwoDotNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.OtherwiseNode:
                processOtherwiseNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.SwapNode:
                processSwapNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.TrueNode:
                processTrueNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.LENode:
                processLeNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.LTNode:
                processLtNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.GENode:
                processGeNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.GTNode:
                processGtNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.EQNode:
                processEqNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.NEQNode:
                processNeqNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.PlusNode:
                processPlusNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.MinusNode:
                processMinusNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ORNode:
                processOrNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.MultNode:
                processMultiNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.DivNode:
                processDivNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ANDNode:
                processAndNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ModNode:
                processModNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.NotNode:
                processNotNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.EOFNode:
                processEofNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.SuccNode:
                processSuccNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.PredNode:
                processPredNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ChrNode:
                processChrNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.OrdNode:
                processOrdNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.IdentifierNode:
                processIdentifierNode(node);
                break;
        }
    }

    public void processProgramNode(TreeNode node) throws Exception {
        //TODO:Check
        //TODO add halt
        if (!node.getIthChild(1).getLastChild().getName().equals(
                node.getIthChild(7).getLastChild().getName())) {
            if (this.checkErrorAndContinue) {
                addError(new AttributeError("Invalid start and End name for program"));
            } else {
                throw new InvalidIdentifierException("Invalid start and End name for program");
            }
        }
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.HALTOP,
                StackConstants.AbsMachineOperations.HALTOP
        ));
        updateNode(node, 1, DataTypes.Statement);
    }

    public void processConstsNode(TreeNode node) {
        handleNop(node);
    }

    public void processConstNode(TreeNode node) throws Exception {
        //TODO: Check
        TreeNode firstChild = node.getIthChild(1);
        TreeNode secondChild = node.getLastChild();
        String identifierName = firstChild.getLastChild().getName();
        String identifierType = secondChild.getName();
        if (identifierType.contains(DataTypes.INT)) {
            identifierType = DataTypes.INT;
        } else if (identifierType.contains(DataTypes.CHAR)) {
            identifierType = DataTypes.CHAR;
        } else if (identifierType.contains(DataTypes.Identifier)) {
            identifierType = DataTypes.Identifier;
        } else {
            if (this.checkErrorAndContinue) {
                addError(new AttributeError(InvalidIdentifierException
                        .generateErrorMessage(identifierName)));
            } else {
                throw new VariableAlreadyDefinedException(InvalidIdentifierException
                        .generateErrorMessage(identifierName));
            }
        }
        DclnRow dclnRow = dclnTable.lookup(identifierName, getLocalScope());
        if (dclnRow == null) {
            //Define
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.LITOP,
                    StackConstants.AbsMachineOperations.LITOP,
                    0
            ));
            dclnTable.enter(identifierName, getTopForSave(), identifierType);
            dclnRow = dclnTable.lookup(identifierName, getLocalScope());
            updateNode(
                    node,
                    1,
                    DataTypes.Statement
            );
            // Add the value

            String value = null;
            if (identifierType.equals(DataTypes.Identifier)) {
                DclnRow dclnRow1 = dclnTable.lookup(secondChild.getLastChild().getName(), getLocalScope());
                dclnRow.setType(dclnRow1.getType());
                if (dclnRow1.getConst()) {
                    value = dclnRow1.getValue().toString();
                } else {
                    //TODO: Add a common function for put errors
                    if (this.checkErrorAndContinue) {
                        addError(new AttributeError(VariableAlreadyDefinedException
                                .generateErrorMessage(identifierName)));
                    } else {
                        throw new VariableAlreadyDefinedException(VariableAlreadyDefinedException
                                .generateErrorMessage(identifierName));
                    }
                }
            } else {
                value = secondChild.getLastChild().getName();
            }
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.LITOP,
                    addRawName(StackConstants.AbsMachineOperations.LITOP, value),
                    value
            ));
            updateNode(
                    node,
                    1,
                    identifierType
            );
            //Assign
            if (!getLocalScope()) {
                addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.SGVOP,
                    addRawName(StackConstants.AbsMachineOperations.SGVOP, String.valueOf(dclnTable.lookup(identifierName, getLocalScope()).getLocation())),
                    dclnTable.lookup(identifierName, getLocalScope()).getLocation()
                ));
            } else {
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.SLVOP,
                        addRawName(StackConstants.AbsMachineOperations.SLVOP, String.valueOf(dclnTable.lookup(identifierName, getLocalScope()).getLocation())),
                        dclnTable.lookup(identifierName, getLocalScope()).getLocation()
                ));
            }
            updateNode(
                    node,
                    -1,
                    DataTypes.Statement
            );
            dclnRow.setConst(true);
            dclnRow.setValue(value);
            node.getIthChild(1).setType(dclnTable.lookup(identifierName, getLocalScope()).getType());
        } else {
            if (this.checkErrorAndContinue) {
                addError(new AttributeError(VariableAlreadyDefinedException
                        .generateErrorMessage(identifierName)));
            } else {
                throw new VariableAlreadyDefinedException(VariableAlreadyDefinedException
                        .generateErrorMessage(identifierName));
            }
        }
    }

    public void processTypesNode(TreeNode node) {
        handleNop(node);
    }

    public void processTypeNode(TreeNode node) {
        //TODO: Implement this
        TreeNode firstChild = node.getIthChild(1);
        TreeNode secondChild = node.getIthChild(1);
        TreeNode litValue;
        firstChild.setLit(true);
        for (int i = 0; i < secondChild.getChildren().size(); i++) {
            litValue = secondChild.getIthChild(i);
            firstChild.addLitList(litValue.getLastChild().getName());
        }
    }

    public void processLitNode(TreeNode node) {
        handleNop(node);
    }

    public void processSubProgsNode(TreeNode node) throws Exception {
        String continueLabel = generateLabel(-1);
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.GOTOOP,
                StackConstants.AbsMachineOperations.GOTOOP,
                continueLabel
        ));
        for(TreeNode child: node.getChildren()) {
            generateInstructions(child);
        }
        updateLabel(continueLabel, this.next);

        updateNode(node, 0, DataTypes.Statement);
    }

    public void processFcnNode(TreeNode node) throws Exception {
        //TODO Handle functions

        //Fcn -> 'function' Name '(' Params ')' ':' Name ';' Consts Types Dclns Body Name ';

        //Params -> Dcln list ';'                                                             => "params";

        //Dclns -> 'var' (Dcln ';')+                                                          => "dclns"
        //       ->                                                                           => "dclns";

        //Dcln -> Name list ',' ':' Name'

        //Types -> 'type' (Type ';')+                                                         => "types"
        //      ->                                                                            => "types";

        //Type -> Name '=' LitList

        setLocalScope(true);
        if (!node.getIthChild(1).getLastChild().getName().equals(
                node.getLastChild().getLastChild().getName())) {
            if (this.checkErrorAndContinue) {
                addError(new AttributeError("Invalid start and End name for the function"));
            } else {
                throw new InvalidIdentifierException("Invalid start and End name for the function");
            }
        }

        String paramName, paramType;
        TreeNode paramChild;
        int localAddress;
        String funcName = node.getIthChild(1).getLastChild().getName();
        dclnTable.enterFunctionName(funcName, generateLabel(this.next));
        setCurrentProcessingFunction(funcName);
        TreeNode paramNode = node.getIthChild(2);
        dclnTable.setNumberParameters(funcName, paramNode.getChildren().size());
        //TODO: can have seperate by comma => m,n:integer
        for (int i = 1; i <= paramNode.getChildren().size(); i++) {
            localAddress = paramNode.getChildren().size() - (i);
            paramChild = paramNode.getIthChild(i);
            paramName = paramChild.getIthChild(1).getLastChild().getName();
            paramType = paramChild.getIthChild(2).getLastChild().getName();
            System.out.println("@@ " + paramName + " " + paramType);
            dclnTable.enterLocalVariable(
                    paramName, i - 1, paramType, funcName
            );
        }
        System.out.println("@@@  => " + dclnTable.getLocalVariables());
        dclnTable.setFuncReturnType(funcName, node.getIthChild(3).getLastChild().getName());
        //TODO Handle Consts, types, dclns in a function
        //TODO: Invalidate the statements after return!!!!
        generateInstructions(node.getIthChild(3));
        generateInstructions(node.getIthChild(4));
        generateInstructions(node.getIthChild(5));
        generateInstructions(node.getIthChild(6));
        generateInstructions(node.getIthChild(7));
        Instruction returnInstruction = createInstruction(
                StackConstants.AbsMachineOperations.RTNOP,
                StackConstants.AbsMachineOperations.RTNOP,
                StackConstants.FunctionConstants.returnValue,
                funcName
        );
        addInstruction(returnInstruction);
        setLocalScope(false);
    }

    public void processParamsNode(TreeNode node) {
        //TODO Handle functions
    }

    public void processDclnsNode(TreeNode node) {
        //TODO: Check-> Correct
        handleNop(node);
    }

    public void processVarNode(TreeNode node) throws Exception {
        for (int i = 1; i < node.getChildren().size(); i++) {
            String identifierName = node.getIthChild(i).getLastChild().getName();
            DclnRow dclnRow = dclnTable.lookup(identifierName, getLocalScope());
            if (dclnRow == null) {
                //TODO: Check this situation
                String type = node.getLastChild().getLastChild().getName();
                if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
                    type = DataTypes.BOOLEAN;
                }
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.LITOP,
                        StackConstants.AbsMachineOperations.LITOP,
                        0
                ));
                if (!getLocalScope()) {
                    dclnTable.enter(identifierName, getTopForSave(), type);
                } else {
                    dclnTable.enterLocalVariable(identifierName, getTopForSave(), type, getCurrentProcessingFunction());
                }
                updateNode(
                        node,
                        1,
                        DataTypes.Statement
                );
            } else {
                if (this.checkErrorAndContinue) {
                    addError(new AttributeError(VariableAlreadyDefinedException
                            .generateErrorMessage(identifierName)));
                } else {
                    throw new VariableAlreadyDefinedException(VariableAlreadyDefinedException
                            .generateErrorMessage(identifierName));
                }
            }
        }
    }

    public void processBlockNode(TreeNode node) {
        handleNop(node);
    }

    public void processOutputNode(TreeNode node) throws Exception {
        int size = node.getChildren().size();
        for (TreeNode childNode : node.getChildren()) {
            size--;
            generateInstructions(childNode);
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.SOSOP,
                    StackConstants.AbsMachineOperations.SOSOP,
                    createInstruction(
                            StackConstants.OperatingSystemOperators.OUTPUT,
                            StackConstants.OperatingSystemOperators.OUTPUT
                    )
            ));
            if (size != 0) {
                processDataTypeNode(node, DataTypes.STRING, StackConstants.Constants.PrintSeparator);
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.SOSOP,
                        StackConstants.AbsMachineOperations.SOSOP,
                        createInstruction(
                                StackConstants.OperatingSystemOperators.OUTPUT,
                                StackConstants.OperatingSystemOperators.OUTPUT
                        )
                ));
            }
        }
        updateNode(
                node,
                node.getChildren().size(),
                DataTypes.Statement
        );
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.SOSOP,
                StackConstants.AbsMachineOperations.SOSOP,
                createInstruction(
                        StackConstants.OperatingSystemOperators.OUTPUTL,
                        StackConstants.OperatingSystemOperators.OUTPUTL
                )
        ));
    }

    public void processIfNode(TreeNode node) throws Exception {
        String closeLabel = null;
        generateInstructions(node.getIthChild(1));
        String trueLabel = generateLabel(-1);
        String falseLabel = generateLabel(-1);
        if (node.getChildren().size() > 2) {
            closeLabel = generateLabel(top);
        } else {
            closeLabel = falseLabel;
        }
        addInstruction(
            createInstruction(
                StackConstants.AbsMachineOperations.CONDOP,
                StackConstants.AbsMachineOperations.CONDOP,
                trueLabel,
                falseLabel
            )
        );
        updateLabel(trueLabel, this.next);
        generateInstructions(node.getIthChild(2));
        addInstruction(
            createInstruction(
                StackConstants.AbsMachineOperations.GOTOOP,
                StackConstants.AbsMachineOperations.GOTOOP,
                closeLabel
            )
        );
        updateLabel(falseLabel, this.next);
        if (node.getChildren().size() > 2) {
            generateInstructions(node.getIthChild(3));
            updateLabel(closeLabel, this.next);
        }
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processWhileNode(TreeNode node) throws Exception {
        String startLabel = generateLabel(this.next);
        String doLabel = generateLabel(-1);
        String closeLabel = generateLabel(-1);
        generateInstructions(node.getIthChild(1));
        addInstruction(
            createInstruction(
                StackConstants.AbsMachineOperations.CONDOP,
                StackConstants.AbsMachineOperations.CONDOP,
                doLabel,
                closeLabel
            )
        );
        updateLabel(doLabel, this.next);
        generateInstructions(node.getIthChild(2));
        addInstruction(
            createInstruction(
                StackConstants.AbsMachineOperations.GOTOOP,
                StackConstants.AbsMachineOperations.GOTOOP,
                startLabel
            )
        );
        updateLabel(closeLabel, this.next);
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processRepeatNode(TreeNode node) throws Exception {
        String startLabel = generateLabel(this.next);
        String closeLabel = generateLabel(-1);
        for (int i = 1; i <= node.getChildren().size() - 1; i++) {
            generateInstructions(node.getIthChild(i));
        }
        generateInstructions(node.getLastChild());
        addInstruction(
            createInstruction(
                StackConstants.AbsMachineOperations.CONDOP,
                StackConstants.AbsMachineOperations.CONDOP,
                closeLabel,
                startLabel
            )
        );

        updateLabel(closeLabel, this.next);
        updateNode(
            node,
            -1,
            DataTypes.Statement
        );
    }

    public void processForNode(TreeNode node) throws Exception {
        generateInstructions(node.getIthChild(1));
        String startLabel = generateLabel(this.next);
        String loopLabel = generateLabel(-1);
        String closeLabel = generateLabel(-1);
        generateInstructions(node.getIthChild(2));
        addInstruction(
                createInstruction(
                        StackConstants.AbsMachineOperations.CONDOP,
                        StackConstants.AbsMachineOperations.CONDOP,
                        loopLabel,
                        closeLabel
                )
        );
        updateLabel(loopLabel, this.next);
        generateInstructions(node.getIthChild(3));
        generateInstructions(node.getIthChild(4));
        addInstruction(
                createInstruction(
                        StackConstants.AbsMachineOperations.GOTOOP,
                        StackConstants.AbsMachineOperations.GOTOOP,
                        startLabel
                )
        );
        updateLabel(closeLabel, this.next);
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processLoopNode(TreeNode node) throws Exception {
        //TODO: This may change depend on the break situation
        String startLabel = generateLabel(this.next);
        for (int i = 1; i <= node.getChildren().size(); i++) {
            generateInstructions(node.getIthChild(i));
        }
        addInstruction(
                createInstruction(
                        StackConstants.AbsMachineOperations.GOTOOP,
                        StackConstants.AbsMachineOperations.GOTOOP,
                        startLabel
                )
        );
        updateNode(
                node,
                //TODO: This may change depend on the break situation
                0,
                DataTypes.Statement
        );
    }

    public void processCaseNode(TreeNode node) throws Exception {
        //'case' Expression 'of' Caseclauses OtherwiseClause 'end'
        //Caseclauses-> (Caseclause ';')+;
        // Caseclause -> CaseExpression list ',' ':' Statement
        //CaseExpression
        //  -> ConstValue
        //  -> ConstValue '..' ConstValue

        String correctLabel, incorrectLabel, startLabel, closeLabel, caseClauseNodeValue;
        TreeNode caseClauseNode, caseClauseNodeIthChild;
        TreeNode caseValueNode = node.getIthChild(1);
        generateInstructions(caseValueNode);
        for (int i = 3; i <= node.getChildren().size() ; i++) {
            caseClauseNode = node.getIthChild(i);
            if (caseClauseNode.getName().equals(StackConstants.DataMemoryNodeNames.OtherwiseNode)) {
                continue;
            }
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.DUPOP,
                    StackConstants.AbsMachineOperations.DUPOP
            ));
            updateNode(node, 1, DataTypes.Statement);
        }
        closeLabel = generateLabel(-1);
        for (int i = 2; i <= node.getChildren().size() ; i++) {
            //TODO: Handle for more values
            caseClauseNode = node.getIthChild(i);
            if (caseClauseNode.getName().equals(StackConstants.DataMemoryNodeNames.OtherwiseNode)) {
                continue;
            }
            for (int j = 1; j < caseClauseNode.getChildren().size(); j++) {
                correctLabel = generateLabel(-1);
                incorrectLabel = generateLabel(-1);
                if (j != caseClauseNode.getChildren().size() - 1) {
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.DUPOP,
                            StackConstants.AbsMachineOperations.DUPOP
                    ));
                }
                updateNode(node, 1, DataTypes.Statement);
//                startLabel = generateLabel(this.next);
                caseClauseNodeIthChild = caseClauseNode.getIthChild(j);
                if (caseClauseNodeIthChild.getName().equals(StackConstants.DataMemoryNodeNames.TwoDotNodeNode)) {
                    generateInstructions(caseClauseNodeIthChild.getIthChild(1));
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.BOPOP,
                            StackConstants.AbsMachineOperations.BOPOP,
                            createInstruction(
                                    StackConstants.BinaryOperators.BGE,
                                    StackConstants.BinaryOperators.BGE
                            )
                    ));
                    updateNode(node, -1, DataTypes.Statement);
                    generateInstructions(caseValueNode);
                    generateInstructions(caseClauseNodeIthChild.getIthChild(2));
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.BOPOP,
                            StackConstants.AbsMachineOperations.BOPOP,
                            createInstruction(
                                    StackConstants.BinaryOperators.BLE,
                                    StackConstants.BinaryOperators.BLE
                            )
                    ));

                    updateNode(node, -1, DataTypes.Statement);
                } else {
                    int varLocation;
                    caseClauseNodeValue = caseClauseNodeIthChild.getLastChild().getName();
                    if (caseClauseNodeIthChild.getName().contains(DataTypes.Identifier)) {
                        DclnRow dclnRow = dclnTable.lookup(caseClauseNodeValue, getLocalScope());
                        if (dclnRow == null) {
                            generateOrThrowError(InvalidIdentifierException.generateErrorMessage(
                                    caseClauseNodeValue
                            ));
                        } else {
                            varLocation = dclnRow.getLocation();
                            if (!getLocalScope()) {
                                addInstruction(createInstruction(
                                        StackConstants.AbsMachineOperations.LGVOP,
                                        addRawName(StackConstants.AbsMachineOperations.LGVOP, String.valueOf(varLocation)),
                                        varLocation
                                ));
                                updateNode(node, 1, DataTypes.Statement);
                            } else {
                                addInstruction(createInstruction(
                                        StackConstants.AbsMachineOperations.LLVOP,
                                        addRawName(StackConstants.AbsMachineOperations.LLVOP, String.valueOf(varLocation)),
                                        varLocation
                                ));
                                updateNode(node, 1, DataTypes.Statement);
                            }
                        }
                    } else {
                        addInstruction(createInstruction(
                                StackConstants.AbsMachineOperations.LITOP,
                                addRawName(StackConstants.AbsMachineOperations.LITOP, caseClauseNodeValue),
                                caseClauseNodeValue
                        ));
                        updateNode(node, 1, DataTypes.Statement);
                    }
                }
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.BOPOP,
                        StackConstants.AbsMachineOperations.BOPOP,
                        createInstruction(
                                StackConstants.BinaryOperators.BEQ,
                                StackConstants.BinaryOperators.BEQ
                        )
                ));
                updateNode(node, -1, DataTypes.Statement);
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.CONDOP,
                        StackConstants.AbsMachineOperations.CONDOP,
                        correctLabel,
                        incorrectLabel
                ));
                updateNode(node, -1, DataTypes.Statement);
                updateLabel(correctLabel, this.next);
                generateInstructions(caseClauseNode.getLastChild());
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.GOTOOP,
                        StackConstants.AbsMachineOperations.GOTOOP,
                        closeLabel
                ));
                updateLabel(incorrectLabel, this.next);
            }
        }
        if (node.getLastChild().getName().equals(StackConstants.DataMemoryNodeNames.OtherwiseNode)) {
            generateInstructions(node.getLastChild());
        }
        updateLabel(closeLabel, this.next);
    }

    public void processReadNode(TreeNode node) {
        for (int i = 1; i <= node.getChildren().size(); i++) {
            //TODO: Make a common methos along with assign, const functions!!!
            String identifierName = node.getIthChild(i).getLastChild().getName();

            DclnRow dclnRow = dclnTable.lookup(identifierName, getLocalScope());
            if (dclnRow == null) {
                //TODO: Check this situation
//                String type = node.getLastChild().getType();
//                if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
//                    type = DataTypes.BOOLEAN;
//                }
//                dclnTable.enter(identifierName, top, type);
//                updateNode(
//                        node,
//                        DataTypes.Statement
//                );
//                node.getIthChild(1).setType(type);
            } else {
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.SOSOP,
                        StackConstants.AbsMachineOperations.SOSOP,
                        createInstruction(
                                StackConstants.OperatingSystemOperators.INPUT,
                                StackConstants.OperatingSystemOperators.INPUT
                        )
                ));
                if (!getLocalScope()) {
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.SGVOP,
                            addRawName(StackConstants.AbsMachineOperations.SGVOP, String.valueOf(dclnRow.getLocation())),
                            dclnRow.getLocation()
                    ));
                } else {
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.SLVOP,
                            addRawName(StackConstants.AbsMachineOperations.SLVOP, String.valueOf(dclnRow.getLocation())),
                            dclnRow.getLocation()
                    ));
                }
                updateNode(
                        node,
                        DataTypes.Statement
                );
                node.getIthChild(1).setType(dclnRow.getType());
            }
        }
        updateNode(
                node,
                0,
                //TODO: Check a method to make it progress for char and string as well!.
                DataTypes.Other
        );
    }

    public void processExitNode(TreeNode node) {
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.HALTOP,
                StackConstants.AbsMachineOperations.HALTOP
                ));
        updateNode(
                node,
                DataTypes.Other
        );
    }

    public void processReturnNode(TreeNode node) {
        //TODO Related to function. Check It
    }

    public void processNullNode(TreeNode node) {
        //TODO: Check the Implementation
        handleNop(node);
        updateNode(
                node,
                DataTypes.NULL
        );
    }

    public void processIntegerNode(TreeNode node) {
//        processDataTypeNode(node, DataTypes.INT, node.getLastChild().getName());
        updateNode(
                node,
                DataTypes.INT
        );
    }

    public void processRawIntegerNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.INT, node.getLastChild().getName());
    }

    public void processRawCharNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.CHAR, node.getLastChild().getName());
    }

    public void processStringNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.STRING, node.getLastChild().getLastChild().getName());
    }

    public void processCaseClauseNode(TreeNode node) {
        //want, if and else, more
    }

    public void processTwoDotNode(TreeNode node) {
        //1..50 -> 6 - > true
    }

    public void processOtherwiseNode(TreeNode node) {
        handleNop(node);
    }

    public void processAssignNode(TreeNode node) throws Exception {
        //TODO: Handle Litlist
        String identifierName = node.getIthChild(1).getLastChild().getName();
        DclnRow dclnRow = dclnTable.lookup(identifierName, getLocalScope());
        generateInstructions(node.getLastChild());
        if (dclnRow == null) {
            //TODO: Check this situation
            String type = node.getLastChild().getType();
            if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
                type = DataTypes.BOOLEAN;
            }
            dclnTable.enter(identifierName, getTopForSave(), type);
            updateNode(
                    node,
                    DataTypes.Statement
            );
            node.getIthChild(1).setType(type);
        } else {
            if (!getLocalScope()) {
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.SGVOP,
                        addRawName(StackConstants.AbsMachineOperations.SGVOP, String.valueOf(dclnRow.getLocation())),
                        dclnRow.getLocation()
                ));
                updateNode(
                        node,
                        -1,
                        DataTypes.Statement
                );
            } else {
                addInstruction(createInstruction(
                        StackConstants.AbsMachineOperations.SLVOP,
                        addRawName(StackConstants.AbsMachineOperations.SLVOP, String.valueOf(dclnRow.getLocation())),
                        dclnRow.getLocation()
                ));
                updateNode(
                        node,
                        -1,
                        DataTypes.Statement
                );
            }
            node.getIthChild(1).setType(dclnRow.getType());
        }
    }

    public void processSwapNode(TreeNode node) throws Exception {
        String leftName = node.getIthChild(1).getLastChild().getName();
        String rightName = node.getIthChild(2).getLastChild().getName();
        DclnRow dclnRowLeft = dclnTable.lookup(leftName, getLocalScope());
        DclnRow dclnRowRight = dclnTable.lookup(rightName, getLocalScope());
        if (!dclnRowLeft.getType().equals(dclnRowRight.getType())) {
            throw new InvalidIdentifierException("Can not assign " + leftName + " to " + rightName);
        }
        int temp = dclnRowLeft.getLocation();
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.SWAPOP,
                StackConstants.AbsMachineOperations.SWAPOP
        ));
        dclnRowLeft.setLocation(dclnRowRight.getLocation());
        dclnRowRight.setLocation(temp);
        updateNode(
                node,
                DataTypes.Statement
        );
    }

    public void processTrueNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.BOOLEAN, true);
    }

    private void processLeNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BLE, DataTypes.INT, DataTypes.BOOLEAN);
    }

    private void processLtNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BLT, DataTypes.INT, DataTypes.BOOLEAN);
    }

    private void processGeNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BGE, DataTypes.INT, DataTypes.BOOLEAN);
    }

    private void processGtNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BGT, DataTypes.INT, DataTypes.BOOLEAN);
    }

    private void processEqNode(TreeNode node) throws Exception {
        processBinaryNodesForMoreTypes(node, StackConstants.BinaryOperators.BEQ, DataTypes.INT, DataTypes.CHAR, DataTypes.BOOLEAN);
    }

    private void processNeqNode(TreeNode node) throws Exception {
        processBinaryNodesForMoreTypes(node, StackConstants.BinaryOperators.BNE, DataTypes.INT, DataTypes.CHAR, DataTypes.BOOLEAN);
    }

    private void processPlusNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BPLUS, DataTypes.INT, DataTypes.INT);
    }

    private void processMinusNode(TreeNode node) throws Exception {
        if (node.getChildren().size() == 1) {
            processUnaryNodes(node, StackConstants.UnaryOperators.UNEG, DataTypes.INT, DataTypes.INT);
        } else {
            processBinaryNodes(node, StackConstants.BinaryOperators.BMINUS, DataTypes.INT, DataTypes.INT);
        }
    }

    private void processOrNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BOR, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processMultiNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BMULT, DataTypes.INT, DataTypes.INT);
    }

    private void processDivNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BDIV, DataTypes.INT, DataTypes.INT);
    }

    private void processAndNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BAND, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processModNode(TreeNode node) throws Exception {
        processBinaryNodes(node, StackConstants.BinaryOperators.BMOD, DataTypes.INT, DataTypes.INT);
    }

    private void processNotNode(TreeNode node) throws Exception {
        processUnaryNodes(node, StackConstants.UnaryOperators.UNOT, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processEofNode(TreeNode node) {
        if (checkErrorAndContinue || node.getChildren().isEmpty()) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.SOSOP,
                    StackConstants.AbsMachineOperations.SOSOP,
                    createInstruction(
                        StackConstants.OperatingSystemOperators.EOF,
                        StackConstants.OperatingSystemOperators.EOF
                    )
            ));
            updateNode(
                    node,
                    1,
                    DataTypes.Other
            );
        } else {
            handleError();
        }
    }

    private void processCallNode(TreeNode node) throws Exception {
        //TODO Related to function. Check It
        //Take the stack top ( for Call Operation) before call the child nodes
        String funcName = node.getIthChild(1).getLastChild().getName();
        int localStartIndex = this.top;
        System.out.println("##### " + localStartIndex);

//        setLocalScope(true);
        for (int i = 2; i <= node.getChildren().size(); i++) {
            if (false && node.getIthChild(i).getName().equals(StackConstants.DataMemoryNodeNames.IdentifierNode)) {

            } else {
                generateInstructions(node.getIthChild(i));
            }
        }
        String funcLabel = dclnTable.getFunctionLabel(funcName);
        if (funcLabel != null) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.CODEOP,
                    StackConstants.AbsMachineOperations.CODEOP,
                    funcLabel
            ));
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.CALLOP,
                    StackConstants.AbsMachineOperations.CALLOP,
                    localStartIndex
            ));
            System.out.println("#### " + this.top);
            addReturnLabel(funcName, this.next);

        } else {
            generateOrThrowError(
                    InvalidIdentifierException.generateErrorMessage(funcName)
            );
        }
//        setLocalScope(false);
    }

    private void processSuccNode(TreeNode node) throws Exception {
        processUnaryNodes(node, StackConstants.UnaryOperators.USUCC, DataTypes.INT, DataTypes.INT);
    }

    private void processPredNode(TreeNode node) throws Exception {
        processUnaryNodes(node, StackConstants.UnaryOperators.UPRED, DataTypes.INT, DataTypes.INT);
    }

    private void processChrNode(TreeNode node) throws Exception {
        if (checkErrorAndContinue || checkErrorsAndContinue(node, DataTypes.INT)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.CHROP,
                    StackConstants.AbsMachineOperations.CHROP
            ));
            updateNode(
                    node,
                    DataTypes.CHAR
            );
        } else {
            handleError();
        }
    }

    private void processOrdNode(TreeNode node) throws Exception {
        if (checkErrorAndContinue || checkErrorsAndContinue(node, DataTypes.CHAR)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.ORDOP,
                    StackConstants.AbsMachineOperations.ORDOP
            ));
            updateNode(
                    node,
                    DataTypes.INT
            );
        } else {
            handleError();
        }
    }

    private void processIdentifierNode(TreeNode node) {
        if (node.getLastChild().getName().equals(StackConstants.Constants.TrueIdentifier) ||
                node.getLastChild().getName().equals(StackConstants.Constants.FalseIdentifier)) {
            node.setType(DataTypes.BOOLEAN);
        } else {
            DclnRow dclnRow = dclnTable.lookup(node.getLastChild().getName(), getLocalScope());
            if (dclnRow != null) {
                node.setType(dclnRow.getType());
                if (!getLocalScope()) {
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.LGVOP,
                            StackConstants.AbsMachineOperations.LGVOP,
                            dclnRow.getLocation()
                    ));
                    updateNode(
                            node,
                            1,
                            dclnRow.getType()
                    );
                } else {
                    addInstruction(createInstruction(
                            StackConstants.AbsMachineOperations.LLVOP,
                            StackConstants.AbsMachineOperations.LLVOP,
                            dclnRow.getLocation()
                    ));
                    updateNode(
                            node,
                            1,
                            dclnRow.getType()
                    );
                }
            }
        }
        node.setType(DataTypes.Identifier);
    }

    //Helper Functions

    private void processBinaryNodesForMoreTypes(TreeNode node, String innerInstruction,
                    String inputtype1, String inputtype2, String outputtype) throws Exception {
        if (checkErrorAndContinue || checkErrorsAndContinue(node, inputtype1, inputtype2)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.BOPOP,
                    StackConstants.AbsMachineOperations.BOPOP,
                    createInstruction(
                            innerInstruction,
                            innerInstruction
                    )
            ));
            updateNode(
                    node,
                    - 1,
                    outputtype
            );
        } else {
            handleError();
        }
    }

    private void processBinaryNodes(TreeNode node, String innerInstruction, String inputtype, String outputtype)
            throws Exception {
        //TODO Update for functions
        if (true || checkErrorAndContinue || checkErrorsAndContinue(node, inputtype)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.BOPOP,
                    StackConstants.AbsMachineOperations.BOPOP,
                    createInstruction(
                            innerInstruction,
                            innerInstruction
                    )
            ));
            updateNode(
                    node,
                    - 1,
                    outputtype
            );
        } else {
            handleError();
        }
    }

    private void processDataTypeNode(TreeNode node, String type, Object value) {
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.LITOP,
                addRawName(StackConstants.AbsMachineOperations.LITOP, value.toString()),
                value
        ));
        updateNode(
                node,
                1,
                type
        );
    }

    private void processUnaryNodes(TreeNode node, String innerInstruction, String inputtype, String outputtype)
            throws Exception {
        //TODO Update for functions
        if (true || checkErrorAndContinue || checkErrorsAndContinue(node, inputtype)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.UOPOP,
                    StackConstants.AbsMachineOperations.UOPOP,
                    createInstruction(
                            innerInstruction,
                            innerInstruction
                    )
            ));
            updateNode(
                    node,
                    outputtype
            );
        } else {
            handleError();
        }
    }

    public void handleNop(TreeNode node) {
        //TODO: ADD LATER
//        addInstruction(createInstruction(
//                StackConstants.AbsMachineOperations.NOP,
//                StackConstants.AbsMachineOperations.NOP
//        ));
        updateNode(
                node,
                DataTypes.Statement
        );
    }

    public boolean checkErrorsAndContinue(TreeNode node, String type) throws Exception {
        return checkErrorsAndContinue(node, type, null);
    }

    public boolean checkErrorsAndContinue(TreeNode node, String type1, String type2) throws Exception {
        ArrayList<AttributeError> generatedErrors;
        if (type2 == null) {
            generatedErrors = checkNodeAttributeType(node, type1, dclnTable, getLocalScope());
        } else {
            generatedErrors = checkNodeAttributeType(node, type1, type2, dclnTable, getLocalScope());
        }
        if(generatedErrors == null) {
            return true;
        }
        if (!this.checkErrorAndContinue) {
            //TODO: Throw the correct error Type
            throw new InvalidOperationException(
                    generatedErrors.get(0).getMessage()
            );
        } else {
            addErrors(generatedErrors);
        }
        return false;
    }

    public void updateNode(TreeNode node, int incrementTop, String type) {
        if (!getLocalScope()) {
            incrementTop(incrementTop);
        } else {
            this.localTop = this.localTop + incrementTop;
        }
        node.setType(type);
        node.setTop(getTopForSave());
        node.setNext(this.next);
        if (Debug) {
            System.out.println(node.getName() + " increment top by " + incrementTop + ".124 Now top = " + this.top + "\n Local top = " + this.localTop);
        }
    }

    public void updateNode(TreeNode node, String type) {
        node.setType(type);
        node.setTop(getTopForSave());
        node.setNext(this.next);
        if (Debug) {
            System.out.println(node.getName() + " increment top by " + 0 + ". 124Now top = " + this.top);
        }
    }


    private void handleError() {
        for (AttributeError error: getErrors()) {
            System.out.println(error.getMessage());
        }
        System.exit(0);
    }

    //TODO: Move to the other similar code blocks as well
    private void generateOrThrowError(String message) throws Exception {
        if (this.checkErrorAndContinue) {
            addError(new AttributeError(message));
        } else {
            //TODO: Add the Error Types
            throw new VariableAlreadyDefinedException(message);
        }
    }

    //TODO: Move to the other similar code blocks as well
    private String getNodeType(String nodeName) throws Exception {
        if (nodeName.contains(DataTypes.INT)) {
            return DataTypes.INT;
        } else if (nodeName.contains(DataTypes.CHAR)) {
            return DataTypes.CHAR;
        } else if (nodeName.contains(DataTypes.Identifier)) {
            return DataTypes.Identifier;
        } else {
            generateOrThrowError(nodeName);
            return null;
        }
    }

    @Override
    public String toString() {
        String s = "The begining of the Instructions\n";
        Instruction instructiion;
        for (int i = 0; i < this.instructions.size(); i++) {
            instructiion = this.instructions.get(i);
            s += instructiion.toString();
            s+= "\n";
        }
        s += "The End of the Instructions\n";
        return s;
    }
}
