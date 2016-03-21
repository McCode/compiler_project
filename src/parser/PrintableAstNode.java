package parser;

public interface PrintableAstNode {
    String indentation = "    ";
    void printTree(int indentLevel);
    default void printlnWithIndentation(int indentLevel, String str) {
        for(int i = 0; i < indentLevel; i++) { System.out.print(indentation); }
        System.out.println(str);
    }
}
