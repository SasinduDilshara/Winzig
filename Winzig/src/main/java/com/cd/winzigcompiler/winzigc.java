package com.cd.winzigcompiler;

import com.cd.winzigcompiler.helper.FileHelper;
import com.cd.winzigcompiler.parser.Parser;
import com.cd.winzigcompiler.scanner.LexicalAnalayer;

public class winzigc {
    public static void main(String[] args) throws Exception {

        LexicalAnalayer lexicalAnalayer;
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

        lexicalAnalayer = new LexicalAnalayer();
        lexicalAnalayer.ScanAndScreen(inputString);

        Parser parser = new Parser(lexicalAnalayer);
        parser.winzigProcedure();
        result = parser.getTreeStack().pop().traverseTree();
        System.out.println(result);
        if (outputFile != null || outputFile.equals("")) {
            FileHelper.writeFile(outputFile, result);
        }
    }
}
