package parser;

public class VarExpression implements Expression {
    String id;
    Expression expr;

    public VarExpression(String id) {
        this.id = id;
        this.expr = null;
    }

    public VarExpression(Expression expr, String id) {
        this.expr = expr;
        this.id = id;
    }
}
