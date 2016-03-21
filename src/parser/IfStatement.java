package parser;

public class IfStatement implements Statement {
    public final Expression expr;
    public final Statement thenStmt;
    public final Statement elseStmt;

    public IfStatement(Expression expr, Statement stmt) {
        this(expr, stmt, null);
    }

    public IfStatement(Expression expr, Statement thenStmt, Statement elseStmt) {
        this.expr = expr;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "IfStatement");
        expr.printTree(indentLevel + 1);
        thenStmt.printTree(indentLevel + 1);
        if(elseStmt != null) {
            elseStmt.printTree(indentLevel + 1);
        }
    }
}
