package parser;

public class ReturnStatement implements Statement {
    public final Expression expr;

    public ReturnStatement() {
        this.expr = null;
    }

    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "ReturnStatement");

        if(expr == null) {
            printlnWithIndentation(indentLevel + 1, "Empty");
        } else {
            expr.printTree(indentLevel + 1);
        }
    }
}
