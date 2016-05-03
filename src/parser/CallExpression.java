package parser;

import lowlevel.*;
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
            regNums.add(args.get(i).genLLCode(block));
        }

        int paramNum = Math.min(args.size(), 6);
        for(int reg: regNums) {
            Operation op = new Operation(Operation.OperationType.PASS, block);
            op.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, reg));

            if(paramNum > 0) {
                paramNum--;
                op.addAttribute(new Attribute("PARAM_NUM", Integer.toString(paramNum)));
            }
            block.appendOper(op);
        }

        Operation callOp = new Operation(Operation.OperationType.CALL, block);
        callOp.setSrcOperand(0, new Operand(Operand.OperandType.STRING, id));
        callOp.addAttribute(new Attribute("numParams", Integer.toString(regNums.size())));
        block.appendOper(callOp);

        int regularRegister = block.getFunc().getNewRegNum();
        Operation moveReturnValueOp = new Operation(Operation.OperationType.ASSIGN, block);
        moveReturnValueOp.setSrcOperand(0, new Operand(Operand.OperandType.MACRO, "RetReg"));
        moveReturnValueOp.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regularRegister));
        block.appendOper(moveReturnValueOp);

        return regularRegister;
    }
}
