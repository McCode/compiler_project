package scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		
		BufferedReader inputFile = null;
		PrintWriter writer = null;
		
		System.out.println("Attempting to open " + args[0] + "...");
		try {
			inputFile = new BufferedReader(new FileReader(new File(args[0])));
			writer = new PrintWriter("tokenList.txt", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: File could not be opened!");
			System.exit(1);
		}
		System.out.println("Opening the file was successful!");
		
		Scanner scanner = null;
		System.out.println("Scanning...");
		try {
			scanner = new CMinusScannerLex(inputFile);

			Token token = scanner.getNextToken();
			while (token.getTokenType() != Token.TokenType.EOF_TOKEN) {
				String tokenType = token.getTokenType().toString();
				// Output when token is an ID
				if (token.getTokenType() == Token.TokenType.ID_TOKEN) {
					String idData = (String)token.getTokenData();
					writer.println(tokenType + " " + idData);
				// Output when token is an NUM
				} else if (token.getTokenType() == Token.TokenType.NUM_TOKEN) {
					String numData = (String)token.getTokenData();
					writer.println(tokenType + " " + numData);
				// Output when token is of any other type
				} else {
					writer.println(tokenType);
				} 
				
				token = scanner.getNextToken();
			}
		} catch (LexicalErrorException|IOException e) {
			//e.printStackTrace();
			System.out.println("ERROR: " + e.getMessage());
			System.exit(1);
		} 
		System.out.println("Scanning complete! You can find your token list in tokenList.txt.");
		writer.close();
		System.exit(0);

	}

}
