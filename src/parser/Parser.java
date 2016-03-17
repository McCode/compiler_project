package parser;

public interface Parser {
    Program parse(String file);
    void printTree();
}
