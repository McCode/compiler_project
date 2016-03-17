package parser;

public class VarExpression implements Expression {
    String id;

    public VarExpression(String id) {
        this.id = id;
    }
}
