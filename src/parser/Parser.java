package parser;

import java.io.Reader;

public interface Parser {
    Program parse() throws ParserException;
    void printTree();
}
