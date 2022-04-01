package code_generator;

import abstract_machine.AbstractMachine;
import abstract_machine.AttributeError;
import abstract_machine.Instruction;
import abstract_machine.StackNode;
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

public class CodeGenerator {
    private AbstractMachine machine;
    private ArrayList<Instruction> instructions;
    private HashMap<String, Integer> instructionLabels;
    private ArrayList<AttributeError> errors;
    private TreeStack ast;
    private boolean checkErrorAndContinue = false;
    private DclnTable dclnTable;
    private int next;
    private int top;

    public CodeGenerator(TreeStack ast) {
        this.ast = ast;
        this.instructions = new ArrayList<>();
        this.machine = new AbstractMachine();
        this.dclnTable = new DclnTable();
        this.instructionLabels = new HashMap<>();
        this.next = 0;
        this.top = 0;
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

    public ArrayList<AttributeError> getErrors() {
        return errors;
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

    public String generateCode() throws VariableAlreadyDefinedException, InvalidIdentifierException,
            InvalidValueForIdentifierException, InvalidUserInputException, InvalidOperationException {
        generateInstructions(ast.pop());
        machine.setInstructions(this.instructions);
        System.out.println("-------------------------------------- Abstract Machines -----------------------------------------------------");
        machine.next();
        System.out.println("-------------------------------------- Abstract Machines -----------------------------------------------------");
        return machine.getValue();
    }

    public void generateInstructions(TreeNode treeNode) throws VariableAlreadyDefinedException,
            InvalidIdentifierException, InvalidValueForIdentifierException {
        if (treeNode.getChildren().isEmpty()) {
            System.out.println("Now Processing " + treeNode);
            process(treeNode);
            System.out.println(this);
        } else {
            if (isSpecialTreeNode(treeNode)) {
                System.out.println("Now Processing " + treeNode.getName());
                handleSpecialNodes(treeNode);
                System.out.println(this);
            } else {
                for (TreeNode node : treeNode.getChildren()) {
                    generateInstructions(node);
                }
            }
            System.out.println("Now Processing " + treeNode.getName());
            process(treeNode);
            System.out.println(this);
        }
    }

    private boolean isSpecialTreeNode(TreeNode treeNode) {
        if (treeNode.getName().equals(StackConstants.DataMemoryNodeNames.OutputNode)) {
            return true;
        }
        return false;
    }

    private void handleSpecialNodes(TreeNode treeNode) throws VariableAlreadyDefinedException,
            InvalidValueForIdentifierException, InvalidIdentifierException {
        switch (treeNode.getName()) {
            case StackConstants.DataMemoryNodeNames.OutputNode:
                processOutputNode(treeNode);
                break;
        }
    }

    public void process(TreeNode node) throws VariableAlreadyDefinedException, InvalidIdentifierException, InvalidValueForIdentifierException {
        //TODO: Check Error
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
            case StackConstants.DataMemoryNodeNames.SubProgsNode:
                processSubProgsNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.FunctionNode:
                processFcnNode(node);
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
//            case StackConstants.DataMemoryNodeNames.OutputNode:
//                processOutputNode(node);
//                break;
            case StackConstants.DataMemoryNodeNames.IfNode:
                processIfNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.WhileNode:
                processWhileNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.RepeatNode:
                processRepeatNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.For:
                processForNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.LoopNode:
                processLoopNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.CaseNode:
                processCaseNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.ReadNode:
                processReadNode(node);
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
            case StackConstants.DataMemoryNodeNames.StringNode:
                processStringNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.CaseClauseNode:
                processCaseClauseNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.TwoDotNodeNode:
                processTwoDotNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.OtherwiseNode:
                processOtherwiseNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.AssignNode:
                processAssignNode(node);
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
            case StackConstants.DataMemoryNodeNames.CallNode:
                processCallNode(node);
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

    public void processProgramNode(TreeNode node) throws InvalidIdentifierException {
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
        handleNop(node);
    }

    public void processConstsNode(TreeNode node) {
        handleNop(node);
    }

    public void processConstNode(TreeNode node) throws InvalidIdentifierException, InvalidValueForIdentifierException {
        TreeNode firstChild = node.getIthChild(1);
        if (!(firstChild.getType().equals(DataTypes.INT) || firstChild.getType().equals(DataTypes.BOOLEAN))
                || firstChild.getType().equals(DataTypes.CHAR) || firstChild.getType().equals(DataTypes.Identifier)) {
            if (this.checkErrorAndContinue) {
                addError(new AttributeError(VariableAlreadyDefinedException
                        .generateErrorMessage("CONST " + firstChild.getName())));
            } else {
                throw new InvalidIdentifierException(
                        InvalidIdentifierException.generateErrorMessage("CONST " + firstChild.getName())
                );
            }
        }
        if (firstChild.isLit()) {
            if (!(firstChild.getLitlist().contains(node.getIthChild(2).getLastChild().getName()))) {
                if (this.checkErrorAndContinue) {
                    addError(new AttributeError(InvalidValueForIdentifierException
                            .generateErrorMessage(firstChild.getLastChild().getName(),
                                    node.getIthChild(2).getLastChild().getName())));
                } else {
                    throw new InvalidValueForIdentifierException(
                        InvalidValueForIdentifierException.
                        generateErrorMessage(firstChild.getLastChild().getName(),
                        node.getIthChild(2).getLastChild().getName()));
                }
            }
        }
        String identifierName = firstChild.getName();
        DclnRow dclnRow = dclnTable.lookup(identifierName);
        if (dclnRow == null) {
            //TODO: Check this situation
            String type = node.getLastChild().getType();
            if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
                if (this.checkErrorAndContinue) {
                    addError(new AttributeError(InvalidIdentifierException
                            .generateErrorMessage(identifierName)));
                } else {
                    throw new InvalidIdentifierException(InvalidIdentifierException
                            .generateErrorMessage(identifierName));
                }
            }
            dclnTable.enter(identifierName, top, type);
            updateNode(
                    node,
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
    }

    public void processTypesNode(TreeNode node) {
        handleNop(node);
    }

    public void processTypeNode(TreeNode node) {
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

    public void processSubProgsNode(TreeNode node) {
        //TODO Handle functions
    }

    public void processFcnNode(TreeNode node) {
        //TODO Handle functions
    }

    public void processParamsNode(TreeNode node) {
        //TODO Handle functions
    }

    public void processDclnsNode(TreeNode node) {
        //TODO: Check-> Correct
        handleNop(node);
    }

    public void processVarNode(TreeNode node) throws VariableAlreadyDefinedException {
        for (int i = 1; i <= node.getChildren().size(); i++) {
            String identifierName = node.getIthChild(i).getName();
            DclnRow dclnRow = dclnTable.lookup(identifierName);
            if (dclnRow == null) {
                //TODO: Check this situation
                String type = node.getLastChild().getLastChild().getName();
                if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
                    type = DataTypes.BOOLEAN;
                }
                dclnTable.enter(identifierName, top, type);
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

    public void processOutputNode(TreeNode node) throws VariableAlreadyDefinedException,
            InvalidValueForIdentifierException, InvalidIdentifierException {
        for (TreeNode childNode : node.getChildren()) {
            generateInstructions(childNode);
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.SOSOP,
                    StackConstants.AbsMachineOperations.SOSOP,
                    createInstruction(
                            StackConstants.OperatingSystemOperators.OUTPUT,
                            StackConstants.OperatingSystemOperators.OUTPUT
                    )
            ));
        }
        updateNode(
                node,
                node.getChildren().size(),
                DataTypes.Statement
        );
        processDataTypeNode(node, DataTypes.STRING, StackConstants.Constants.LineSeparator);
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.SOSOP,
                StackConstants.AbsMachineOperations.SOSOP,
                createInstruction(
                        StackConstants.OperatingSystemOperators.OUTPUT,
                        StackConstants.OperatingSystemOperators.OUTPUT
                )
        ));
    }

    public void processIfNode(TreeNode node) {
        int trueIndex = node.getIthChild(1).getNext() + 1;
        int falseIndex = node.getIthChild(2).getNext() + 2;
        int closeIndex = node.getLastChild().getNext();
        String closeLabel = generateLabel(closeIndex + 1);
        if (node.getChildren().size() > 2) {
            falseIndex = node.getIthChild(3).getNext() + 1;
        }
        addInstruction(trueIndex + 1,
            createInstruction(
                StackConstants.AbsMachineOperations.GOTOOP,
                StackConstants.AbsMachineOperations.GOTOOP,
                closeLabel
            ));
        addInstruction(createInstruction(
            StackConstants.AbsMachineOperations.CONDOP,
            addRawName(StackConstants.AbsMachineOperations.CONDOP, generateLabel(trueIndex), generateLabel(falseIndex)),
            generateLabel(trueIndex),
            generateLabel(falseIndex),
            closeLabel
        ));
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processWhileNode(TreeNode node) {
        int startIndex = node.getIthChild(1).getNext() + 1;
        int closeIndex = node.getLastChild().getNext();
        String closeLabel = generateLabel(closeIndex + 1);
        //TODO Check the implementation
        addInstruction(closeIndex + 1,
                createInstruction(
                        StackConstants.AbsMachineOperations.GOTOOP,
                        StackConstants.AbsMachineOperations.GOTOOP,
                        closeLabel
                ));
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.CONDOP,
                addRawName(StackConstants.AbsMachineOperations.CONDOP,
                        generateLabel(startIndex), generateLabel(closeIndex)),
                generateLabel(startIndex),
                generateLabel(closeIndex),
                closeLabel
        ));
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processRepeatNode(TreeNode node) {
        //TODO: Check the Machine Instructions
        int startIndex = node.getIthChild(1).getNext();
        int closeIndex = node.getLastChild().getNext() + 1;
        String closeLabel = generateLabel(closeIndex + 1);
        //TODO Check the implementation
        addInstruction(closeIndex + 1,
                createInstruction(
                        StackConstants.AbsMachineOperations.GOTOOP,
                        StackConstants.AbsMachineOperations.GOTOOP,
                        closeLabel
                ));
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.CONDOP,
                addRawName(StackConstants.AbsMachineOperations.CONDOP,
                        generateLabel(startIndex), generateLabel(closeIndex)),
                generateLabel(startIndex),
                generateLabel(closeIndex),
                closeLabel
        ));
        updateNode(
                node,
                -1,
                DataTypes.Statement
        );
    }

    public void processForNode(TreeNode node) {

    }

    public void processLoopNode(TreeNode node) {

    }

    public void processCaseNode(TreeNode node) {

    }

    public void processReadNode(TreeNode node) {
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.SOSOP,
                StackConstants.AbsMachineOperations.SOSOP,
                createInstruction(
                        StackConstants.OperatingSystemOperators.INPUT,
                        StackConstants.OperatingSystemOperators.INPUT
                )
        ));
        updateNode(
                node,
                1,
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
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.NOP,
                StackConstants.AbsMachineOperations.NOP
        ));
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

    public void processStringNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.STRING, node.getLastChild().getName());
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

    public void processAssignNode(TreeNode node) {
        //TODO: Handle Litlist
        String identifierName = node.getIthChild(1).getName();
        DclnRow dclnRow = dclnTable.lookup(identifierName);
        if (dclnRow == null) {
            //TODO: Check this situation
            String type = node.getLastChild().getType();
            if (node.getLastChild().getType() != null && node.getLastChild().getType().equals(DataTypes.BOOLEAN)) {
                type = DataTypes.BOOLEAN;
            }
            dclnTable.enter(identifierName, top, type);
            updateNode(
                    node,
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
    }

    public void processSwapNode(TreeNode node) {
        addInstruction(createInstruction(
                StackConstants.AbsMachineOperations.SWAPOP,
                StackConstants.AbsMachineOperations.SWAPOP
        ));
        updateNode(
                node,
                DataTypes.Statement
        );
    }

    public void processTrueNode(TreeNode node) {
        processDataTypeNode(node, DataTypes.BOOLEAN, true);
    }

    private void processLeNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BLE, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processLtNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BLT, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processGeNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BGE, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processGtNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BGT, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processEqNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BEQ, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processNeqNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BNE, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processPlusNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BPLUS, DataTypes.INT, DataTypes.INT);
    }

    private void processMinusNode(TreeNode node) {
        if (node.getChildren().size() == 1) {
            processUnaryNodes(node, StackConstants.UnaryOperators.UNEG, DataTypes.INT, DataTypes.INT);
        } else {
            processBinaryNodes(node, StackConstants.BinaryOperators.BMINUS, DataTypes.INT, DataTypes.INT);
        }
    }

    private void processOrNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BOR, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processMultiNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BMULT, DataTypes.INT, DataTypes.INT);
    }

    private void processDivNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BDIV, DataTypes.INT, DataTypes.INT);
    }

    private void processAndNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BAND, DataTypes.BOOLEAN, DataTypes.BOOLEAN);
    }

    private void processModNode(TreeNode node) {
        processBinaryNodes(node, StackConstants.BinaryOperators.BMOD, DataTypes.INT, DataTypes.INT);
    }

    private void processNotNode(TreeNode node) {
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

    private void processCallNode(TreeNode node) {
        //TODO Related to function. Check It
    }

    private void processSuccNode(TreeNode node) {
        processUnaryNodes(node, StackConstants.UnaryOperators.USUCC, DataTypes.INT, DataTypes.INT);
    }

    private void processPredNode(TreeNode node) {
        processUnaryNodes(node, StackConstants.UnaryOperators.UPRED, DataTypes.INT, DataTypes.INT);
    }

    private void processChrNode(TreeNode node) {
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

    private void processOrdNode(TreeNode node) {
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
        }
        node.setType(DataTypes.Identifier);
    }

    //Helper Functions

    private void processBinaryNodes(TreeNode node, String innerInstruction, String inputtype, String outputtype) {
        if (checkErrorAndContinue || checkErrorsAndContinue(node, inputtype)) {
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
        System.out.println("This is " + type + " Node handle, The value is " + value);
//        if (type.equals(DataTypes.INT) && value.equals(StackConstants.DataMemoryNodeNames.RawIntegerNode)) {
//            value = node.getLastChild().getLastChild().getName();
//            System.out.println("Integer Special:- This is " + type + " Node handle, The value is " + value);
//        }
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

    private void processUnaryNodes(TreeNode node, String innerInstruction, String inputtype, String outputtype) {
        if (checkErrorAndContinue || checkErrorsAndContinue(node, inputtype)) {
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

    public boolean checkErrorsAndContinue(TreeNode node, String type) {
        return checkErrorsAndContinue(node, type, null);
    }

    public boolean checkErrorsAndContinue(TreeNode node, String type1, String type2) {
        ArrayList<AttributeError> generatedErrors;
        if (type2 == null) {
            generatedErrors = checkNodeAttributeType(node, type1);
        } else {
            generatedErrors = checkNodeAttributeType(node, type1, type2);
        }
        if(generatedErrors == null) {
            return true;
        }
        addErrors(generatedErrors);
        return false;
    }

    public void updateNode(TreeNode node, int incrementTop, String type) {
        incrementTop(incrementTop);
        node.setType(type);
        node.setTop(this.top);
        node.setNext(this.next);
    }

    public void updateNode(TreeNode node, String type) {
        node.setType(type);
        node.setTop(this.top);
        node.setNext(this.next);
    }


    private void handleError() {
        for (AttributeError error: getErrors()) {
            System.out.println(error.getMessage());
        }
        System.exit(0);
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
