package parser;

import lowlevel.*;

import java.util.HashMap;
import java.util.List;

public class FunctionDeclaration implements Declaration {
    public final TypeSpecifier type;
    public final String id;
    public final List<Param> params;
    public final CompoundStatement stmt;

    public FunctionDeclaration(TypeSpecifier type, String id, List<Param> params, CompoundStatement stmt) {
        this.type = type;
        this.id = id;
        this.params = params;
        this.stmt = stmt;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "FunctionDeclaration");

        // print the type
        printlnWithIndentation(indentLevel + 1, "Type: " + type.toString().toLowerCase());

        // print the id
        printlnWithIndentation(indentLevel + 1, "Id: " + id);

        // print the params
        printlnWithIndentation(indentLevel + 1, "Params");
        if(params.size() > 0) {
            for(Param p: params) {
                p.printTree(indentLevel + 2);
            }
        } else {
            printlnWithIndentation(indentLevel + 2, "None");
        }

        // print the compound-statement
        stmt.printTree(indentLevel + 1);
    }

    @Override
    public CodeItem genLLCode(HashMap globalHash) {
        int intType = 0;
        switch(type) {
            case Void:
                intType = Data.TYPE_VOID;
                break;
            case Int:
                intType = Data.TYPE_INT;
                break;
        }

        Function func = new lowlevel.Function(intType, id, getParamsAsFuncParam());

        for(Param p: params) {
            func.getTable().put(p.id, func.getNewRegNum());
        }

        func.createBlock0();
        func.setCurrBlock(func.getFirstBlock());
        func.setGlobalHash(globalHash);

        stmt.genLLCode(func);

        func.appendBlock(func.getReturnBlock());
        if(func.getFirstUnconnectedBlock() != null) {
            func.appendBlock(func.getFirstUnconnectedBlock());
        }

        return func;
    }

    private FuncParam getParamsAsFuncParam() {
        if(params.size() > 0) {
            FuncParam p = new FuncParam(Data.TYPE_INT, params.get(0).id, params.get(0).isArray);
            FuncParam ptr = p;
            for(int i = 1; i < params.size(); i++) {
                ptr.setNextParam(new FuncParam(Data.TYPE_INT, params.get(i).id, params.get(i).isArray));
                ptr = ptr.getNextParam();
            }

            return p;
        } else {
            return null;
        }
    }
}
