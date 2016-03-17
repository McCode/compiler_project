package parser;

public class VarDeclaration implements Declaration {
    String id;
    boolean isArray;
    int arrayLength;

    public VarDeclaration(String id) {
        this.id = id;
        isArray = false;
    }

    public VarDeclaration(String id, int arrayLength) {
        this.id = id;
        isArray = true;
        this.arrayLength = arrayLength;
    }
}
