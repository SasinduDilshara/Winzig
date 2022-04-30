import helper.FileHelper;
import parser.Parser;
import scannar.LexicalAnalyzer;

public class winzigc {
    public static void main(String[] args) throws Exception {

        LexicalAnalyzer lexicalAnalyzer;
        String inputString, result, inputFile = "", outputFile = "";
        try {
            inputFile = args[1];
        } catch (Exception ex) {
            throw new Exception("Please Specify the input file to run the program");
        }
        try {
            outputFile = args[2];
        } catch (Exception e) {

        }

        if (inputFile == null || inputFile.equals("")) {
            throw new Exception("Please Specify the input file to run the program");
        }

        inputString = FileHelper.readFile(inputFile);

        lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.ScanAndScreen(inputString);

        Parser parser = new Parser(lexicalAnalyzer);
        parser.winzigProcedure();
        result = parser.getTreeStack().pop().traverseTree();
        System.out.println(result);
        if (outputFile != null || outputFile.equals("")) {
            FileHelper.writeFile(outputFile, result);
        }
    }
}
