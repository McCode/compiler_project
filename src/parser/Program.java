package parser;

import java.util.*;

public class Program implements PrintableAstNode {
    public final List<Declaration> declarations;

    public Program(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "Program");

        for(Declaration d: declarations) {
            d.printTree(indentLevel + 1);
        }
    }
}
