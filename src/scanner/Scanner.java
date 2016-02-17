package scanner;

import java.io.IOException;

public interface Scanner {

	public Token getNextToken () throws LexicalErrorException, IOException;
    public Token viewNextToken ();
}
