package scanner;

public class Token {

	public enum TokenType {
        EQ_TOKEN,
        NEQ_TOKEN,
        LT_TOKEN,
        GT_TOKEN,
        LTE_TOKEN,
        GTE_TOKEN,
        PLUS_TOKEN,
        MINUS_TOKEN,
        TIMES_TOKEN,
        OVER_TOKEN,
        ASSIGN_TOKEN,
        NUM_TOKEN,
        ID_TOKEN,
        LPAREN_TOKEN,
        RPAREN_TOKEN,
        LBRACKET_TOKEN,
        RBRACKET_TOKEN,
        LCURLY_TOKEN,
        RCURLY_TOKEN,
        SEMI_TOKEN,
        COMMA_TOKEN,
        EOF_TOKEN,
        
        // Reserved Words
        INT_TOKEN,
        IF_TOKEN,
        RETURN_TOKEN,
        ELSE_TOKEN,
        VOID_TOKEN,
        WHILE_TOKEN
    }
	
	private TokenType tokenType;
    private Object tokenData;

    public Token (TokenType type) {
        this (type, null);
    }

    public Token (TokenType type, Object data) {
        tokenType = type;
        tokenData = data;
    }

    public TokenType getTokenType () {
    	return tokenType;
    }
    
    public Object getTokenData () {
    	return tokenData;
    }
    
    public void setTokenData (Object data) {
    	tokenData = data;
    }

}
