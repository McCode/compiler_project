package parser;

import lowlevel.BasicBlock;

import java.util.HashMap;
import java.util.List;

public class CompoundStatement implements Statement {
    public final List<VarDeclaration> declarations;
    public final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    @Override
    public void printTree(int indentLevel) {
        printlnWithIndentation(indentLevel, "CompoundStatement");

        printlnWithIndentation(indentLevel + 1, "VarDeclarations");
        for(VarDeclaration varDecl: declarations) {
            varDecl.printTree(indentLevel + 2);
        }

        printlnWithIndentation(indentLevel + 1, "Statements");
        for(Statement s: statements) {
            s.printTree(indentLevel + 2);
        }
    }

    @Override
    public void genLLCode(BasicBlock block) {
        HashMap symbolTable = block.getFunc().getTable();

        for(VarDeclaration d: declarations) {
            symbolTable.put(d.id, block.getFunc().getNewRegNum());
        }

        for(Statement s: statements) {
            s.genLLCode(block);
        }
    }
}
