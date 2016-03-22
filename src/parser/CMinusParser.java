package parser;

import scanner.*;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class CMinusParser implements Parser {
    private final scanner.Scanner s;

    // Stores the program produced by parse() so that printTree() works
    private Program p;

    public CMinusParser(Reader file) {
        s = new CMinusScannerLex(file);
    }


    @Override
    public void printTree() {
        p.printTree(0);
    }

    // Creates an AST from the scanner given to the constructor.
    // This could also be called parseProgram(), as it parses a program.
    @Override
    public Program parse() throws ParserException, IOException, LexicalErrorException {
        List<Declaration> declarations = new ArrayList<>();

        while(viewNextTokenType() != Token.TokenType.EOF_TOKEN) {
            declarations.add(parseDeclaration());
        }

        while(true) {
            switch(viewNextTokenType()) {
                case INT_TOKEN:
                case VOID_TOKEN:
                    declarations.add(parseDeclaration());
                    break;
                case EOF_TOKEN:
                    return p = new Program(declarations);
                default:
                    throw new ParserException("Expected declaration, found " + getNextTokenTypeAsString());
            }
        }
    }

    // Parses both var declarations and function declarations.
    private Declaration parseDeclaration() throws ParserException, IOException, LexicalErrorException {
        Token.TokenType type = s.getNextToken().getTokenType();

        // We had to switch over the type before we switch over it again so that
        // any errors encountered in the first token are reported before the match() below
        switch(type) {
            case INT_TOKEN:
            case VOID_TOKEN:
                break;
            default:
                throw new ParserException("Expected type, found " + type);
        }
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

    /**
     * Parses a function declaration given a return type and a function name.
     * @param type The return type of the function
     * @param id   The name of the function
     */
    private FunctionDeclaration parseFunctionDeclaration(TypeSpecifier type, String id) throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LPAREN_TOKEN);

        List<Param> params = parseParams();

        match(Token.TokenType.RPAREN_TOKEN);

        CompoundStatement compoundStatement = parseCompoundStatement();

        return new FunctionDeclaration(type, id, params, compoundStatement);
    }

    /*
     * Parses a var declaration, without being given the id.
     * It just matches INT and an ID, and passes those to parseVarDeclaration(String).
     */
    private VarDeclaration parseVarDeclaration() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.INT_TOKEN);
        String id = (String)match(Token.TokenType.ID_TOKEN).getTokenData();
        return parseVarDeclaration(id);
    }

    /**
     * Parses a ver declaration given the string name of the variable.
     * @param id The name of the variable
     */
    private VarDeclaration parseVarDeclaration(String id) throws ParserException, IOException, LexicalErrorException {
        switch(s.viewNextToken().getTokenType()) {
            case SEMI_TOKEN:
                match(Token.TokenType.SEMI_TOKEN);
                return new VarDeclaration(id);
            case LBRACKET_TOKEN:
                match(Token.TokenType.LBRACKET_TOKEN);
                int num = Integer.decode((String)match(Token.TokenType.NUM_TOKEN).getTokenData());
                match(Token.TokenType.RBRACKET_TOKEN);
                match(Token.TokenType.SEMI_TOKEN);
                return new VarDeclaration(id, num);
            default:
                throw new ParserException("Expected [ or ;");
        }
    }

    // Parses parameters in a function declaration.
    private List<Param> parseParams() throws IOException, LexicalErrorException, ParserException {
        List<Param> params = new ArrayList<>();

        if(viewNextTokenType() == Token.TokenType.VOID_TOKEN) {
            // If the first token is a VOID, we pass back an empty param list.
            match(Token.TokenType.VOID_TOKEN);
        } else {
            // Parse params until we hit a right paren.
            while(viewNextTokenType() != Token.TokenType.RPAREN_TOKEN) {
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

    // Parses a single parameter in a function declaration
    private Param parseParam() throws ParserException, IOException, LexicalErrorException {
        // All params are ints
        match(Token.TokenType.INT_TOKEN);
        String id = (String)match(Token.TokenType.ID_TOKEN).getTokenData();

        boolean isArray = false;

        // Check to see if it's an array parameter
        if(viewNextTokenType() == Token.TokenType.LBRACKET_TOKEN) {
            match(Token.TokenType.LBRACKET_TOKEN);
            match(Token.TokenType.RBRACKET_TOKEN);
            isArray = true;
        }

        return new Param(id, isArray);
    }

    private CompoundStatement parseCompoundStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LCURLY_TOKEN);

        List<VarDeclaration> varDeclarations = new ArrayList<>();
        while(viewNextTokenType() == Token.TokenType.INT_TOKEN) {
            varDeclarations.add(parseVarDeclaration());
        }

        List<Statement> statements = new ArrayList<>();
        while(viewNextTokenType() != Token.TokenType.RCURLY_TOKEN) {
            statements.add(parseStatement());
        }

        match(Token.TokenType.RCURLY_TOKEN);

        return new CompoundStatement(varDeclarations, statements);
    }

    // Decides what kind of statement it's looking at and passes it along to the right specific method
    private Statement parseStatement() throws IOException, LexicalErrorException, ParserException {
        switch(viewNextTokenType()) {
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
            case SEMI_TOKEN:
                return parseExpressionStatement();
            default:
                throw new ParserException("Expected statement, got " + getNextTokenTypeAsString());
        }
    }

    // Parses an expression-statement, which may or may not have an expression.
    private ExpressionStatement parseExpressionStatement() throws ParserException, IOException, LexicalErrorException {
        Expression expr = null;
        switch(viewNextTokenType()) {
            case NUM_TOKEN:
            case ID_TOKEN:
            case LPAREN_TOKEN:
                expr = parseExpression();
                break;
            case SEMI_TOKEN:
                // A single semicolon is an ExpressionStatement with no expression.
                break;
            default:
                throw new ParserException("Expected expression statement, found " + getNextTokenTypeAsString());
        }

        match(Token.TokenType.SEMI_TOKEN);
        return new ExpressionStatement(expr);
    }

    // Parses an if statement, which may or may not have an else statement.
    private IfStatement parseIfStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.IF_TOKEN);
        match(Token.TokenType.LPAREN_TOKEN);
        Expression expr = parseExpression();
        match(Token.TokenType.RPAREN_TOKEN);

        Statement stmt = parseStatement();
        Statement elseStmt = null;

        if(viewNextTokenType() == Token.TokenType.ELSE_TOKEN) {
            match(Token.TokenType.ELSE_TOKEN);
            elseStmt = parseStatement();
        }

        return new IfStatement(expr, stmt, elseStmt);
    }

    private WhileStatement parseWhileStatement() throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.WHILE_TOKEN);

        match(Token.TokenType.LPAREN_TOKEN);
        Expression expr = parseExpression();
        match(Token.TokenType.RPAREN_TOKEN);

        Statement stmt = parseStatement();

        return new WhileStatement(expr, stmt);
    }

    // Parses a return statement, which may or may not have an expression.
    private ReturnStatement parseReturnStatement() throws IOException, LexicalErrorException, ParserException {
        match(Token.TokenType.RETURN_TOKEN);
        switch(s.viewNextToken().getTokenType()) {
            case NUM_TOKEN:
            case ID_TOKEN:
            case LPAREN_TOKEN:
                Expression expr = parseExpression();
                match(Token.TokenType.SEMI_TOKEN);
                return new ReturnStatement(expr);
            case SEMI_TOKEN:
                match(Token.TokenType.SEMI_TOKEN);
                return new ReturnStatement();
            default:
                throw new ParserException("Expression or semicolon expected, found " + getNextTokenTypeAsString());
        }
    }

    /* This corresponds to expression in the grammar.
     * Parses an expression.
     * Treats an ID as a special case in order to handle assignment.
     */
    private Expression parseExpression() throws ParserException, IOException, LexicalErrorException {
        switch(viewNextTokenType()) {
            case NUM_TOKEN:
            case LPAREN_TOKEN:
                return parseSimpleExpression();
            case ID_TOKEN:
                String id = (String) match(Token.TokenType.ID_TOKEN).getTokenData();
                return parseExpression(id);
            default:
                throw new ParserException("Expected expression, found " + getNextTokenTypeAsString());
        }
    }

    /* This corresponds to expression' in the grammar.
     * This handles the special case of an expression starting with an id.
     */
    private Expression parseExpression(String id) throws ParserException, IOException, LexicalErrorException {
        VarExpression var;
        switch(viewNextTokenType()) {
            case ASSIGN_TOKEN:
                var = parseVar(id);
                match(Token.TokenType.ASSIGN_TOKEN);
                Expression expr = parseExpression();
                return new BinaryExpression(var, expr, BinaryOperation.Assign);
            case LBRACKET_TOKEN:
                var = parseVar(id);
                return parseExpression(var);
            case LPAREN_TOKEN:
                CallExpression call = parseCall(id);
                return parseSimpleExpression(call);
            case LTE_TOKEN:
            case LT_TOKEN:
            case GT_TOKEN:
            case GTE_TOKEN:
            case EQ_TOKEN:
            case NEQ_TOKEN:
            case PLUS_TOKEN:
            case MINUS_TOKEN:
            case TIMES_TOKEN:
            case OVER_TOKEN:
            case SEMI_TOKEN:
            case COMMA_TOKEN:
            case RPAREN_TOKEN:
            case RBRACKET_TOKEN:
                var = new VarExpression(id);
                return parseSimpleExpression(var);
            default:
                throw new ParserException("Expected expression continuation, found " + getNextTokenTypeAsString());
        }
    }

    /* This corresponds to expression'' in the grammar.
     * This handles the special case of an expression starting with an array indexing.
     */
    private Expression parseExpression(VarExpression var) throws IOException, LexicalErrorException, ParserException {
        switch(viewNextTokenType()) {
            case ASSIGN_TOKEN:
                match(Token.TokenType.ASSIGN_TOKEN);
                Expression expr = parseExpression();
                return new BinaryExpression(var, expr, BinaryOperation.Assign);
            case LTE_TOKEN:
            case LT_TOKEN:
            case GT_TOKEN:
            case GTE_TOKEN:
            case EQ_TOKEN:
            case NEQ_TOKEN:
            case PLUS_TOKEN:
            case MINUS_TOKEN:
            case TIMES_TOKEN:
            case OVER_TOKEN:
                return parseSimpleExpression(var);
            case RPAREN_TOKEN:
            case SEMI_TOKEN:
            case RBRACKET_TOKEN:
            case COMMA_TOKEN:
                // the end of the expression
                return var;
            default:
                throw new ParserException("Expected expression continuation, found " + getNextTokenTypeAsString());
        }
    }

    // Parses a simple-expression without being provided a factor.
    private Expression parseSimpleExpression() throws ParserException, IOException, LexicalErrorException {
        return parseSimpleExpression(parseFactor());
    }

    // Parses a simple-expression given a factor, which may be the whole left side or only a part of it.
    // Unlike the other parse*Expression methods, a simple-expression can't be stringed along.
    // It has only two sides, and sometimes only one.
    private Expression parseSimpleExpression(Expression factor) throws IOException, LexicalErrorException, ParserException {
        Expression lhs = parseAdditiveExpression(factor);
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

    // Parses an additive-expression without being given a factor.
    private Expression parseAdditiveExpression() throws ParserException, IOException, LexicalErrorException {
        return parseAdditiveExpression(parseFactor());
    }

    /**
     * Parses an additive-expression given a factor, which may be the entire left side
     * or only a part of it.
     * Since an additive-expression can have multiple addops, it will keep parsing terms
     * and adding them to the tree, moving the tree down and to the left.
     * This ensures left-associativity.
     */
    private Expression parseAdditiveExpression(Expression factor) throws IOException, LexicalErrorException, ParserException {
        // Every time we find another addop and another term, we will construct
        // a new BinaryExpression using the current lhs and the new term, and put the result in lhs.
        Expression lhs = parseTerm(factor);
        Expression rhs;
        // Since it is cumbersome to compare against two token types in the while loop
        // and then compare against them again in the switch, we opted to just loop until we return.
        while(true) {
            switch(viewNextTokenType()) {
                case PLUS_TOKEN:
                    match(Token.TokenType.PLUS_TOKEN);
                    rhs = parseTerm();
                    lhs = new BinaryExpression(lhs, rhs, BinaryOperation.Plus);
                    break;
                case MINUS_TOKEN:
                    match(Token.TokenType.MINUS_TOKEN);
                    rhs = parseTerm();
                    lhs = new BinaryExpression(lhs, rhs, BinaryOperation.Minus);
                    break;
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
                    return lhs;
                default:
                    throw new ParserException("Unexpected " + getNextTokenTypeAsString());
            }
        }
    }

    // Parses a term without being given a factor.
    private Expression parseTerm() throws ParserException, IOException, LexicalErrorException {
        Expression factor = parseFactor();
        return parseTerm(factor);
    }

    /**
     * Parses a term given a factor, which may be the entire left side
     * or only a part of it.
     * Since an additive-expression can have multiple mulops, it will keep parsing terms
     * and adding them to the tree, moving the tree down and to the left.
     * This ensures left-associativity.
     */
    private Expression parseTerm(Expression factor) throws IOException, LexicalErrorException, ParserException {
        // Every time we find another mulop and another factor, we will construct
        // a new BinaryExpression using the current lhs and the new factor, and put the result in lhs.
        Expression lhs = factor;
        Expression rhs;
        // Since it is cumbersome to compare against two token types in the while loop
        // and then compare against them again in the switch, we opted to just loop until we return.
        while(true) {
            switch(viewNextTokenType()) {
                case TIMES_TOKEN:
                    match(Token.TokenType.TIMES_TOKEN);
                    rhs = parseFactor();
                    lhs = new BinaryExpression(lhs, rhs, BinaryOperation.Times);
                    break;
                case OVER_TOKEN:
                    match(Token.TokenType.OVER_TOKEN);
                    rhs = parseFactor();
                    lhs = new BinaryExpression(lhs, rhs, BinaryOperation.Over);
                    break;
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
                    return lhs;
                default:
                    throw new ParserException("Unexpected " + getNextTokenTypeAsString());
            }
        }
    }

    /* Parses a factor, which may be:
     *      a num
     *      a parenthetical expression
     *      a call
     *      a var
     *      an indexed array
     */
    private Expression parseFactor() throws IOException, LexicalErrorException, ParserException {
        switch(s.viewNextToken().getTokenType()) {
            case LPAREN_TOKEN:
                // it's a parenthetical expression
                match(Token.TokenType.LPAREN_TOKEN);
                Expression expr = parseExpression();
                match(Token.TokenType.RPAREN_TOKEN);
                return expr;
            case NUM_TOKEN:
                return parseNumExpression();
            case ID_TOKEN:
                // it could be a var, an indexed array, or a call
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

    // parses a call, given an id
    private CallExpression parseCall(String id) throws ParserException, IOException, LexicalErrorException {
        match(Token.TokenType.LPAREN_TOKEN);
        List<Expression> args = parseArgs();
        match(Token.TokenType.RPAREN_TOKEN);

        return new CallExpression(id, args);
    }

    // parses a list of args, which may be empty
    private List<Expression> parseArgs() throws IOException, LexicalErrorException, ParserException {
        switch(viewNextTokenType()) {
            case RPAREN_TOKEN:
                // no arguments
                return new ArrayList<>();
            case NUM_TOKEN:
            case ID_TOKEN:
            case LPAREN_TOKEN:
                // there are arguments
                List<Expression> args = new ArrayList<>();
                args.add(parseExpression());
                while(viewNextTokenType() != Token.TokenType.RPAREN_TOKEN) {
                    match(Token.TokenType.COMMA_TOKEN);
                    args.add(parseExpression());
                }
                return args;
            default:
                throw new ParserException("Expected expression, found " + getNextTokenTypeAsString());
        }
    }

    // parses a var expression given an id.  It may just be a var, or it might be an indexed array.
    private VarExpression parseVar(String id) throws IOException, LexicalErrorException, ParserException {
        switch(viewNextTokenType()) {
            case LBRACKET_TOKEN:
                // It's an indexed array
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
            case ASSIGN_TOKEN:
                // It's just a var
                return new VarExpression(id);
            default:
                throw new ParserException("Unexpected token: " + getNextTokenTypeAsString());
        }
    }

    // Parses a num expression.
    private NumExpression parseNumExpression() throws ParserException, IOException, LexicalErrorException {
        // This is a pretty easy function.
        return new NumExpression(Integer.parseInt((String) match(Token.TokenType.NUM_TOKEN).getTokenData()));
    }





    // Helper functions

    // Returns the next token if it matches the given token type, throws an exception otherwise.
    private Token match(Token.TokenType tokenType) throws ParserException, IOException, LexicalErrorException {
        if(viewNextTokenType() != tokenType) {
            throw new ParserException("Expected " + tokenType.toString() + ", found " + getNextTokenTypeAsString());
        } else {
            return s.getNextToken();
        }
    }

    // Returns the type of the next token (doesn't munch a token)
    private Token.TokenType viewNextTokenType() throws IOException, LexicalErrorException {
        return s.viewNextToken().getTokenType();
    }

    // Gets the name of the next token type as a string (doesn't munch a token)
    private String getNextTokenTypeAsString() throws IOException, LexicalErrorException {
        return viewNextTokenType().toString();
    }
}
