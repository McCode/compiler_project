package parser;

public class ExpressionStatement implements Statement {
    public final Expression expr;

    public ExpressionStatement(Expression expr) {
        this.expr = expr;
    }
}
