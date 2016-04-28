package parser;

import lowlevel.BasicBlock;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class CallExpression implements Expression {
    public final String id;
    public final List<Expression> args;

    public CallExpression(String id, List<Expression> args) {
        this.id = id;
        this.args = args;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "CallExpression");
        printlnWithIndentation(indentLevel + 1, id);
        printlnWithIndentation(indentLevel + 1, "Args:");
        if(args.isEmpty()) {
            printlnWithIndentation(indentLevel + 2, "None");
        } else {
            for(Expression e: args) {
                e.printTree(indentLevel + 2);
            }
        }
    }

    @Override
    public int genLLCode(BasicBlock block) {


        throw new NotImplementedException();
        return 0;
    }
}
