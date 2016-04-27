package parser;

import lowlevel.CodeItem;

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

    public CodeItem genLLCode() {
        CodeItem code = declarations.get(0).genLLCode();
        CodeItem ptr = code;
        for(int i = 1; i < declarations.size(); i++) {
            ptr.setNextItem(declarations.get(i).genLLCode());
            ptr = ptr.getNextItem();
        }
        return code;
    }
}
