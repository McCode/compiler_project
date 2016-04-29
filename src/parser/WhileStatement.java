package parser;

import lowlevel.*;

public class WhileStatement implements Statement {
    public final Expression expr;
    public final Statement stmt;

    public WhileStatement(Expression expr, Statement stmt) {
        this.expr = expr;
        this.stmt = stmt;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "WhileStatement");
        expr.printTree(indentLevel + 1);
        stmt.printTree(indentLevel + 1);
    }

    @Override
    public void genLLCode(Function func) {
        BasicBlock block = func.getCurrBlock();

        BasicBlock loopBlock = new BasicBlock(func);
        BasicBlock postBlock = new BasicBlock(func);

        int conditionReg = expr.genLLCode(block);

        Operation branchOp = new Operation(Operation.OperationType.BEQ, block);
        branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionReg));
        branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, postBlock.getBlockNum()));
        block.appendOper(branchOp);

        func.appendToCurrentBlock(loopBlock);
        func.setCurrBlock(loopBlock);
        stmt.genLLCode(func);

        conditionReg = expr.genLLCode(block);
        Operation loopOp = new Operation(Operation.OperationType.BNE, block);
        branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionReg));
        branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, loopBlock.getBlockNum()));
        block.appendOper(branchOp);

        func.appendToCurrentBlock(postBlock);
        func.setCurrBlock(postBlock);
    }
}
