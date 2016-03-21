package parser;

public class VarDeclaration implements Declaration {
    final String id;
    final boolean isArray;
    final int arrayLength;

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
}
