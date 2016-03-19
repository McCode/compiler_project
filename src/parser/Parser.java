package parser;

import scanner.LexicalErrorException;

import java.io.IOException;
import java.io.Reader;

public interface Parser {
    Program parse() throws ParserException, IOException, LexicalErrorException;
    void printTree();
}
