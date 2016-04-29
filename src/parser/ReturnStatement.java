package parser;

import lowlevel.*;

public class ReturnStatement implements Statement {
    public final Expression expr;

    public ReturnStatement() {
        this.expr = null;
    }

    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "ReturnStatement");

        if(expr == null) {
            printlnWithIndentation(indentLevel + 1, "Empty");
        } else {
            expr.printTree(indentLevel + 1);
        }
    }

    @Override
    public void genLLCode(Function func) {
        BasicBlock block = func.getCurrBlock();
        if(expr != null) {
            int resultReg = expr.genLLCode(block);
            Operation moveReturnValueOp = new Operation(Operation.OperationType.ASSIGN, block);
            moveReturnValueOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, resultReg));
            moveReturnValueOp.setDestOperand(0, new Operand(Operand.OperandType.MACRO, "RetReg"));
            block.appendOper(moveReturnValueOp);
        }

        Operation returnOp = new Operation(Operation.OperationType.JMP, block);
        returnOp.setSrcOperand(0, new Operand(Operand.OperandType.BLOCK, func.getReturnBlock().getBlockNum()));
        block.appendOper(returnOp);
    }
}
