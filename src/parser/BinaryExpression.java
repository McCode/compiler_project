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
        int lhsRegNum = lhs.genLLCode(block);
        int rhsRegNum = rhs.genLLCode(block);
        int returnRegNum;
        if(operation == BinaryOperation.Assign) {
            returnRegNum = lhsRegNum;
        } else {
            returnRegNum = block.getFunc().getNewRegNum();
        }

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
                break;
            default:
                throw new LowLevelException("This never happens.");
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
