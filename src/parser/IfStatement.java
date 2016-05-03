package parser;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import lowlevel.BasicBlock;
import lowlevel.Function;
import lowlevel.Operand;
import lowlevel.Operation;

public class IfStatement implements Statement {
    public final Expression expr;
    public final Statement thenStmt;
    public final Statement elseStmt;

    public IfStatement(Expression expr, Statement stmt) {
        this(expr, stmt, null);
    }

    public IfStatement(Expression expr, Statement thenStmt, Statement elseStmt) {
        this.expr = expr;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "IfStatement");
        expr.printTree(indentLevel + 1);
        thenStmt.printTree(indentLevel + 1);
        if(elseStmt != null) {
            elseStmt.printTree(indentLevel + 1);
        }
    }

    @Override
    public void genLLCode(Function func) {
        BasicBlock block = func.getCurrBlock();

        BasicBlock thenBlock = new BasicBlock(func);
        BasicBlock postBlock = new BasicBlock(func);

        int conditionReg = expr.genLLCode(block);

        if(elseStmt == null) {
            Operation branchOp = new Operation(Operation.OperationType.BEQ, block);
            branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionReg));
            branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
            branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, postBlock.getBlockNum()));

            block.appendOper(branchOp);
            func.appendToCurrentBlock(thenBlock);
            func.setCurrBlock(thenBlock);
            thenStmt.genLLCode(func);
            func.appendToCurrentBlock(postBlock);
            func.setCurrBlock(postBlock);
        } else {
            BasicBlock elseBlock = new BasicBlock(func);

            Operation branchOp = new Operation(Operation.OperationType.BEQ, block);
            branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionReg));
            branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
            branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, elseBlock.getBlockNum()));
            block.appendOper(branchOp);

            func.appendToCurrentBlock(thenBlock);
            func.setCurrBlock(thenBlock);
            thenStmt.genLLCode(func);
            func.appendToCurrentBlock(postBlock);

            func.setCurrBlock(elseBlock);
            elseStmt.genLLCode(func);
            BasicBlock endElseBlock = elseBlock;
            while(endElseBlock.getNextBlock() != null) {
                endElseBlock = endElseBlock.getNextBlock();
            }
            if(endElseBlock.getLastOper() != null && endElseBlock.getLastOper().getType() != Operation.OperationType.JMP) {
                Operation branchBackOp = new Operation(Operation.OperationType.JMP, endElseBlock);
                branchBackOp.setSrcOperand(0, new Operand(Operand.OperandType.BLOCK, postBlock.getBlockNum()));
                endElseBlock.appendOper(branchBackOp);
            }

            func.appendUnconnectedBlock(elseBlock);

            func.setCurrBlock(postBlock);
        }
    }
}
