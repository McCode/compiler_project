package parser;

public class WhileStatement implements Statement {
    Expression expr;
    Statement stmt;

    public WhileStatement(Expression expr, Statement stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }
}
