package scanner;
import java.io.*;

%%


%class CMinusScannerLex
%implements Scanner
%type Token
%yylexthrow LexicalErrorException

%{
    private Token nextToken;

    public Token getNextToken() throws IOException, LexicalErrorException {
        if(nextToken == null) {
            nextToken = scanToken();
        }
        Token returnToken = nextToken;
        if (nextToken.getTokenType() != Token.TokenType.EOF_TOKEN) {
            nextToken = scanToken();
        }
        return returnToken;
    }

    public Token viewNextToken() throws IOException, LexicalErrorException {
        if(nextToken == null) {
            nextToken = scanToken();
        }
        return nextToken;
    }

    private Token scanToken() throws IOException, LexicalErrorException {
        Token returnToken = yylex();
        if(returnToken != null) {
            return returnToken;
        } else {
            return new Token(Token.TokenType.EOF_TOKEN);
        }
    }
%}

digit      = [0-9]
number     = {digit}+
letter     = [a-zA-Z]
identifier = {letter}+
whitespace = [ \t\r\n]+
comment    = "/*" [^*] ~"*/" | "/*" "*"+ "/"



%%

"int"        {return new Token(Token.TokenType.INT_TOKEN);}
"if"         {return new Token(Token.TokenType.IF_TOKEN);}
"return"     {return new Token(Token.TokenType.RETURN_TOKEN);}
"else"       {return new Token(Token.TokenType.ELSE_TOKEN);}
"void"       {return new Token(Token.TokenType.VOID_TOKEN);}
"while"      {return new Token(Token.TokenType.WHILE_TOKEN);}

"="          {return new Token(Token.TokenType.ASSIGN_TOKEN);}
"=="         {return new Token(Token.TokenType.EQ_TOKEN);}
"!="         {return new Token(Token.TokenType.NEQ_TOKEN);}
"<"          {return new Token(Token.TokenType.LT_TOKEN);}
">"          {return new Token(Token.TokenType.GT_TOKEN);}
"<="         {return new Token(Token.TokenType.LTE_TOKEN);}
">="         {return new Token(Token.TokenType.GTE_TOKEN);}
"+"          {return new Token(Token.TokenType.PLUS_TOKEN);}
"-"          {return new Token(Token.TokenType.MINUS_TOKEN);}
"*"          {return new Token(Token.TokenType.TIMES_TOKEN);}
"/"          {return new Token(Token.TokenType.OVER_TOKEN);}
"("          {return new Token(Token.TokenType.LPAREN_TOKEN);}
")"          {return new Token(Token.TokenType.RPAREN_TOKEN);}
"["          {return new Token(Token.TokenType.LBRACKET_TOKEN);}
"]"          {return new Token(Token.TokenType.RBRACKET_TOKEN);}
"{"          {return new Token(Token.TokenType.LCURLY_TOKEN);}
"}"          {return new Token(Token.TokenType.RCURLY_TOKEN);}
";"          {return new Token(Token.TokenType.SEMI_TOKEN);}
","          {return new Token(Token.TokenType.COMMA_TOKEN);}
"\0"         {return new Token(Token.TokenType.EOF_TOKEN);}

{number}     {return new Token(Token.TokenType.NUM_TOKEN, yytext());}
{identifier} {return new Token(Token.TokenType.ID_TOKEN, yytext());}

{whitespace} { /* ignore */ }
{comment}    { /* ignore */ }

{letter}+{digit}+ {throw new LexicalErrorException("Invalid token.");}
{digit}+{letter}+ {throw new LexicalErrorException("Invalid token.");}



























