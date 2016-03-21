package parser;

public class ExpressionStatement implements Statement {
    public final Expression expr;

    public ExpressionStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "ExpressionStatement");
        if(expr == null) {
            printlnWithIndentation(indentLevel + 1, "Empty");
        } else {
            expr.printTree(indentLevel + 1);
        }
    }
}
