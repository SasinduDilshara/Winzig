package abstract_machine;

import constants.StackConstants;
import exceptions.InvalidOperationException;
import exceptions.InvalidUserInputException;

import java.util.ArrayList;

import static abstract_machine.StackHelper.convertBooleanToInt;

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
        this.LBR = 0;
        this.STR = -1;
        this.SLR = 0;
        this.stack = new ArrayList<>();
        this.stackSize = 0;
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
        setSLR(this.SLR + n);
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

    public void removeStack(int index) {
        this.stack.remove(index);
        stackSize--;
    }

    public void pushStack(StackNode node) {
        this.stack.add(node);
        stackSize++;
    }

    public void removeStack() {
        removeStack(stackSize - 1);
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

    public int Unop(String i, int x) throws InvalidOperationException {
        switch (i) {
            case StackConstants.UnaryOperators.UNEG:
                return -1 * x;
            case StackConstants.UnaryOperators.USUCC:
                return x + 1;
            case StackConstants.UnaryOperators.UPRED:
                return x - 1;
            default:
                throw new InvalidOperationException(i);
        }
    }

    public int Unop(String i, boolean x) throws InvalidOperationException {
        switch (i) {
            case StackConstants.UnaryOperators.UNOT:
                return convertBooleanToInt(!x);
            default:
                throw new InvalidOperationException(i);
        }
    }

    public int Binop(String i, int x, int y) throws InvalidOperationException {
        switch (i) {
            case StackConstants.BinaryOperators.BPLUS:
                return x + y;
            case StackConstants.BinaryOperators.BMINUS:
                return x - y;
            case StackConstants.BinaryOperators.BMULT:
                return x * y;
            case StackConstants.BinaryOperators.BDIV:
                try {
                    return x / y;
                } catch (ArithmeticException exception) {
                    throw new InvalidOperationException("Divide by zero");
                }
            case StackConstants.BinaryOperators.BMOD:
                return x % y;
            case StackConstants.BinaryOperators.BEQ:
                return convertBooleanToInt(x == y);
            case StackConstants.BinaryOperators.BNE:
                return convertBooleanToInt(x != y);
            case StackConstants.BinaryOperators.BLE:
                return convertBooleanToInt(x <= y);
            case StackConstants.BinaryOperators.BGE:
                return convertBooleanToInt(x >= y);
            case StackConstants.BinaryOperators.BLT:
                return convertBooleanToInt(x < y);
            case StackConstants.BinaryOperators.BGT:
                return convertBooleanToInt(x > y);
            default:
                throw new InvalidOperationException(i);
        }
    }
    public int Binop(String i, boolean x, boolean y) throws InvalidOperationException {
        switch (i) {
            case StackConstants.BinaryOperators.BAND:
                return convertBooleanToInt(x && y);
            case StackConstants.BinaryOperators.BOR:
                return convertBooleanToInt(x || y);
            default:
                throw new InvalidOperationException(i);
        }
    }

    public void OperatingSystem(String i) throws InvalidOperationException, InvalidUserInputException {
        switch (i) {
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
                throw new InvalidOperationException(i);
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
        pushStack(new StackNode(
                StackHelper.generateIntegerNodeName(userInput),
                userInput,
                StackConstants.DataTypes.INT
        ));
    }

    private void inputcOperaion() throws InvalidUserInputException {
        char userInput = StackHelper.readUserCharacterInput();
        pushStack(new StackNode(
                StackHelper.generateCharNodeName(userInput),
                (int) userInput,
                StackConstants.DataTypes.CHAR
        ));
    }

    private void outputOperaion() {
        System.out.print(popLf().getValue());
    }

    private void outputcOperaion() {
        System.out.print((char) (popLf().getValue()));
    }

    private void outputLOperaion() {
        System.out.println();
    }

    private void eofOperaion() {
        //TODO: Add the Logic
    }

    public void pushLf(StackNode v) {
        incrementSTR();
        pushStack(this.STR, v);
    }

    public StackNode topLf() {
        return getStack(this.STR);
    }

    public StackNode popLf() {
        StackNode top = getStack();
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

}
