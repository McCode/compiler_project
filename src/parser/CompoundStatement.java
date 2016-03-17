package parser;

import java.util.List;

public class CompoundStatement implements Statement {
    List<Declaration> declarations;
    List<Statement> statements;

    public CompoundStatement(List<Declaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }
}
