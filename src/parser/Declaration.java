package parser;

import lowlevel.CodeItem;

public interface Declaration extends PrintableAstNode {
    public CodeItem genLLCode();
}
