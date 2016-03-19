package parser;

public class ReturnStatement implements Statement {
    Expression expr;

    public ReturnStatement() {
        this.expr = null;
    }

    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }
}
