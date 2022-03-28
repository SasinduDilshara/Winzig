package abstract_machine;

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
    }

    public static class DataMemoryNodeNames {
        public static final String INTEGER = "Integer";
        public static final String CHAR = "Character";
    }
}
