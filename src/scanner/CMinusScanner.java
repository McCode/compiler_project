package scanner;

import java.io.BufferedReader;
import java.io.IOException;

public class CMinusScanner implements Scanner {
	
	public enum StateType {
		START,
		INASSIGN,
		INCOMMENT,
		INNUM,
		INID,
		DONE,
		SLASH,
		NOTEQUAL,
		LESSTHAN,
		GREATERTHAN,
		COMMENTENDING
	}

	private BufferedReader inFile;
    private Token nextToken;
    String tokenString;
    StringBuilder sb;
    
    static String line;
    static int linepos = 0;
    
    static int bufsize = 0;

    public CMinusScanner (BufferedReader file) throws LexicalErrorException, IOException {
        inFile = file;
        sb = new StringBuilder();
        nextToken = scanToken();
    }
    
    public Token getNextToken () throws LexicalErrorException, IOException {
        Token returnToken = nextToken;
        if (nextToken.getTokenType() != Token.TokenType.EOF_TOKEN)
            nextToken = scanToken();
        return returnToken;
    }
    
    public Token viewNextToken () {
        return nextToken;
    }
    
    private Token scanToken () throws LexicalErrorException, IOException {
    	Token currentToken = null;
    	StateType state = StateType.START;
    	boolean save = false;
    	
    	while (state != StateType.DONE) {
    		char c = '0';
    		try {
    			c = getNextChar();
    		} catch (IOException e) {
    			System.out.print(e);
    		}
    		
    		save = true;
    		
    		switch (state) {
    		case START:
    			if (Character.isDigit(c)) {state = StateType.INNUM;}
    			else if (Character.isAlphabetic(c)) {state = StateType.INID;}
    			else if (c == '=') {state = StateType.INASSIGN;}
    			else if ((c == ' ') || (c == '\t') || (c == '\n')) {
    				save = false;
    			}
    			else if (c == '/') {
    				save = false;
    				state = StateType.SLASH;
    			}
    			else if (c == '!') {
    				state = StateType.NOTEQUAL;
    			}
    			else if (c == '<') {
    				state = StateType.LESSTHAN;
    			}
    			else if (c == '>') {
    				state = StateType.GREATERTHAN;
    			}
    			else if (c == '=') {
    				state = StateType.INASSIGN;
    			}
    			else {
    				state = StateType.DONE;
    				switch (c) {
    				case '\0':
    					save = false;
    					currentToken = new Token(Token.TokenType.EOF_TOKEN);
    					break;
    				case '=':
    					currentToken = new Token(Token.TokenType.EQ_TOKEN);
    					break;
    				case '<':
    					currentToken = new Token(Token.TokenType.LT_TOKEN);
    					break;
    				case '+':
    					currentToken = new Token(Token.TokenType.PLUS_TOKEN);
    					break;
    				case '-':
    					currentToken = new Token(Token.TokenType.MINUS_TOKEN);
    					break;
    				case '*':
    					currentToken = new Token(Token.TokenType.TIMES_TOKEN);
    					break;
    				case '(':
    					currentToken = new Token(Token.TokenType.LPAREN_TOKEN);
    					break;
    				case ')':
    					currentToken = new Token(Token.TokenType.RPAREN_TOKEN);
    					break;
    				case '[':
    					currentToken = new Token(Token.TokenType.LBRACKET_TOKEN);
    					break;
    				case ']':
    					currentToken = new Token(Token.TokenType.RBRACKET_TOKEN);
    					break;
    				case '{':
    					currentToken = new Token(Token.TokenType.LCURLY_TOKEN);
    					break;
    				case '}':
    					currentToken = new Token(Token.TokenType.RCURLY_TOKEN);
    					break;
    				case ',':
    					currentToken = new Token(Token.TokenType.COMMA_TOKEN);
    					break;
    				case ';':
    					currentToken = new Token(Token.TokenType.SEMI_TOKEN);
    					break;
    				default:
    					throw new LexicalErrorException("Invalid token.");
    				}
    			}
    			break;
    		case INCOMMENT:
    			save = false;
    			if (c == '*') {
    				state = StateType.COMMENTENDING;
    			} 
    			break;
    		case INASSIGN:
    			state = StateType.DONE;
    			if (c == '=') {
    				save = true;
    				currentToken = new Token(Token.TokenType.EQ_TOKEN);
    			} else {
    				ungetNextChar();
    				save = false;
    				currentToken = new Token(Token.TokenType.ASSIGN_TOKEN);
    				//throw new LexicalErrorException();
    			}
    			break;
    		case INNUM:
    			if (!Character.isDigit(c)) {
    				ungetNextChar();
    				save = false;
    				state = StateType.DONE;
    				currentToken = new Token(Token.TokenType.NUM_TOKEN, sb.toString());
    			}
    			break;
    		case INID:
    			if (!Character.isAlphabetic(c)) {
    				ungetNextChar();
    				save = false;
    				state = StateType.DONE;
    				currentToken = new Token(Token.TokenType.ID_TOKEN);
    			}
    			break;
    		case SLASH:
				save = false;
				if (c == '*') {
    				state = StateType.INCOMMENT;
    			} else {
    				ungetNextChar();
    				state = StateType.DONE;
    				currentToken = new Token(Token.TokenType.OVER_TOKEN);
    			}
    			break;
    		case COMMENTENDING:
    			save = false;
    			if (c == '/') {
    				state = StateType.START;
    			} else if (c != '*') {
    				state = StateType.INCOMMENT;
    			}
    			break;
    		case NOTEQUAL:
				state = StateType.DONE;
    			if (c == '=') {
    				save = true;
    				currentToken = new Token(Token.TokenType.NEQ_TOKEN);
    			} else {
    				save = false;
    				throw new LexicalErrorException("Invalid token.");
    			}
    			break;
    		case GREATERTHAN:
				state = StateType.DONE;
    			if (c == '=') {
    				save = true;
    				currentToken = new Token(Token.TokenType.GTE_TOKEN);
    			} else {
    				save = false;
    				currentToken = new Token(Token.TokenType.GT_TOKEN);
    			}
    			break;
    		case LESSTHAN:
				state = StateType.DONE;
    			if (c == '=') {
    				save = true;
    				currentToken = new Token(Token.TokenType.LTE_TOKEN);
    			} else {
    				save = false;
    				currentToken = new Token(Token.TokenType.LT_TOKEN);
    			}
    			break;
    		case DONE:
    		default: 
    			System.out.println("Something didn't go well!");
    			state = StateType.DONE;
    			throw new LexicalErrorException("Invalid token.");
			}
    		
    		if (save == true) {
    			sb.append(c);
    		}
    		if (state == StateType.DONE) {
    			tokenString = sb.toString();
    			if (currentToken.getTokenType() == Token.TokenType.ID_TOKEN) {
    				currentToken = reservedLookup(tokenString);
    			}
    		}
		}
    	
    	sb.delete(0, sb.length());
    	return currentToken;
    }
    
    private char getNextChar() throws IOException{
    	if (!(linepos < bufsize)) {
    		if ((line = inFile.readLine()) != null) {
    			System.out.println("Working on the following line: " + line);
    			bufsize = line.length();
    			linepos = 0;
    			if (bufsize > 0) {
        			return line.charAt(linepos++);
    			} else {
    				return '\n';
    			}
    		} else {
	    		// EOF character
	    		return '\0';
	    	}
    	} else {
    		return line.charAt(linepos++);
    	}
    }
    
    private void ungetNextChar() {
    	linepos--;
    }
    
    private Token reservedLookup(String s) {
    	switch (s) {
    	case "if": 
    		return new Token(Token.TokenType.IF_TOKEN);
    	case "else": 
    		return new Token(Token.TokenType.ELSE_TOKEN);
    	case "int": 
    		return new Token(Token.TokenType.INT_TOKEN);
    	case "while":
			return new Token(Token.TokenType.WHILE_TOKEN);
    	case "void":
			return new Token(Token.TokenType.VOID_TOKEN);
    	case "return":
			return new Token(Token.TokenType.RETURN_TOKEN);
       	default: 
    		return new Token(Token.TokenType.ID_TOKEN, s);
    	}
    }
}
