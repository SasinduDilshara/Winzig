package abstract_machine;

import exceptions.InvalidOperationException;
import exceptions.InvalidUserInputException;

import java.util.ArrayList;

public class AbstractMachine {
    public DataMemory dataMemory;
    public ReturnMemory returnMemory;
    public int pc;
    public ArrayList<Instruction> instructions;

    public AbstractMachine() {
        this.dataMemory = new DataMemory();
        this.returnMemory = new ReturnMemory();
        this.pc = 1;
        this.instructions = new ArrayList<>();
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
        return getInstructions().get(this.pc);
    }

    private void next() throws InvalidOperationException, InvalidUserInputException {
        Instruction instruction = getNextInstruction();
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
        }
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
                instruction.getFirstArgument().toString(), (boolean) x.getValue()
            );
            type = StackConstants.DataTypes.BOOLEAN;
        } else {
            value = dataMemory.Unop(
                    instruction.getFirstArgument().toString(), (int) x.getValue()
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
        String bop = instruction.getFirstArgument().toString();
        String type;
        if (bop.equals(StackConstants.BinaryOperators.BOR) ||
                bop.equals(StackConstants.BinaryOperators.BAND)) {
            value = dataMemory.Binop(
                    bop,
                    (boolean) x.getValue(),
                    (boolean) y.getValue()
            );
        } else {
            value = dataMemory.Binop(
                    bop,
                    (int) x.getValue(),
                    (int) y.getValue()
            );
        }
        if (bop.equals(StackConstants.BinaryOperators.BPLUS) ||
                bop.equals(StackConstants.BinaryOperators.BMINUS) ||
                bop.equals(StackConstants.BinaryOperators.BMULT) ||
                bop.equals(StackConstants.BinaryOperators.BDIV) ||
                bop.equals(StackConstants.BinaryOperators.BMOD)) {
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
        instruction = (Instruction) instruction.getFirstArgument();
        return instruction;
    }

    private Instruction condHandler(Instruction instruction) {
        StackNode first = dataMemory.popLf();
        if (first.getType().equals(StackConstants.DataTypes.BOOLEAN)
                && ((int) first.getValue()) == 1){
            return (Instruction) instruction.getFirstArgument();
        } else if (first.getType().equals(StackConstants.DataTypes.BOOLEAN)
                && ((int) first.getValue()) == 0){
            return (Instruction) instruction.getSecondArgument();
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
        dataMemory.OperatingSystem(instruction.getFirstArgument().toString());
    }

    private void limitHandler(Instruction instruction) {

    }
}
