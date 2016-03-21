package parser;

import scanner.*;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CMinusParser implements Parser {
    scanner.Scanner s;

    public CMinusParser(Reader file) {
        s = new CMinusScannerLex(file);
    }


    Token match(Token.TokenType tokenType) throws ParserException, IOException, LexicalErrorException {
        Token t = s.getNextToken();
        if(t.getTokenType() != tokenType) {
            throw new ParserException("Expected " + tokenType.toString() + ", found " + getNextTokenTypeAsString());
        } else {
            return t;
        }
    }

    @Override
    public Program parse() throws ParserException, IOException, LexicalErrorException {
        List<Declaration> declarations = new ArrayList<>();

        while(viewNextTokenType() != Token.TokenType.EOF_TOKEN) {
            declarations.add(parseDeclaration());
        }

        return new Program(declarations);
    }

    Declaration parseDeclaration() throws ParserException, IOException, LexicalErrorException {
        Token.TokenType type = s.getNextToken().getTokenType();
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
                        throw new ParserException(getNextTokenTypeAsString() + " not expected.");
                }
            case VOID_TOKEN:
                return parseFunctionDeclaration(TypeSpecifier.Void, id);
            default:
                throw new ParserException("Type expected.");
        }
    }

    FunctionDeclaration parseFunctionDeclaration(TypeSpecifier type, String id) throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LPAREN_TOKEN);

        List<Param> params = parseParams();

        CompoundStatement compoundStatement = parseCompoundStatement();

        return new FunctionDeclaration(type, id, params, compoundStatement);
    }

    VarDeclaration parseVarDeclaration() throws ParserException, IOException, LexicalErrorException {
        String id = (String)match(Token.TokenType.ID_TOKEN).getTokenData();
        return parseVarDeclaration(id);
    }

    VarDeclaration parseVarDeclaration(String id) throws ParserException, IOException, LexicalErrorException {
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

    List<Param> parseParams() throws IOException, LexicalErrorException, ParserException {
        List<Param> params = new ArrayList<>();

        if(s.viewNextToken().getTokenType() == Token.TokenType.VOID_TOKEN) {
            match(Token.TokenType.VOID_TOKEN);
        } else {
            while(s.viewNextToken().getTokenType() != Token.TokenType.RPAREN_TOKEN) {
                params.add(parseParam());

                switch(s.viewNextToken().getTokenType()) {
                    case COMMA_TOKEN:
                        match(Token.TokenType.COMMA_TOKEN);
                        break;
                    case RPAREN_TOKEN:
                        break;
                    default:
                        throw new ParserException("Unexpected token: " + getNextTokenTypeAsString());
                }
            }
        }

        return params;
    }

    Param parseParam() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.INT_TOKEN);
        String id = (String)match(Token.TokenType.ID_TOKEN).getTokenData();

        boolean isArray = false;
        if(s.viewNextToken().getTokenType() == Token.TokenType.LBRACKET_TOKEN) {
            match(Token.TokenType.LBRACKET_TOKEN);
            match(Token.TokenType.RBRACKET_TOKEN);
            isArray = true;
        }

        return new Param(id, isArray);
    }

    CompoundStatement parseCompoundStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LCURLY_TOKEN);

        List<VarDeclaration> varDeclarations = new ArrayList<>();
        while(s.viewNextToken().getTokenType() == Token.TokenType.INT_TOKEN) {
            varDeclarations.add(parseVarDeclaration());
        }

        List<Statement> statements = new ArrayList<>();
        while(s.viewNextToken().getTokenType() != Token.TokenType.RCURLY_TOKEN) {
            statements.add(parseStatement());
        }

        match(Token.TokenType.RCURLY_TOKEN);

        return new CompoundStatement(varDeclarations, statements);
    }

    Statement parseStatement() throws IOException, LexicalErrorException, ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case IF_TOKEN:
                return parseIfStatement();
            case WHILE_TOKEN:
                return parseWhileStatement();
            case RETURN_TOKEN:
                return parseReturnStatement();
            case LCURLY_TOKEN:
                return parseCompoundStatement();
            case NUM_TOKEN:
            case LPAREN_TOKEN:
            case ID_TOKEN:
                return parseExpressionStatement();
            default:
                throw new ParserException("Unexpected " + getNextTokenTypeAsString());
        }
    }

    ExpressionStatement parseExpressionStatement() throws ParserException, IOException, LexicalErrorException {
        Expression stmt = parseExpression();
        match(Token.TokenType.SEMI_TOKEN);
        return new ExpressionStatement(stmt);
    }

    IfStatement parseIfStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.IF_TOKEN);
        match(Token.TokenType.LPAREN_TOKEN);
        Expression expr = parseExpression();
        match(Token.TokenType.RPAREN_TOKEN);

        Statement stmt = parseStatement();

        if(s.viewNextToken().getTokenType() == Token.TokenType.ELSE_TOKEN) {
            match(Token.TokenType.ELSE_TOKEN);
            return new IfStatement(expr, stmt, parseStatement());
        } else {
            return new IfStatement(expr, stmt);
        }
    }

    WhileStatement parseWhileStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.WHILE_TOKEN);
        match(Token.TokenType.LPAREN_TOKEN);
        Expression expr = parseExpression();
        match(Token.TokenType.RPAREN_TOKEN);
        Statement stmt = parseStatement();
        return new WhileStatement(expr, stmt);
    }

    ReturnStatement parseReturnStatement() throws IOException, LexicalErrorException, ParserException {
        match(Token.TokenType.RETURN_TOKEN);
        switch(s.viewNextToken().getTokenType()) {
            case NUM_TOKEN:
            case ID_TOKEN:
            case LPAREN_TOKEN:
                return new ReturnStatement(parseExpression());
            case SEMI_TOKEN:
                return new ReturnStatement();
            default:
                throw new ParserException("Expression or semicolon expected, found " + getNextTokenTypeAsString());
        }
    }

    Expression parseExpression() throws ParserException, IOException, LexicalErrorException {
        return parseSimpleExpression();
        /*switch(s.viewNextToken().getTokenType()) {
            case NUM_TOKEN:
                NumExpression num = parseNumExpression();
                break;
            case LPAREN_TOKEN:
                break;
            case ID_TOKEN:
                break;
            default:
                throw new ParserException("Expression expected, found " + getNextTokenTypeAsString());
        }
        return null;*/
    }

    Expression parseSimpleExpression() throws IOException, LexicalErrorException, ParserException {
        Expression lhs = parseAdditiveExpression();
        switch(viewNextTokenType()) {
            case LTE_TOKEN:
                match(Token.TokenType.LTE_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.LessThanOrEqualTo);
            case LT_TOKEN:
                match(Token.TokenType.LT_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.LessThan);
            case GT_TOKEN:
                match(Token.TokenType.GT_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.GreaterThan);
            case GTE_TOKEN:
                match(Token.TokenType.GTE_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.GreaterThanOrEqualTo);
            case EQ_TOKEN:
                match(Token.TokenType.EQ_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.Equal);
            case NEQ_TOKEN:
                match(Token.TokenType.NEQ_TOKEN);
                return new BinaryExpression(lhs, parseAdditiveExpression(), BinaryOperation.NotEqual);
            case SEMI_TOKEN:
            case RPAREN_TOKEN:
            case RBRACKET_TOKEN:
            case COMMA_TOKEN:
                return lhs;
            default:
                throw new ParserException("Unexpected " + getNextTokenTypeAsString());
        }
    }

    Expression parseAdditiveExpression() throws IOException, LexicalErrorException, ParserException {
        Expression term = parseTerm();
        switch(viewNextTokenType()) {
            case PLUS_TOKEN:
                match(Token.TokenType.PLUS_TOKEN);
                return new BinaryExpression(term, parseAdditiveExpression(), BinaryOperation.Plus);
            case MINUS_TOKEN:
                match(Token.TokenType.MINUS_TOKEN);
                return new BinaryExpression(term, parseAdditiveExpression(), BinaryOperation.Minus);
            case SEMI_TOKEN:
            case RPAREN_TOKEN:
            case RBRACKET_TOKEN:
            case COMMA_TOKEN:
            case LTE_TOKEN:
            case LT_TOKEN:
            case GT_TOKEN:
            case GTE_TOKEN:
            case EQ_TOKEN:
            case NEQ_TOKEN:
                return term;
            default:
                throw new ParserException("Unexpected " + getNextTokenTypeAsString());
        }
    }

    Expression parseTerm() throws ParserException, IOException, LexicalErrorException {
        Expression factor = parseFactor();
        return parseTerm(factor);
    }

    Expression parseTerm(Expression factor) throws IOException, LexicalErrorException, ParserException {
        switch(viewNextTokenType()) {
            case TIMES_TOKEN:
                match(Token.TokenType.TIMES_TOKEN);
                return new BinaryExpression(factor, parseTerm(), BinaryOperation.Times);
            case OVER_TOKEN:
                match(Token.TokenType.OVER_TOKEN);
                return new BinaryExpression(factor, parseTerm(), BinaryOperation.Over);
            case SEMI_TOKEN:
            case RPAREN_TOKEN:
            case RBRACKET_TOKEN:
            case COMMA_TOKEN:
            case LTE_TOKEN:
            case LT_TOKEN:
            case GT_TOKEN:
            case GTE_TOKEN:
            case EQ_TOKEN:
            case NEQ_TOKEN:
            case PLUS_TOKEN:
            case MINUS_TOKEN:
                return factor;
            default:
                throw new ParserException("Unexpected " + getNextTokenTypeAsString());
        }
    }

    Expression parseFactor() throws IOException, LexicalErrorException, ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case LPAREN_TOKEN:
                match(Token.TokenType.LPAREN_TOKEN);
                Expression expr = parseExpression();
                match(Token.TokenType.RPAREN_TOKEN);
                return expr;
            case NUM_TOKEN:
                return parseNumExpression();
            case ID_TOKEN:
                String id = (String)match(Token.TokenType.ID_TOKEN).getTokenData();
                switch(s.viewNextToken().getTokenType()) {
                    case LPAREN_TOKEN:
                        return parseCall(id);
                    case LBRACKET_TOKEN:
                    case TIMES_TOKEN:
                    case OVER_TOKEN:
                    case SEMI_TOKEN:
                    case RPAREN_TOKEN:
                    case RBRACKET_TOKEN:
                    case COMMA_TOKEN:
                    case LTE_TOKEN:
                    case LT_TOKEN:
                    case GT_TOKEN:
                    case GTE_TOKEN:
                    case EQ_TOKEN:
                    case NEQ_TOKEN:
                    case PLUS_TOKEN:
                    case MINUS_TOKEN:
                        return parseVar(id);
                    default:
                        throw new ParserException("Unexpected " + getNextTokenTypeAsString());
                }
            default:
                throw new ParserException("Expected factor, found " + getNextTokenTypeAsString());
        }
    }

    CallExpression parseCall() throws ParserException, IOException, LexicalErrorException {
        String id = (String) match(Token.TokenType.ID_TOKEN).getTokenData();
        return parseCall(id);
    }

    CallExpression parseCall(String id) throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LPAREN_TOKEN);
        List<Expression> args = parseArgs();
        match(Token.TokenType.RPAREN_TOKEN);

        return new CallExpression(id, args);
    }

    List<Expression> parseArgs() throws IOException, LexicalErrorException, ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case RPAREN_TOKEN:
                return new ArrayList<>();
            case NUM_TOKEN:
            case ID_TOKEN:
            case LPAREN_TOKEN:
                List<Expression> args = new ArrayList<>();
                args.add(parseExpression());
                while(s.viewNextToken().getTokenType() != Token.TokenType.RPAREN_TOKEN) {
                    match(Token.TokenType.COMMA_TOKEN);
                    args.add(parseExpression());
                }
                return args;
            default:
                throw new ParserException("Expected expression, found " + getNextTokenTypeAsString());
        }
    }

    VarExpression parseVar() throws ParserException, IOException, LexicalErrorException {
        String id = (String) match(Token.TokenType.ID_TOKEN).getTokenData();
        return parseVar(id);
    }

    VarExpression parseVar(String id) throws IOException, LexicalErrorException, ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case LBRACKET_TOKEN:
                match(Token.TokenType.LBRACKET_TOKEN);
                Expression expr = parseExpression();
                match(Token.TokenType.RBRACKET_TOKEN);
                return new VarExpression(id, expr);
            case TIMES_TOKEN:
            case OVER_TOKEN:
            case SEMI_TOKEN:
            case RPAREN_TOKEN:
            case RBRACKET_TOKEN:
            case COMMA_TOKEN:
            case LTE_TOKEN:
            case LT_TOKEN:
            case GT_TOKEN:
            case GTE_TOKEN:
            case EQ_TOKEN:
            case NEQ_TOKEN:
            case PLUS_TOKEN:
            case MINUS_TOKEN:
                return new VarExpression(id);
            default:
                throw new ParserException("Unexpected token: " + getNextTokenTypeAsString());
        }
    }

    NumExpression parseNumExpression() throws ParserException, IOException, LexicalErrorException {
        return new NumExpression(Integer.parseInt(match(Token.TokenType.NUM_TOKEN).toString()));
    }




    @Override
    public void printTree() {

    }

    // Helper functions
    String getNextTokenTypeAsString() throws IOException, LexicalErrorException {
        return viewNextTokenType().toString();
    }

    Token.TokenType viewNextTokenType() throws IOException, LexicalErrorException {
        return s.viewNextToken().getTokenType();
    }
}
