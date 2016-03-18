package parser;

import scanner.*;

import java.io.Reader;
import java.util.*;

public class CMinusParser implements Parser {
    scanner.Scanner s;

    public CMinusParser(Reader file) {
        s = new CMinusScannerLex(file);
    }


    Token match(Token.TokenType tokenType) throws ParserException {
        Token t = s.getNextToken();
        if(t.getTokenType() != tokenType) {
            throw new ParserException("Expected " + tokenType.toString() + " here.");
        } else {
            return t;
        }
    }

    @Override
    public Program parse() throws ParserException {
        List<Declaration> declarations = new ArrayList<>();

        while(s.viewNextToken() != Token.TokenType.EOF_TOKEN) {
            declarations.add(parseDeclaration());
        }

        return new Program(declarations);
    }

    Declaration parseDeclaration() throws ParserException {
        Token.TokenType type = s.getNextToken();
        String id = (String) match(Token.TokenType.ID_TOKEN).getTokenData();

        switch(type) {
            case INT_TOKEN:
                switch(s.viewNextToken().getTokenType()) {
                    case LPAREN_TOKEN:
                        return parseFunctionDeclaration(TypeSpecifier.Int, id);
                    case LBRACKET_TOKEN:
                    case SEMI_TOKEN:
                        return parseVarDeclaration(id);
                    default:
                        throw new ParserException(s.viewNextToken().getTokenType().toString() + " not expected.");
                }
            case VOID_TOKEN:
                return parseFunctionDeclaration(TypeSpecifier.Void, id);
            default:
                throw new ParserException("Type expected.");
        }
    }

    FunctionDeclaration parseFunctionDeclaration(TypeSpecifier type, String id) {


        return null;
    }

    VarDeclaration parseVarDeclaration(String id) throws ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case SEMI_TOKEN:
                match(Token.TokenType.SEMI_TOKEN);
                return new VarDeclaration(id);
            case LBRACKET_TOKEN:
                match(Token.TokenType.LBRACKET_TOKEN);
                int num = Integer.decode((String)match(Token.TokenType.NUM_TOKEN).getTokenData());
                return new VarDeclaration(id, num);
            default:
                throw new ParserException("Expected [ or ;");
        }
    }

    Statement parseStatement() {
        return null;
    }

    ExpressionStatement parseExpressionStatement() {
        return null;
    }

    Expression parseExpression() {
        return null;
    }




    @Override
    public void printTree() {

    }
}
