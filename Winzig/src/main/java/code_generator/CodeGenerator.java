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
    }

    private void processLtNode(TreeNode node) {
    }

    private void processGeNode(TreeNode node) {
    }

    private void processGtNode(TreeNode node) {
    }

    private void processEqNode(TreeNode node) {
    }

    private void processNeqNode(TreeNode node) {
    }

    private void processPlusNode(TreeNode node) {
        if (checkErrorAndContinue || checkErrorsAndContinue(node)) {
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.BOPOP,
                    StackConstants.AbsMachineOperations.BOPOP,
                    createInstruction(
                            StackConstants.BinaryOperators.BPLUS,
                            StackConstants.BinaryOperators.BPLUS
                    )
            ));
            updateNode(
                node,
                node.getLastChild().getTop() - 1,
                node.getLastChild().getNext() + 1,
                DataTypes.INT
            );
        }
    }

    private void processMinusNode(TreeNode node) {
        if (checkErrorAndContinue || checkErrorsAndContinue(node)) {
            //TODO: Make plus and minus to a one function
            addInstruction(createInstruction(
                    StackConstants.AbsMachineOperations.BOPOP,
                    StackConstants.AbsMachineOperations.BOPOP,
                    createInstruction(
                            StackConstants.BinaryOperators.BMINUS,
                            StackConstants.BinaryOperators.BMINUS
                    )
            ));
            updateNode(
                node,
                node.getLastChild().getTop() - 1,
                node.getLastChild().getNext() + 1,
                DataTypes.INT
            );
        }
    }

    private void processOrNode(TreeNode node) {
    }

    private void processMultiNode(TreeNode node) {
    }

    private void processDivNode(TreeNode node) {
    }

    private void processAndNode(TreeNode node) {
    }

    private void processModNode(TreeNode node) {
    }

    private void processNotNode(TreeNode node) {
    }

    private void processEofNode(TreeNode node) {
    }

    private void processCallNode(TreeNode node) {
    }

    private void processSuccNode(TreeNode node) {
    }

    private void processPredNode(TreeNode node) {
    }

    private void processChrNode(TreeNode node) {
    }

    private void processOrdNode(TreeNode node) {
    }

    public boolean checkErrorsAndContinue(TreeNode node) {
        ArrayList<AttributeError> errors = checkNodeAttributeType(node, DataTypes.INT);
        if(errors == null) {
            return true;
        }
        addErrors(errors);
        return false;
    }

    public void updateNode(TreeNode node, int top, int next, String type) {
        node.setType(type);
        node.setTop(top);
        node.setNext(next);
    }
}
