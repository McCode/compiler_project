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
}
