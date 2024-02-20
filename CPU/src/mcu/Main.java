package mcu;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		CPU cpu = new CPU();
		Memory memory = new Memory();
		KeyboardBuffer keyboardBuffer = new KeyboardBuffer();
		MonitorBuffer monitorBuffer = new MonitorBuffer();

		cpu.associate(memory); 
		memory.associate(cpu.mar, cpu.mbr, cpu.cs, cpu.ds, cpu.ss, cpu.hs);
		
		memory.associate(keyboardBuffer, monitorBuffer);
		keyboardBuffer.associate(memory);
		monitorBuffer.associate(memory);
		
		cpu.start();
	}
}
