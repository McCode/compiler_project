package parser;

import lowlevel.BasicBlock;
import lowlevel.Operand;
import lowlevel.Operation;

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

    @Override
    public int genLLCode(BasicBlock block) {
        Operation op = new Operation(Operation.OperationType.LOAD_I, block);
        Operand srcOperand = new Operand(Operand.OperandType.INTEGER, num);
        op.setSrcOperand(0, srcOperand);
        int regNum = block.getFunc().getNewRegNum();
        Operand destOperand = new Operand(Operand.OperandType.REGISTER, regNum);
        op.setDestOperand(0, destOperand);

        block.appendOper(op);
        return regNum;
    }
}
