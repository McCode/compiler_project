package parser;

import lowlevel.*;
import java.util.HashMap;

public class VarExpression implements Expression {
    public final String id;
    public final Expression expr;

    public VarExpression(String id) {
        this.id = id;
        this.expr = null;
    }

    public VarExpression(String id, Expression expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "VarExpression");
        if(this.expr == null) {
            printlnWithIndentation(indentLevel + 1, id);
        } else {
            printlnWithIndentation(indentLevel + 1, id + "[]");
            expr.printTree(indentLevel + 1);
        }
    }

    @Override
    public int genLLCode(BasicBlock block) {
        Function func = block.getFunc();
        HashMap funcSymbolTable = func.getTable();
        HashMap globalSymbolTable = func.getGlobalHash();
        if(funcSymbolTable.containsKey(id)) {
            return (int)funcSymbolTable.get(id);
        } else if(globalSymbolTable.containsKey(id)) {
            int returnRegNum = func.getNewRegNum();
            Operation op = new Operation(Operation.OperationType.LOAD_I, block);
            op.setSrcOperand(0, new Operand(Operand.OperandType.STRING, id));
            op.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, returnRegNum));
            block.appendOper(op);
            return returnRegNum;
        } else {
            throw new lowlevel.LowLevelException("Var " + id + " is not defined");
        }
    }
}
