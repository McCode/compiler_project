package scanner;

import java.io.IOException;

public interface Scanner {
	Token getNextToken() throws IOException, LexicalErrorException;
    Token viewNextToken() throws IOException, LexicalErrorException;
}
