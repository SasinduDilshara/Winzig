package abstract_machine;

import constants.StackConstants;
import exceptions.InvalidOperationException;
import exceptions.InvalidUserInputException;
import helper.FileHelper;

import java.util.ArrayList;

import static abstract_machine.StackHelper.convertBooleanToInt;
import static constants.StackConstants.Constants.FileWriteTest;
import static constants.StackConstants.Constants.TestFilePath;

public class DataMemory {
    private int GBR;
    private int GLR;
    private int LBR;
    private int STR;
    private int SLR;
    ArrayList<StackNode> stack;
    private int stackSize;

    public DataMemory() {
        this.GBR = 0;
        this.GLR = 0;
        this.LBR = -1;
        this.STR = -1;
        this.SLR = 0;
        this.stack = new ArrayList<>();
        this.stackSize = 0;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public void setGBR(int GBR) {
        this.GBR = GBR;
    }

    public void setGLR(int GLR) {
        this.GLR = GLR;
    }

    public void setLBR(int LBR) {
        this.LBR = LBR;
    }

    public void setSTR(int STR) {
        this.STR = STR;
    }

    public void setSLR(int SLR) {
        this.SLR = SLR;
    }

    public int getGBR() {
        return GBR;
    }

    public int getGLR() {
        return GLR;
    }

    public int getLBR() {
        return LBR;
    }

    public int getSTR() {
        return STR;
    }

    public int getSLR() {
        return SLR;
    }

    public void incrementGBR(int n) {
        setGBR(this.GBR + n);
    }

    public void incrementGLR(int n) {
        setGLR(this.GLR + n);
    }

    public void incrementLBR(int n) {
        setLBR(this.LBR + n);
    }

    public void incrementSTR(int n) {
        setSTR(this.STR + n);
    }

    public void incrementSLR(int n) {
        setSLR(getSLR() + n);
    }

    public void incrementGBR() {
        incrementGBR(1);
    }

    public void incrementGLR() {
        incrementGLR(1);
    }

    public void incrementLBR() {
        incrementLBR(1);
    }

    public void incrementSTR() {
        incrementSTR(1);
    }

    public void incrementSLR() {
        incrementSLR(1);
    }

    public void decrementGBR(int n) {
        setGBR(this.GBR - n);
    }

    public void decrementGLR(int n) {
        setGLR(this.GLR - n);
    }

    public void decrementLBR(int n) {
        setLBR(this.LBR - n);
    }

    public void decrementSTR(int n) {
        setSTR(this.STR - n);
    }

    public void decrementSLR(int n) {
        setSLR(this.SLR - n);
    }

    public void decrementGBR() {
        decrementGBR(1);
    }

    public void decrementGLR() {
        decrementGLR(1);
    }

    public void decrementLBR() {
        decrementLBR(1);
    }

    public void decrementSTR() {
        decrementSTR(1);
    }

    public void decrementSLR() {
        decrementSLR(1);
    }

    public void pushStack(int index, StackNode node) {
        this.stack.add(index, node);
        stackSize++;
    }

    public StackNode removeStack(int index) {
        stackSize--;
        return this.stack.remove(index);
    }

    public void pushStack(StackNode node) {
        this.stack.add(node);
        stackSize++;
    }

    public StackNode removeStack() {
        return removeStack(stackSize - 1);
    }

    public StackNode getStack(int index) {
        return this.stack.get(index);
    }

    public StackNode getStack() {
        return getStack(stackSize - 1);
    }

    public StackNode updateStack(int index, StackNode node) {
        return this.stack.set(index, node);
    }

    public int Unop(Instruction instruction, int x) throws Exception {
        switch (instruction.getName()) {
            case StackConstants.UnaryOperators.UNEG:
                return -1 * x;
            case StackConstants.UnaryOperators.USUCC:
                return x + 1;
            case StackConstants.UnaryOperators.UPRED:
                return x - 1;
            default:
                throw new InvalidOperationException(instruction.getName());
        }
    }

    public int Unop(Instruction instruction, boolean x) throws Exception {
        switch (instruction.getName()) {
            case StackConstants.UnaryOperators.UNOT:
                return convertBooleanToInt(!x);
            default:
                throw new InvalidOperationException(instruction.getName());
        }
    }

    public int Binop(Instruction instruction, String x1, String y1) throws Exception {
        int x = 0;
        int y = 0;
        if (!(instruction.getName().equals(StackConstants.BinaryOperators.BEQ) ||
                instruction.getName().equals(StackConstants.BinaryOperators.BNE)
        )) {
            x = Integer.valueOf(x1);
            y = Integer.valueOf(y1);
        }
        switch (instruction.getName()) {
            case StackConstants.BinaryOperators.BPLUS:
                return x + y;
            case StackConstants.BinaryOperators.BMINUS:
                return y - x;
            case StackConstants.BinaryOperators.BMULT:
                return x * y;
            case StackConstants.BinaryOperators.BDIV:
                try {
                    return y / x;
                } catch (ArithmeticException exception) {
                    throw new InvalidOperationException("Divide by zero");
                }
            case StackConstants.BinaryOperators.BMOD:
                return y % x;
            case StackConstants.BinaryOperators.BEQ:
                return convertBooleanToInt(x1.equals(y1));
            case StackConstants.BinaryOperators.BNE:
                return convertBooleanToInt(!(x1.equals(y1)));
            case StackConstants.BinaryOperators.BLE:
                return convertBooleanToInt(y <= x);
            case StackConstants.BinaryOperators.BGE:
                return convertBooleanToInt(y >= x);
            case StackConstants.BinaryOperators.BLT:
                return convertBooleanToInt(y < x);
            case StackConstants.BinaryOperators.BGT:
                return convertBooleanToInt(y > x);
            default:
                throw new InvalidOperationException(instruction.getName());
        }
    }
    public int Binop(Instruction instruction, boolean x, boolean y) throws Exception {
        switch (instruction.getName()) {
            case StackConstants.BinaryOperators.BAND:
                return convertBooleanToInt(x && y);
            case StackConstants.BinaryOperators.BOR:
                return convertBooleanToInt(x || y);
            default:
                throw new InvalidOperationException(instruction.getName());
        }
    }

    public void OperatingSystem(Instruction i) throws Exception {
        switch (i.getName()) {
            case StackConstants.OperatingSystemOperators.TRACEX:
                traceOperation();
                break;
            case StackConstants.OperatingSystemOperators.DUMPMEM:
                memoryDumpOperaion();
                break;
            case StackConstants.OperatingSystemOperators.INPUT:
                inputOperaion();
                break;
            case StackConstants.OperatingSystemOperators.INPUTC:
                inputcOperaion();
                break;
            case StackConstants.OperatingSystemOperators.OUTPUT:
                outputOperaion();
                break;
            case StackConstants.OperatingSystemOperators.OUTPUTC:
                outputcOperaion();
                break;
            case StackConstants.OperatingSystemOperators.OUTPUTL:
                outputLOperaion();
                break;
            case StackConstants.OperatingSystemOperators.EOF:
                eofOperaion();
                break;
            default:
                throw new InvalidOperationException(i.getName());
        }
    }

    private void traceOperation() {
        //TODO: Implement Trace
        System.out.println("This is Trace function!!");
    }

    private void memoryDumpOperaion() {
        //TODO: Implement Dump
        System.out.println("This is DUMPMEM function!!");
    }

    private void inputOperaion() throws InvalidUserInputException {
        int userInput = StackHelper.readUserIntegerInput();
        pushLf(new StackNode(
                StackHelper.generateIntegerNodeName(userInput),
                userInput,
                StackConstants.DataTypes.INT
        ));
    }

    private void inputcOperaion() throws InvalidUserInputException {
        char userInput = StackHelper.readUserCharacterInput();
        pushLf(new StackNode(
                StackHelper.generateCharNodeName(userInput),
                (int) userInput,
                StackConstants.DataTypes.CHAR
        ));
    }

    private void outputOperaion() {
        //TODO:Update
        if (FileWriteTest) {
            String val = popLf().getValue().toString();
            FileHelper.writeFile(TestFilePath, val);
//            System.out.print(val);
        } else {
            System.out.print(popLf().getValue());
        }
    }

    private void outputcOperaion() {
        System.out.print((char) (popLf().getValue()));
    }

    private void outputLOperaion() {
        if (FileWriteTest) {
            FileHelper.writeFile(TestFilePath, "\n");
//            System.out.println();
        } else {
            System.out.println();
        }
    }

    private void eofOperaion() {
        pushLf(new StackNode(
            StackConstants.DataTypes.BOOLEAN,
            true,
            StackConstants.DataTypes.BOOLEAN
        ));
    }

    public void pushLf(StackNode v) {
        incrementSTR();
        pushStack(this.STR, v);
    }

    public StackNode topLf() {
        return getStack(this.STR);
    }

    public StackNode popLf() {
        StackNode top = removeStack();
        decrementSTR();
        return top;
    }

    public void popNOffLf(int n) {
        decrementSTR(n);
    }

    public int depthLf() {
        return getSTR() - getLBR() + 1;
    }

    public void openFrame(int i) {
        incrementLBR(i);
    }

    public void closeFrame(int i) {
        decrementLBR(i);
    }

    public StackNode lf(int i) {
        return getStack(getGBR() + getLocalAddress(i));
    }

    public StackNode gf(int i) {
        return getStack(getGBR() + getGlobalAddress(i));
    }

    public int lfGetIndex(int i) {
        return getGBR() + getLocalAddress(i);
    }

    public int gfGetIndex(int i) {
        return getGBR() + getGlobalAddress(i);
    }

    public int getLocalAddress(int i) {
        return getLBR() + i - getGBR();
    }

    public int getGlobalAddress(int i) {
        return i;
    }

    public void swapTwoStackNodes(int x, int y) {
        StackNode xNode = getStack(x);
        updateStack(x, getStack(y));
        updateStack(y, xNode);
    }

    @Override
    public String toString() {
        String s = "The begining of the Stack\n";
        if (stackSize != stack.size()) {
            return "Stack Size Error";
        }
        StackNode stackNode;
        for (int i = 0; i < stackSize; i++) {
            stackNode = stack.get(i);
            s += stackNode.toString();
            s+= "\n";
        }
        s += "The End of the Stack\n";
        return s;
    }
}
