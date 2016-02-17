package scanner;

import java.io.IOException;

public interface Scanner {
	Token getNextToken() throws LexicalErrorException, IOException;
    Token viewNextToken();
}
