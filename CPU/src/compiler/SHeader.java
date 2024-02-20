package compiler;

public class SHeader implements INode {
	private SSymbolTable symbolTable;
	
	public SHeader(SSymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	
	@Override
	public String parse(SLex lex) {
		String token = lex.getToken();
		while(!token.equals(".code")) {
			SSymbolEntity entity = new SSymbolEntity();
			entity.setType(token);
			entity.setName(token);
			entity.setValue(Integer.parseInt(lex.getToken()));
			this.symbolTable.add(entity);
			token = lex.getToken();
		}
		return token;
	}
	
	public SSymbolTable getSymbolTable() {
		return this.symbolTable;
	}
}
