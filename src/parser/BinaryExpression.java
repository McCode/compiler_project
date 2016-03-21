package parser;

public class BinaryExpression implements Expression {
    public final Expression lhs;
    public final Expression rhs;
    public final BinaryOperation operation;

    public BinaryExpression(Expression lhs, Expression rhs, BinaryOperation operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "BinaryExpression: " + operation.toString());
        lhs.printTree(indentLevel + 1);
        rhs.printTree(indentLevel + 1);
    }
}
