package parser;

import java.util.List;

public class CompoundStatement implements Statement {
    public final List<VarDeclaration> declarations;
    public final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "CompoundStatement");

        printlnWithIndentation(indentLevel + 1, "VarDeclarations");
        for(VarDeclaration varDecl: declarations) {
            varDecl.printTree(indentLevel + 2);
        }

        printlnWithIndentation(indentLevel + 1, "Statements");
        for(Statement s: statements) {
            s.printTree(indentLevel + 2);
        }
    }
}
