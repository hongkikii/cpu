package compiler;

import java.io.IOException;

public class SMain {
	
	private SLex lex;
	private SParser parser;
	private SCodeGenerator codeGenerator;
	
	public SMain() {
	}
	
	public static void main(String[] args) throws IOException {
		SMain main = new SMain();
		main.initialize();
		main.run();
		main.finalize();
	}
	
	public void initialize() throws IOException {
		lex = new SLex();
		lex.initialize("code/assemblyCode");
		
		parser = new SParser();
		
		codeGenerator = new SCodeGenerator();
		codeGenerator.initialize();
	}
	
	public void run() throws IOException {
		parser.parse(this.lex);		
		codeGenerator.translate();
	}
	
	public void finalize() throws IOException {
		lex.finalize();
		codeGenerator.finalize();
	}
}
