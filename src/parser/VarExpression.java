package parser;

public class VarExpression implements Expression {
    String id;
    Expression expr;

    public VarExpression(String id) {
        this.id = id;
        this.expr = null;
    }

    public VarExpression(String id, Expression expr) {
        this.id = id;
        this.expr = expr;
    }
}
