package parser;

public class VarDeclaration implements Declaration {
    public final String id;
    public final boolean isArray;
    public final int arrayLength;

    public VarDeclaration(String id) {
        this.id = id;
        isArray = false;
        arrayLength = 0;
    }

    public VarDeclaration(String id, int arrayLength) {
        this.id = id;
        isArray = true;
        this.arrayLength = arrayLength;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "VarDeclaration");
        if(isArray) {
            printlnWithIndentation(indentLevel + 1, id + "[" + Integer.toString(arrayLength) + "]");
        } else {
            printlnWithIndentation(indentLevel + 1, id);
        }
    }
}
