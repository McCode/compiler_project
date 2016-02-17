package scanner;

%%

%class CMinusScannerLex
%type Token.TokenType

%{
    //private boolean firstTime = true;
    
    public Token getNextToken() throws java.io.IOException {
        Token.TokenType currentToken;
        /*if(firstTime) {
            firstTime = false;
            yyin = source;
            yyout = listing;
        }*/
        
        currentToken = yylex();
        return new Token(currentToken);
    }
%}

digit      = [0-9]
number     = {digit}+
letter     = [a-zA-Z]
identifier = {letter}+
whitespace = [ \t\rn]+
comment    = "/*" [^*] ~"*/" | "/*" "*"+ "/"



%%

"int"        {return Token.TokenType.INT_TOKEN;}
"if"         {return Token.TokenType.IF_TOKEN;}
"return"     {return Token.TokenType.RETURN_TOKEN;}
"else"       {return Token.TokenType.ELSE_TOKEN;}
"void"       {return Token.TokenType.VOID_TOKEN;}
"while"      {return Token.TokenType.WHILE_TOKEN;}

"="          {return Token.TokenType.EQ_TOKEN;}
"!="         {return Token.TokenType.NEQ_TOKEN;}
"<"          {return Token.TokenType.LT_TOKEN;}
">"          {return Token.TokenType.GT_TOKEN;}
"<="         {return Token.TokenType.LTE_TOKEN;}
">="         {return Token.TokenType.GTE_TOKEN;}
"+"          {return Token.TokenType.PLUS_TOKEN;}
"-"          {return Token.TokenType.MINUS_TOKEN;}
"*"          {return Token.TokenType.TIMES_TOKEN;}
"/"          {return Token.TokenType.OVER_TOKEN;}
"("          {return Token.TokenType.LPAREN_TOKEN;}
")"          {return Token.TokenType.RPAREN_TOKEN;}
"["          {return Token.TokenType.LBRACKET_TOKEN;}
"]"          {return Token.TokenType.RBRACKET_TOKEN;}
"{"          {return Token.TokenType.LCURLY_TOKEN;}
"}"          {return Token.TokenType.RCURLY_TOKEN;}
";"          {return Token.TokenType.SEMI_TOKEN;}
","          {return Token.TokenType.COMMA_TOKEN;}
"\0"         {return Token.TokenType.EOF_TOKEN;}

{number}     {return Token.TokenType.NUM_TOKEN;}
{identifier} {return Token.TokenType.ID_TOKEN;}

{whitespace} { /* ignore */ }
{comment}    { /* ignore */ }




























