package parser;

public class VarExpression implements Expression {
    public final String id;
    public final Expression expr;

    public VarExpression(String id) {
        this.id = id;
        this.expr = null;
    }

    public VarExpression(String id, Expression expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "VarExpression");
        if(this.expr == null) {
            printlnWithIndentation(indentLevel + 1, id);
        } else {
            printlnWithIndentation(indentLevel + 1, id + "[]");
            expr.printTree(indentLevel + 1);
        }
    }
}
