package parser;

public class BinaryExpression implements Expression {
    final Expression lhs;
    final Expression rhs;
    final BinaryOperation operation;

    public BinaryExpression(Expression lhs, Expression rhs, BinaryOperation operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }
}
