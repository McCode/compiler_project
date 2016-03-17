package parser;

public class ReturnStatement implements Statement {
    Statement stmt;

    public ReturnStatement(Statement stmt) {
        this.stmt = stmt;
    }
}
