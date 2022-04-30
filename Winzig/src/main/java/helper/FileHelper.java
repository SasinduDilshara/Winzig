package helper;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper
{
    public static Boolean compareResultFiles(String file1, String file2) throws IOException
    {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();
        boolean areEqual = true;
        int lineNum = 1;
        while (line1 != null || line2 != null) {
            if(line1 == null || line2 == null) {
                areEqual = false;
                break;
            } else if(! line1.equalsIgnoreCase(line2)) {
                areEqual = false;
                break;
            }
            line1 = reader1.readLine();
            line2 = reader2.readLine();
            lineNum++;
        }
        reader1.close();
        reader2.close();
        if(areEqual) {
            return areEqual;
        } else {
            System.out.println("file1 has "+line1+" and file2 has "+line2+" at line "+lineNum);
            return areEqual;
        }
    }

    public static void writeFile(String file, String content) {
        FileWriter myWriter;
        String currentcontent = "";
        try {
            currentcontent = readFile(file);
            if (currentcontent != null) {
                content = currentcontent + content;
            }
            myWriter = new FileWriter(file);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String readFile(String file) {
        String input = null;
        try {
            input = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error while reading the file :- " + file + "\n" + e.getLocalizedMessage() + "\n");
        }
        return input;
    }
}
