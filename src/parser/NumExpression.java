package parser;

public class NumExpression implements Expression {
    public final int num;

    public NumExpression(int num) {
        this.num = num;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "NumExpression");
        printlnWithIndentation(indentLevel + 1, String.valueOf(num));
    }
}
