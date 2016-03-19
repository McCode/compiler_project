package parser;

public class ReturnStatement implements Statement {
    Statement stmt;

    public ReturnStatement() {
        this.stmt = null;
    }

    public ReturnStatement(Statement stmt) {
        this.stmt = stmt;
    }
}
