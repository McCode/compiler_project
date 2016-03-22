package parser;

import scanner.LexicalErrorException;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader inputFile = null;

        try {
            inputFile = new BufferedReader(new FileReader(new File(args[0])));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: File could not be opened!");
            System.exit(1);
        }

        try {
            Parser parser = new CMinusParser(inputFile);
            parser.parse();
            parser.printTree();
        } catch(IOException|LexicalErrorException e) {
            e.printStackTrace();
        } catch(ParserException e) {
            System.out.println(e.getMessage());
        }
    }
}
