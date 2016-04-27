package parser;

import lowlevel.CodeItem;

/**
 * This is here to enforce that all the node classes can printTree.
 */
public interface PrintableAstNode {
    String indentation = "    ";

    void printTree(int indentLevel);

    // This prints a given string with a given level of indentation.
    default void printlnWithIndentation(int indentLevel, String str) {
        for(int i = 0; i < indentLevel; i++) { System.out.print(indentation); }
        System.out.println(str);
    }
}
