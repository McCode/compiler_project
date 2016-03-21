package parser;

import java.util.List;

public class CompoundStatement implements Statement {
    public final List<VarDeclaration> declarations;
    public final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }
}
