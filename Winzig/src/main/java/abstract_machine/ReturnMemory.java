package abstract_machine;

import java.util.ArrayList;

public class ReturnMemory {
    private int RBR;
    private int RTR;
    private int RLR;
    ArrayList<Instruction> stack;
    private int stackSize;

    public ReturnMemory() {
        this.RBR = 0;
        this.RTR = 0;
        this.RLR = 0;
        this.stack = new ArrayList<>();
        this.stackSize = 0;
    }

    public int getRBR() {
        return RBR;
    }

    public void setRBR(int RBR) {
        this.RBR = RBR;
    }

    public int getRTR() {
        return RTR;
    }

    public void setRTR(int RTR) {
        this.RTR = RTR;
    }

    public int getRLR() {
        return RLR;
    }

    public void setRLR(int RLR) {
        this.RLR = RLR;
    }

    public ArrayList<Instruction> getReturnMemoryStack() {
        return stack;
    }

    public void setStack(ArrayList<Instruction> stack) {
        this.stack = stack;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public void incrementRTR(int n) {
        setRTR(this.RTR + n);
    }

    public void incrementRBR(int n) {
        setRBR(this.RBR + n);
    }

    public void incrementRLR(int n) {
        setRLR(this.RLR + n);
    }

    public void incrementRTR() {
        incrementRTR(1);
    }

    public void incrementRBR() {
        incrementRBR(1);
    }

    public void incrementRLR() {
        incrementRLR(1);
    }

    public void decrementRTR(int n) {
        setRTR(this.RTR - n);
    }

    public void decrementRBR(int n) {
        setRBR(this.RBR - n);
    }

    public void decrementRLR(int n) {
        setRLR(this.RLR - n);
    }

    public void decrementRTR() {
        decrementRTR(1);
    }

    public void decrementRBR() {
        decrementRBR(1);
    }

    public void decrementRLR() {
        decrementRLR(1);
    }

    public void pushStack(int index, Instruction node) {
        this.stack.add(index, node);
        stackSize++;
    }

    public void removeStack(int index) {
        this.stack.remove(index);
        stackSize--;
    }

    public void pushStack(Instruction node) {
        this.stack.add(node);
        stackSize++;
    }

    public void removeStack() {
        removeStack(stackSize - 1);
    }

    public Instruction getStackNode(int index) {
        return this.stack.get(index);
    }

    public Instruction getStackNode() {
        return getStackNode(this.stackSize - 1);
    }

    public void pushReturnStack(Instruction i) {
        incrementRTR();
        pushStack(i);
    }

    public Instruction popReturnStack() {
        Instruction i = getStackNode(getRTR());
        decrementRTR();
        return i;
    }
}
