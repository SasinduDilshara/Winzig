package constants;

public class StackConstants {
    // Unary Operations' names
    public static class UnaryOperators {
        public static final String UNOT = "UNOT";
        public static final String UNEG = "UNEG";
        public static final String USUCC = "USUCC";
        public static final String UPRED = "UPRED";
    }

    //Binary Operations' Names
    public static class BinaryOperators {
        public static final String BAND = "BAND";
        public static final String BOR = "BOR";
        public static final String BPLUS = "BPLUS";
        public static final String BMINUS = "BMINUS";
        public static final String BMULT = "BMULT";
        public static final String BDIV = "BDIV";
        public static final String BMOD = "BMOD";
        public static final String BEQ = "BEQ";
        public static final String BNE = "BNE";
        public static final String BLE = "BLE";
        public static final String BGE = "BGE";
        public static final String BLT = "BLT";
        public static final String BGT = "BGT";
    }

    //Operating System Operations' names
    public static class OperatingSystemOperators {
        public static final String TRACEX = "TRACEX";
        public static final String DUMPMEM = "DUMPMEM";
        public static final String INPUT = "INPUT";
        public static final String INPUTC = "INPUTC";
        public static final String OUTPUT = "OUTPUT";
        public static final String OUTPUTC = "OUTPUTC";
        public static final String OUTPUTL = "OUTPUTL";
        public static final String EOF = "EOF";
    }

    public static class DataTypes {
        public static final String STRING = "String";
        public static final String INT = "Integer";
        public static final String CHAR = "Character";
        public static final String BOOLEAN = "Boolean";
    }

    public static class DataMemoryNodeNames {
        public static final String ProgramNode = "program";
        public static final String ConstsNode = "consts";
        public static final String ConstNode = "const";
        public static final String TypesNode = "types";
        public static final String TypeNode = "type";
        public static final String LitNode = "lit";
        public static final String SubProgsNode = "subprogs";
        public static final String FunctionNode = "fcn";
        public static final String ParamsNode = "params";
        public static final String DclnsNode = "dclns";
        public static final String VarNode = "var";
        public static final String BlockNode = "block";
        public static final String OutputNode = "output";
        public static final String IfNode = "if";
        public static final String WhileNode = "while";
        public static final String RepeatNode = "repeat";
        public static final String For = "for";
        public static final String LoopNode = "loop";
        public static final String CaseNode = "case";
        public static final String ReadNode = "read";
        public static final String ExitNode = "exit";
        public static final String ReturnNode = "return";
        public static final String NullNode = "<null>";
        public static final String IntegerNode = "integer";
        public static final String StringNode = "string";
        public static final String CaseClauseNode = "case_clause";
        public static final String TwoDotNodeNode = "..";
        public static final String OtherwiseNode = "otherwise";
        public static final String AssignNode = "assign";
        public static final String SwapNode = "swap";
        public static final String TrueNode = "true";
        public static final String LENode = "<=";
        public static final String LTNode = "<";
        public static final String GENode = ">=";
        public static final String GTNode = ">";
        public static final String EQNode = "=";
        public static final String NEQNode = "<>";
        public static final String PlusNode = "+";
        public static final String MinusNode = "-";
        public static final String ORNode = "or";
        public static final String MultNode = "*";
        public static final String DivNode = "/";
        public static final String ANDNode = "and";
        public static final String ModNode = "mod";
        public static final String NotNode = "not";
        public static final String EOFNode = "eof";
        public static final String CallNode = "call";
        public static final String SuccNode = "succ";
        public static final String PredNode = "pred";
        public static final String ChrNode = "chr";
        public static final String OrdNode = "ord";

        public static final String CharNode = "char";
        public static final String BooleanNode = "boolean";
        public static final String IdentifierNode = "identifier";
    }

    public static class AbsMachineOperations {
        public static final String NOP = "N";
        public static final String HALTOP = "HALT";
        public static final String LITOP = "LIT";
        public static final String LLVOP = "LLV";
        public static final String LGVOP = "LGV";
        public static final String SLVOP = "SLV";
        public static final String SGVOP = "SGV";
        public static final String LLAOP = "LLA";
        public static final String LGAOP = "LGA";
        public static final String UOPOP = "UOP";
        public static final String BOPOP = "BOP";
        public static final String POPOP = "POP";
        public static final String DUPOP = "DUP";
        public static final String SWAPOP = "SWAP";
        public static final String CALLOP = "CALL";
        public static final String RTNOP = "RTN";
        public static final String GOTOOP = "GOTO";
        public static final String CONDOP = "COND";
        public static final String CODEOP = "CODE";
        public static final String SOSOP = "SOS";
        public static final String LIMITOP = "LIMIT";
    }
}
