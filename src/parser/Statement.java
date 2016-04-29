package parser;

import lowlevel.Function;

public interface Statement extends PrintableAstNode {
    void genLLCode(Function func);
}
