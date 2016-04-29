package parser;

import lowlevel.BasicBlock;
import lowlevel.Operand;
import lowlevel.Operation;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

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
        List<Integer> regNums = new ArrayList<>();
        for(int i = args.size() - 1; i >= 0; i--) {
            regNums.set(args.size() - 1 - i, args.get(i).genLLCode(block));
        }

        for(int reg: regNums) {
            Operation op = new Operation(Operation.OperationType.PASS, block);
            op.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, reg));
            // TODO: figure out destination
            block.appendOper(op);
        }

        Operation callOp = new Operation(Operation.OperationType.CALL, block);
        callOp.setSrcOperand(0, new Operand(Operand.OperandType.STRING, id));
        block.appendOper(callOp);

        int returnReg = block.getFunc().getNewRegNum();
        // TODO: add operation to move value from return register to a regular register

        return returnReg;
    }
}
