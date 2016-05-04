package parser;

import lowlevel.*;

public class BinaryExpression implements Expression {
    public final Expression lhs;
    public final Expression rhs;
    public final BinaryOperation operation;

    public BinaryExpression(Expression lhs, Expression rhs, BinaryOperation operation) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operation = operation;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "BinaryExpression: " + operation.toString());
        lhs.printTree(indentLevel + 1);
        rhs.printTree(indentLevel + 1);
    }

    @Override
    public int genLLCode(BasicBlock block) {
        Operation.OperationType opType;

        switch(operation) {
            case LessThanOrEqualTo:
                opType = Operation.OperationType.LTE;
                break;
            case LessThan:
                opType = Operation.OperationType.LT;
                break;
            case GreaterThan:
                opType = Operation.OperationType.GT;
                break;
            case GreaterThanOrEqualTo:
                opType = Operation.OperationType.GTE;
                break;
            case Equal:
                opType = Operation.OperationType.EQUAL;
                break;
            case NotEqual:
                opType = Operation.OperationType.NOT_EQUAL;
                break;
            case Plus:
                opType = Operation.OperationType.ADD_I;
                break;
            case Minus:
                opType = Operation.OperationType.SUB_I;
                break;
            case Times:
                opType = Operation.OperationType.MUL_I;
                break;
            case Over:
                opType = Operation.OperationType.DIV_I;
                break;
            case Assign:
                opType = Operation.OperationType.ASSIGN;

                VarExpression var = (VarExpression)lhs;
                if(block.getFunc().getTable().containsKey(var.id)) {
                    break;
                } else if(block.getFunc().getGlobalHash().containsKey(var.id)) {
                    int rhsRegNum = rhs.genLLCode(block);
                    Operation storeOp = new Operation(Operation.OperationType.STORE_I, block);
                    storeOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, rhsRegNum));
                    storeOp.setSrcOperand(1, new Operand(Operand.OperandType.STRING, var.id));
                    block.appendOper(storeOp);

                    return rhsRegNum;
                } else {
                    throw new LowLevelException("Variable " + var.id + " is not defined.");
                }
            default:
                throw new LowLevelException("This never happens.");
        }

        int lhsRegNum = lhs.genLLCode(block);
        int rhsRegNum = rhs.genLLCode(block);
        int returnRegNum;
        if(operation == BinaryOperation.Assign) {
            returnRegNum = lhsRegNum;
        } else {
            returnRegNum = block.getFunc().getNewRegNum();
        }

        Operation op = new Operation(opType, block);
        if(operation == BinaryOperation.Assign) {
            op.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, rhsRegNum));
        } else {
            op.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, lhsRegNum));
            op.setSrcOperand(1, new Operand(Operand.OperandType.REGISTER, rhsRegNum));
        }

        op.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, returnRegNum));
        block.appendOper(op);

        return returnRegNum;
    }
}
