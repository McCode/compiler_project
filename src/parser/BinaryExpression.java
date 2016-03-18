package parser;

public class BinaryExpression implements Expression {
    Expression lhs;
    Expression rhs;
    BinaryOperation operation;

    public BinaryExpression(Expression lhs, Expression rhs, BinaryOperation operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }
}
