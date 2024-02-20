package mcu;

import java.util.LinkedList;

public class IOBuffer extends LinkedList<String> {
	
	boolean isExecuted;
	Memory memory;
	
	public IOBuffer() {
		this.isExecuted = false;
	}
	
	public void setIsExecuted(boolean executeInterrupt) {
		this.isExecuted = executeInterrupt;
	}
	
	public void associate(Memory memory) {
		this.memory = memory;
	}
}
