package compiler;

import java.io.FileWriter;
import java.io.IOException;

public class SParser {
	private SProgram program;
	
	public void parse(SLex lex) throws IOException {
		program = new SProgram();
		program.parse(lex);

		FileWriter symbolTableWriter = new FileWriter("reference/symbolTable", true);
		System.out.println("<------Symbol Table------>");
		for(SSymbolEntity entity : getProgram().getCodeSegment().getSymbolTable()) {
			System.out.println(entity.toString());
			symbolTableWriter.write(entity.toString() + "\n");
		}
		symbolTableWriter.close();
		
		System.out.println();
		
		FileWriter parseTreeWriter = new FileWriter("reference/parseTree", false);
		System.out.println("<------Parse Tree------>");
		for(SStatement statement : getProgram().getCodeSegment().getStatements()) {
			System.out.println(statement.toString());
			parseTreeWriter.write(statement.toString() + "\n");
		}
		parseTreeWriter.close();
	}
	
	public SProgram getProgram() {
		return this.program;
	}
}
