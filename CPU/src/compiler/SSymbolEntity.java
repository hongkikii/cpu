package compiler;

public class SSymbolEntity {
	private String type;
	private String name;
	private int value;
	
	public String getType() { return type;}
	public void setType(String name) {
		if(name.startsWith("@")) {
			this.type = "Int";
		}
		else if(name.startsWith("L")) {
			this.type = "Label";
		}
	}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getValue() {return value;}
	public void setValue(int value) {this.value = value;}
	
	public String toString() {
		return this.name + " " + this.value + " " + this.type;
	}
}
