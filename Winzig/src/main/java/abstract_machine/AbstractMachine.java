package abstract_machine;

import constants.StackConstants;
import exceptions.InvalidOperationException;
import exceptions.InvalidUserInputException;

import java.util.ArrayList;
import java.util.HashMap;

public class AbstractMachine {
    public DataMemory dataMemory;
    public ReturnMemory returnMemory;
    public int pc;
    public ArrayList<Instruction> instructions;
    private HashMap<String, Integer> instructionLabels;

    public AbstractMachine() {
        this.dataMemory = new DataMemory();
        this.returnMemory = new ReturnMemory();
        this.pc = 1;
        this.instructions = new ArrayList<>();
    }

    public AbstractMachine(ArrayList<Instruction> instructions, HashMap<String, Integer> instructionLabels) {
        this.dataMemory = new DataMemory();
        this.returnMemory = new ReturnMemory();
        this.pc = 1;
        this.instructions = instructions;
        this.instructionLabels = instructionLabels;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void incrementPc() {
        this.pc++;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Instruction getNextInstruction() {
        if (this.pc > getInstructions().size()) {
            return null;
        }
        return getInstructions().get(this.pc - 1);
    }

    public String getValue() {
        if (dataMemory.getStackSize() == 0) {
            return "";
        }
        return dataMemory.popLf().getValue().toString();
    }

    public void addInstruction(Instruction instruction) {
        this.instructions.add(instruction);
    }

    public void next() throws InvalidOperationException, InvalidUserInputException {
        Instruction instruction = getNextInstruction();
        if (instruction == null) {
            return;
        }
        next(instruction);
    }

    public void next(Instruction instruction) throws InvalidOperationException, InvalidUserInputException {
        switch (instruction.getName()) {
            case StackConstants.AbsMachineOperations.NOP:
                nopHandler();
                break;
            case StackConstants.AbsMachineOperations.HALTOP:
                haltHandler();
                return;
            case StackConstants.AbsMachineOperations.LITOP:
                litHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.LLVOP:
                llvHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.LGVOP:
                lgvHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.SLVOP:
                slvHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.SGVOP:
                sgvHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.LLAOP:
                llaHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.LGAOP:
                lgaHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.UOPOP:
                uopHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.BOPOP:
                bopHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.POPOP:
                popHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.DUPOP:
                dupHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.SWAPOP:
                swapHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.CALLOP:
                //TODO: Need to update the array, Check it. Unless it is a error
                instruction = callHandler(instruction);
                next(instruction);
                return;
            case StackConstants.AbsMachineOperations.RTNOP:
                rtnHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.GOTOOP:
                instruction = gotoHandler(instruction);
                next(instruction);
                return;
            case StackConstants.AbsMachineOperations.CONDOP:
                condHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.CODEOP:
                codeHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.SOSOP:
                sosHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.LIMITOP:
                limitHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.CHROP:
                chrHandler(instruction);
                break;
            case StackConstants.AbsMachineOperations.ORDOP:
                ordHandler(instruction);
                break;
        }
        System.out.println("stack after :" + instruction);
        System.out.println(dataMemory.stack);
        incrementPc();
        next();
    }

    private void nopHandler() {
        // Do Nothing
    }

    private void haltHandler() {
        // Do Nothing
    }

    private void litHandler(Instruction instruction) {
        dataMemory.pushLf(new StackNode(
                instruction.getRawName(),
                instruction.getFirstArgument(),
                StackConstants.DataTypes.INT
        ));
    }

    private void llvHandler(Instruction instruction) {
        dataMemory.pushLf(
                dataMemory.lf((Integer) instruction.getFirstArgument())
        );
    }

    private void lgvHandler(Instruction instruction) {
        dataMemory.pushLf(
                dataMemory.gf((Integer) instruction.getFirstArgument())
        );
    }

    private void slvHandler(Instruction instruction) {
        //TODO: Check the update Implementation
        dataMemory.updateStack(
            dataMemory.lfGetIndex((Integer) instruction.getFirstArgument()),
            dataMemory.popLf()
        );
    }

    private void sgvHandler(Instruction instruction) {
        //TODO: Check the update Implementation
        dataMemory.updateStack(
                dataMemory.gfGetIndex((Integer) instruction.getFirstArgument()),
                dataMemory.popLf()
        );
    }

    private void llaHandler(Instruction instruction) {
        dataMemory.pushLf(
            new StackNode(
                instruction.getRawName(),
                dataMemory.getLocalAddress((Integer) instruction.getFirstArgument()),
                StackConstants.DataTypes.INT
            )
        );
    }

    private void lgaHandler(Instruction instruction) {
        dataMemory.pushLf(
            new StackNode(
                instruction.getRawName(),
                dataMemory.getGlobalAddress((Integer) instruction.getFirstArgument()),
                StackConstants.DataTypes.INT
            )
        );
    }

    private void uopHandler(Instruction instruction) throws InvalidOperationException {
        StackNode x = dataMemory.popLf();
        int value;
        String type;
        String uop = instruction.getFirstArgument().toString();
        if (uop.equals(StackConstants.UnaryOperators.UNOT)) {
            value = dataMemory.Unop(
                    (Instruction) instruction.getFirstArgument(), StackHelper.convertIntToBoolean(x.getValue())
            );
            type = StackConstants.DataTypes.BOOLEAN;
        } else {
            value = dataMemory.Unop(
                    (Instruction) instruction.getFirstArgument(), Integer.valueOf(x.getValue().toString())
            );
            type = StackConstants.DataTypes.BOOLEAN;
        }
        dataMemory.pushLf(
            new StackNode(
                instruction.getRawName(),
                value,
                type
            )
        );
    }

    private void bopHandler(Instruction instruction) throws InvalidOperationException {
        StackNode x = dataMemory.popLf();
        StackNode y = dataMemory.popLf();
        int value;
        Instruction bop = (Instruction) instruction.getFirstArgument();
        String type;
        if (bop.getName().equals(StackConstants.BinaryOperators.BOR) ||
                bop.getName().equals(StackConstants.BinaryOperators.BAND)) {
            value = dataMemory.Binop(
                    bop,
                    StackHelper.convertIntToBoolean(x.getValue()),
                    StackHelper.convertIntToBoolean(y.getValue())
            );
        } else {
            value = dataMemory.Binop(
                    bop,
                    Integer.valueOf(x.getValue().toString()),
                    Integer.valueOf(y.getValue().toString())
            );
        }
        if (bop.getName().equals(StackConstants.BinaryOperators.BPLUS) ||
                bop.getName().equals(StackConstants.BinaryOperators.BMINUS) ||
                bop.getName().equals(StackConstants.BinaryOperators.BMULT) ||
                bop.getName().equals(StackConstants.BinaryOperators.BDIV) ||
                bop.getName().equals(StackConstants.BinaryOperators.BMOD)) {
            type = StackConstants.DataTypes.INT;
        } else {
            type = StackConstants.DataTypes.BOOLEAN;
        }
        dataMemory.pushLf(
                new StackNode(
                        instruction.getRawName(),
                        value,
                        type
                )
        );
    }

    private void popHandler(Instruction instruction) {
        for (int i = 0; i < ((int) instruction.getFirstArgument()); i++) {
            dataMemory.popLf();
        }
    }

    private void dupHandler(Instruction instruction) {
        dataMemory.pushLf(
            dataMemory.topLf()
        );
    }

    private void swapHandler(Instruction instruction) {
        StackNode first = dataMemory.popLf();
        StackNode second = dataMemory.popLf();
        dataMemory.pushLf(
            first
        );
        dataMemory.pushLf(
            second
        );
    }

    private Instruction callHandler(Instruction instruction) {
        int n = (int) instruction.getFirstArgument();
        returnMemory.pushStack(instruction);
        instruction = dataMemory.popLf().convertToInstruction();
        dataMemory.openFrame(n);
        return instruction;
    }

    private void rtnHandler(Instruction instruction) {
        int n = (int) instruction.getFirstArgument();
        int start = dataMemory.depthLf() - n;
        if (start > 0) {
            for (int j = 0; j < n; j++) {
                dataMemory.swapTwoStackNodes(dataMemory.lfGetIndex(j), dataMemory.lfGetIndex(start + j));
            }
            dataMemory.removeStack(start);
        }
        instruction = returnMemory.popReturnStack();
        dataMemory.closeFrame((Integer) instruction.getFirstArgument());
    }

    private Instruction gotoHandler(Instruction instruction) {
        String label = (String) instruction.getFirstArgument();
        setPc(instructionLabels.get(label));
        return instructions.get(instructionLabels.get(label));
    }

    private Instruction condHandler(Instruction instruction) {
        StackNode first = dataMemory.popLf();
        String label;
        if (first.getType().equals(StackConstants.DataTypes.BOOLEAN)
                && ((int) first.getValue()) == 1){
             label = (String) instruction.getFirstArgument();
             setPc(instructionLabels.get(label));
             return instructions.get(instructionLabels.get(label));
        } else if (first.getType().equals(StackConstants.DataTypes.BOOLEAN)
                && ((int) first.getValue()) == 0){
            label = (String) instruction.getSecondArgument();
            setPc(instructionLabels.get(label));
            return instructions.get(instructionLabels.get(label));
        } else {
            //TODO Throw an exception
            return null;
        }
    }

    private void codeHandler(Instruction instruction) {
        dataMemory.pushLf(
            new StackNode(
                instruction.getFirstArgument().toString(),
                instruction.getFirstArgument().toString(),
                StackConstants.DataTypes.STRING
            )
        );
    }

    private void sosHandler(Instruction instruction) throws InvalidUserInputException, InvalidOperationException {
        dataMemory.OperatingSystem((Instruction) instruction.getFirstArgument());
    }

    private void limitHandler(Instruction instruction) {

    }

    private void chrHandler(Instruction instruction) {
        StackNode top = dataMemory.popLf();
        dataMemory.pushLf(
            new StackNode(
                instruction.getRawName(),
                (char) ((int)Integer.valueOf(top.getValue().toString())),
                StackConstants.DataTypes.CHAR
            )
        );
    }

    private void ordHandler(Instruction instruction) {
        StackNode top = dataMemory.popLf();
        dataMemory.pushLf(
            new StackNode(
                instruction.getRawName(),
                (int) top.getValue().toString().charAt(0),
                StackConstants.DataTypes.INT
            )
        );
    }
}
