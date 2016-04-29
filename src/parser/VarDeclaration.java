package parser;

import lowlevel.*;

import java.util.HashMap;

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
        printlnWithIndentation(indentLevel + 1, "Id: " + id);
        if(isArray) {
            printlnWithIndentation(indentLevel + 1, "Array length: " + arrayLength);
        }
    }

    @Override
    public CodeItem genLLCode(HashMap globalHash) {
        return new Data(Data.TYPE_INT, id, isArray, arrayLength);
    }
}
