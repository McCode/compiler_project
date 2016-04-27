package parser;

import lowlevel.BasicBlock;

public interface Statement extends PrintableAstNode {
    void genLLCode(BasicBlock block);
}
