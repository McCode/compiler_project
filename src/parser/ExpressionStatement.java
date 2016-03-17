package parser;

public class ExpressionStatement implements Statement {
    Expression expr;

    public ExpressionStatement(Expression expr) {
        this.expr = expr;
    }
}
