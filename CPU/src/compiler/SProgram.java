package compiler;

public class SProgram implements INode {
	private SSymbolTable symbolTable;
	private SHeader header;
	private SCodeSegment codeSegment;
	
	public SProgram() {
		this.symbolTable = new SSymbolTable();
		this.header = new SHeader(symbolTable);
		this.codeSegment = new SCodeSegment(symbolTable);
	}

	@Override
	public String parse(SLex lex) {
		String token = lex.getToken();
		if(token.compareTo(".data") == 0) {
			token = this.header.parse(lex);
		}
		if(token.compareTo(".code") == 0) {
			return this.codeSegment.parse(lex);
		}
		return null;
	}
	
	public SCodeSegment getCodeSegment() {
		return this.codeSegment;
	}
}
