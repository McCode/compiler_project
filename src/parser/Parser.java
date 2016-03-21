package parser;

import scanner.LexicalErrorException;

import java.io.IOException;

public interface Parser {
    Program parse() throws ParserException, IOException, LexicalErrorException;
    void printTree();
}
