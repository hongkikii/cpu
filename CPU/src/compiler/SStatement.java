package compiler;

public class SStatement implements INode {
	private String operator;
	private String operand1;
	
	public SStatement(String operator) {
		this.operator = operator;
	}
	
	public SStatement(String operator, String operand1) {
		this.operator = operator;
		this.operand1 = operand1;
	}

	@Override
	public String parse(SLex lex) {
		return operator;
	}
	
	public String toString() {
		String result = operator;
		if(operand1 != null) {
			result += " "  + operand1;
		}
		return result;
	}
}
