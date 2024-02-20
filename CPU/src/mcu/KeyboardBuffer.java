package mcu;

import java.util.Scanner;

public class KeyboardBuffer extends IOBuffer {
	
	public int read() {
		if(isExecuted) {
			Scanner sc = new Scanner(System.in);
			return sc.nextInt();
		}
		return -9999;
	}
}
