package code_generator;

import abstract_machine.AbstractMachine;
import abstract_machine.AttributeError;
import abstract_machine.Instruction;
import constants.StackConstants;
import constants.StackConstants.DataTypes;

import parser.TreeNode;
import parser.TreeStack;

import java.util.ArrayList;

import static abstract_machine.Instruction.createInstruction;
import static code_generator.CodeGeneratorHelper.checkNodeAttributeType;

public class CodeGenerator {
    private AbstractMachine machine;
    private ArrayList<Instruction> instructions;
    private ArrayList<AttributeError> errors;
    private TreeStack ast;
    private boolean checkErrorAndContinue = false;
//    private int next;
//    private int top;

    public CodeGenerator(TreeStack ast) {
        this.ast = ast;
        instructions = new ArrayList<>();
        machine = new AbstractMachine();
//        this.next = 0;
//        this.top = 0;
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
//        incrementNext(1);
    }

    public ArrayList<AttributeError> getErrors() {
        return errors;
    }

    public void addErrors(ArrayList<AttributeError> errors) {
        getErrors().addAll(errors);
    }

//    public void incrementTop(int n) {
//        this.top = this.top + n;
//    }
//
//    public void incrementTop() {
//        incrementTop(1);
//    }
//
//    public void decrementTop(int n) {
//        this.top = this.top - n;
//    }
//
//    public void decrementTop() {
//        decrementTop(1);
//    }
//
//    public void incrementNext(int n) {
//        this.next = this.next + n;
//    }

    public void generateCode() {
        generateCode(ast.pop());
    }

    public void generateCode(TreeNode treeNode) {
        if (treeNode.getChildren().isEmpty()) {
            process(treeNode);
        } else {
            for (TreeNode node : treeNode.getChildren()) {
                generateCode(node);
            }
        }
    }

    public void process(TreeNode node) {
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
            case StackConstants.DataMemoryNodeNames.OutputNode:
                processOutputNode(node);
                break;
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
            case StackConstants.DataMemoryNodeNames.StringNode:
                processStringNode(node);
                break;
            case StackConstants.DataMemoryNodeNames.CaseClauseNode:
                processcaseClauseNode(node);
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
        }
    }

    public void processProgramNode(TreeNode node) {

    }

    public void processConstsNode(TreeNode node) {

    }

    public void processConstNode(TreeNode node) {

    }

    public void processTypesNode(TreeNode node) {

    }

    public void processTypeNode(TreeNode node) {

    }

    public void processLitNode(TreeNode node) {

    }

    public void processSubProgsNode(TreeNode node) {

    }

    public void processFcnNode(TreeNode node) {

    }

    public void processParamsNode(TreeNode node) {

    }

    public void processDclnsNode(TreeNode node) {

    }

    public void processVarNode(TreeNode node) {

    }

    public void processBlockNode(TreeNode node) {

    }

    public void processOutputNode(TreeNode node) {

    }

    public void processIfNode(TreeNode node) {

    }

    public void processWhileNode(TreeNode node) {

    }

    public void processRepeatNode(TreeNode node) {

    }

    public void processForNode(TreeNode node) {

    }

    public void processLoopNode(TreeNode node) {

    }

    public void processCaseNode(TreeNode node) {

    }

    public void processReadNode(TreeNode node) {

    }

    public void processExitNode(TreeNode node) {

    }

    public void processReturnNode(TreeNode node) {

    }

    public void processNullNode(TreeNode node) {

    }

    public void processIntegerNode(TreeNode node) {

    }

    public void processStringNode(TreeNode node) {

    }

    public void processcaseClauseNode(TreeNode node) {

    }

    public void processTwoDotNode(TreeNode node) {

    }

    public void processOtherwiseNode(TreeNode node) {

    }

    public void processAssignNode(TreeNode node) {

    }

    public void processSwapNode(TreeNode node) {

    }

    public void processTrueNode(TreeNode node) {

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
    }

    private void processCallNode(TreeNode node) {
    }

    private void processSuccNode(TreeNode node) {
        processUnaryNodes(node, StackConstants.UnaryOperators.USUCC, DataTypes.INT, DataTypes.INT);
    }

    private void processPredNode(TreeNode node) {
        processUnaryNodes(node, StackConstants.UnaryOperators.UPRED, DataTypes.INT, DataTypes.INT);
    }

    private void processChrNode(TreeNode node) {
    }

    private void processOrdNode(TreeNode node) {
    }

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
                    node.getLastChild().getTop() - 1,
                    node.getLastChild().getNext() + 1,
                    outputtype
            );
        } else {
            handleError();
        }
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
                    node.getLastChild().getTop(),
                    node.getLastChild().getNext() + 1,
                    outputtype
            );
        } else {
            handleError();
        }
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

    public void updateNode(TreeNode node, int top, int next, String type) {
        node.setType(type);
        node.setTop(top);
        node.setNext(next);
    }


    private void handleError() {
        for (AttributeError error: getErrors()) {
            System.out.println(error.getMessage());
        }
        System.exit(0);
    }
}
