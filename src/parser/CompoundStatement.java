package parser;

import java.util.List;

public class CompoundStatement implements Statement {
    List<VarDeclaration> declarations;
    List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }
}
