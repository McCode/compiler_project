package parser;

import lowlevel.BasicBlock;
import lowlevel.Function;

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
            return (int)globalSymbolTable.get(id);
        } else {
            throw new lowlevel.LowLevelException("Var " + id + " is not defined");
        }
    }
}
