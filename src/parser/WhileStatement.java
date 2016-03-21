package parser;

public class WhileStatement implements Statement {
    public final Expression expr;
    public final Statement stmt;

    public WhileStatement(Expression expr, Statement stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "WhileStatement");
        expr.printTree(indentLevel + 1);
        stmt.printTree(indentLevel + 1);
    }
}
