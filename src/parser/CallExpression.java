package parser;

import java.util.List;

public class CallExpression implements Expression {
    final String id;
    final List<Expression> args;

    public CallExpression(String id, List<Expression> args) {
        this.id = id;
        this.args = args;
    }
}
