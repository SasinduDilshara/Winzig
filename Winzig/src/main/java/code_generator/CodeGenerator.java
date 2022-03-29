package code_generator;

import abstract_machine.AbstractMachine;
import abstract_machine.Instruction;
import parser.TreeStack;

import java.util.ArrayList;

public class CodeGenerator {
    private AbstractMachine machine;
    private ArrayList<Instruction> instructions;
    private TreeStack ast;

    public CodeGenerator(TreeStack ast) {
        this.ast = ast;
        instructions = new ArrayList<>();
        machine = new AbstractMachine();
    }


}
