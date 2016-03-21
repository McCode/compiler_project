package parser;

public class Param implements PrintableAstNode {
    public final String id;
    public final boolean isArray;

    public Param(String id, boolean isArray) {
        this.id = id;
        this.isArray = isArray;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "Param");
        if(isArray) {
            printlnWithIndentation(indentLevel + 1, id + "[]");
        } else {
            printlnWithIndentation(indentLevel + 1, id);
        }
    }
}
