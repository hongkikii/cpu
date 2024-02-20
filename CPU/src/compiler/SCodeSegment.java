package compiler;

import java.util.Vector;

public class SCodeSegment implements INode {
	// Parse Tree
	private Vector<SStatement> statements;
	private SSymbolTable symbolTable;
	
	public SCodeSegment(SSymbolTable symbolTable) {
		this.statements = new Vector<SStatement>();
		this.symbolTable = symbolTable;
	}

	@Override
	public String parse(SLex lex) {
		String[] tokens = lex.getTokens();
		String operator = tokens[0];
		while(!operator.equals(".end")) {
			if((operator.startsWith("//") || (operator.length() == 0))) {
				//comment, empty line
				//skip
			} else if(operator.contains(":")) {
				//symbol table
				SSymbolEntity entity = new SSymbolEntity();
				entity.setType(operator);
				entity.setName(operator.replace(":", ""));
				entity.setValue(this.statements.size());
				this.symbolTable.add(entity);
				
			} else {
				//parse tree
				SStatement statement = null;
				switch (tokens.length) {
				case 1:
					statement = new SStatement(tokens[0]);
					break;
				case 2:
					statement = new SStatement(tokens[0], tokens[1]);
					break;
				}
				this.statements.add(statement);
			}
			tokens = lex.getTokens();
			operator = tokens[0];
		}
		return operator;
	}
	
	public SSymbolTable getSymbolTable() {
		return this.symbolTable;
	}
	
	public Vector<SStatement> getStatements() {
		return this.statements;
	}
}
