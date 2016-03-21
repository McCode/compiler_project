package parser;

import scanner.LexicalErrorException;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        BufferedReader inputFile = null;

        System.out.println("Attempting to open " + args[0] + "...");
        try {
            inputFile = new BufferedReader(new FileReader(new File(args[0])));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: File could not be opened!");
            System.exit(1);
        }
        System.out.println("Opening the file was successful!");

        System.out.println("Parsing...");
        try {
            Parser parser = new CMinusParser(inputFile);
            parser.parse();
            parser.printTree();
        } catch(IOException|LexicalErrorException|ParserException e) {
            e.printStackTrace();
        }
    }
}
