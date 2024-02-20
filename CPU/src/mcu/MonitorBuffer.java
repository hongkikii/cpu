package mcu;

public class MonitorBuffer extends IOBuffer {

	public void write(int output) {
		if(isExecuted) {
			System.out.print(output);
		}
	}
}
