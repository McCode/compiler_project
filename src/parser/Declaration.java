package parser;

import lowlevel.CodeItem;

import java.util.HashMap;

public interface Declaration extends PrintableAstNode {
    public CodeItem genLLCode(HashMap globalHash);
}
