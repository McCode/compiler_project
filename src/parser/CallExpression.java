package parser;

import java.util.List;

public class CallExpression implements Expression {
    public final String id;
    public final List<Expression> args;

    public CallExpression(String id, List<Expression> args) {
        this.id = id;
        this.args = args;
    }
}
