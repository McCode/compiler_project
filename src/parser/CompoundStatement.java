package parser;

import java.util.List;

public class CompoundStatement implements Statement {
    final List<VarDeclaration> declarations;
    final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }
}
