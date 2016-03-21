package parser;

import java.util.List;

public class FunctionDeclaration implements Declaration {
    public final TypeSpecifier type;
    public final String id;
    public final List<Param> params;
    public final CompoundStatement stmt;

    public FunctionDeclaration(TypeSpecifier type, String id, List<Param> params, CompoundStatement stmt) {
        this.type = type;
        this.id = id;
        this.params = params;
        this.stmt = stmt;
    }
}
