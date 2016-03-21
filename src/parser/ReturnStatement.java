package parser;

public class ReturnStatement implements Statement {
    public final Expression expr;

    public ReturnStatement() {
        this.expr = null;
    }

    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }
}
