package parser;

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
        printlnWithIndentation(indentLevel + 1, type.toString().toLowerCase());

        // print the id
        printlnWithIndentation(indentLevel + 1, id);

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
}
