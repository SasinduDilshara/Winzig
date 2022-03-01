import exceptions.WinzigIOException;
import exceptions.WinzigParserException;
import exceptions.WinzigScannarException;
import helper.FileHelper;
import parser.Parser;
import scannar.LexicalAnalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class winzigc {
    public static void main(String[] args) throws WinzigIOException,
            WinzigScannarException, WinzigParserException, IOException {
        String currentDirectory = System.getProperty("user.dir");
        String grammarFilePath = "/src/main/java/com/cd/winzigcompiler/grammar/WinZigC_Grammar.txt";
        String nonTerminalsFile = "/src/main/java/com/cd/winzigcompiler/grammar/non_terminal_list.txt";

//        System.out.println(currentDirectory + grammarFilePath);
//        System.out.println(currentDirectory + nonTerminalsFile);

        // GRAMMAR RELATED WORK

        // GrammarReader.readGrammarRules(currentDirectory + grammarFilePath);
        // GrammarReader.getNullNonTerminals();

//        GrammarReader.printGrammarRules();
//        GrammarReader.printNonTerminals();
//        GrammarReader.printNullableNonterminals();

//        GrammarReader.writeNonTerminals(currentDirectory + nonTerminalsFile);

        // SCANNER RELATED WORK
//        ArrayList<Token> tokens = null;
//                String input = null;
//                String fileinput = "D:\\Acedemic\\UOM\\Semesters\\Semester 8\\Compiler Design\\Project\\Part 01 - Parser\\winzig_test_programs\\winzig_01";
//                String fileinput = "D:\\Acedemic\\UOM\\Semesters\\Semester 8\\Compiler Design\\Project\\Part 01 - Parser\\MyTest\\test";
//                try {
//                    input = new String(Files.readAllBytes(Paths.get(fileinput)), StandardCharsets.UTF_8);
//                } catch (IOException e) {
//                    System.out.println("\nERROR OCCURED WHILE READING THE INPUT\n");
//                }

////        try {
//            tokens = Scanner.scan(input);
//                tokens = Scanner.scan("a = 8;\n b = 7;");
////        } catch (WinzigScannarException ex) {
////            System.out.println("\nERROR :- " + ex.getMessage() + "\n");
////        }
//        Scanner.printScannarTokenArray(tokens);

//        tokens = Scanner.scanAndGenerateTokenList("a = 8;{a\n}\nprogram = 7;");
//        tokens = Scanner.scanAndGenerateTokenList(input);
//        Scanner.printScannarTokenArray(tokens);

//        LexicalAnalayer lexicalAnalayer = new LexicalAnalayer();
//        lexicalAnalayer.ScanAndScreen(input);
//        LexicalAnalayer lexicalAnalayer1 = new LexicalAnalayer();
//        lexicalAnalayer1.ScanAndScreen(input);
//        Token next = null;
//        while (true) {
//            next = lexicalAnalayer1.getNextToken();
//            if (next == null) {
//                break;
//            } else {
////                System.out.println(next);
////                System.out.println("\n");
//            }
//        }

        String input;
        String outputFile = "D:\\Acedemic\\UOM\\Semesters\\Semester 8\\Compiler Design\\Project\\Part 01 - Parser\\MyTest\\mywrite.txt";
        String  inputdir = "D:\\Acedemic\\UOM\\Semesters\\Semester 8\\Compiler Design\\Project\\Part 01 - Parser\\winzig_test_programs";
        File dir = new File(inputdir);
        LexicalAnalyzer lexicalAnalyzer;
        String inputString, result;

        for (File file: dir.listFiles()) {
            if (file.getPath().contains(".tree")) {
                continue;
            }
//            try {
//                System.out.println(file.getPath());
            inputString = FileHelper.readFile(file.getPath());

            lexicalAnalyzer = new LexicalAnalyzer();
            lexicalAnalyzer.ScanAndScreen(inputString);

            Parser parser = new Parser(lexicalAnalyzer);
            parser.winzigProcedure();
            result = parser.getTreeStack().pop().traverseTree();

            FileHelper.writeFile(outputFile, result);

            Boolean r = FileHelper.compareResultFiles(outputFile, file.getPath() + ".tree");
            if (r) {
                System.out.println(file.getName() + " is passed");
            }
            System.out.println("\n");
//            } catch (Exception ex) {
//                System.out.println("error:- " + file.getPath());
//            }

        }


        String fileinput = "D:\\Acedemic\\UOM\\Semesters\\Semester 8\\Compiler Design\\Project\\Part 01 - Parser\\MyTest\\test";
        String input1 = "";
        try {
            input1 = new String(Files.readAllBytes(Paths.get(fileinput)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("\nERROR OCCURED WHILE READING THE INPUT\n");
        }

        LexicalAnalyzer lexicalAnalyzer1 = new LexicalAnalyzer();
        lexicalAnalyzer1.ScanAndScreen(input1);

        Parser parser1 = new Parser(lexicalAnalyzer1);
        parser1.winzigProcedure();

        String result1 = parser1.getTreeStack().pop().traverseTree();
//        System.out.println(result1);

    }
}
