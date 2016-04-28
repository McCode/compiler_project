package parser;

import lowlevel.BasicBlock;

public interface Expression extends PrintableAstNode {
    int genLLCode(BasicBlock block);
}
