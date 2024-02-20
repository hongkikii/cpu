package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class SCodeGenerator {
	private FileWriter fileWriter;
	private Scanner scanner;
	private Map<String, String> symbolTable;
	private Map<String, String> instructions;
	
	public SCodeGenerator() {
	}

	public void initialize() throws IOException {
		fileWriter = new FileWriter("code/objectCode", false);
		symbolTable = new HashMap<>();
		instructions = new HashMap<>();
	}

	public void translate() throws IOException {
		setReference();
		
		try {
			scanner = new Scanner(new File("reference/parseTree"));
			
			while(scanner.hasNext()) {
				String[] lineArr = scanner.nextLine().split(" ");
				mappingOperator(lineArr[0]);
				
				if(lineArr.length == 2) {
					mappingOperand(lineArr[1]);
				}
				
				fileWriter.write("\n");
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void finalize() throws IOException {
		fileWriter.close();
		scanner.close();
	}
	
	private void setReference() {
		fileToMap("instructionSetDesign", instructions);
		fileToMap("symbolTable", symbolTable);
	}
	
	private void fileToMap(String fileName, Map<String, String> map) {
		try {
			scanner = new Scanner(new File("reference/" + fileName));
			while(scanner.hasNext()) {
				String[] lineArr = scanner.nextLine().split(" ");
				map.put(lineArr[1], lineArr[0]);
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void mappingOperator(String operator) throws IOException {
		for(Entry<String, String> entry : instructions.entrySet()) {
			if(entry.getValue().equals(operator)) {
				fileWriter.write(entry.getKey());
				return;
			}
		}
	}
	
	private void mappingOperand(String operand) throws IOException {
		if(operand.startsWith("@")) {
			fileWriter.write(" 11" + transNum(operand));
		}
		else if(operand.startsWith("$")) {
			if(operand.contains("+")) {
				int idx = operand.indexOf('+');
				fileWriter.write(" 22" + transNum(operand.substring(0, idx)) + operand.substring(idx+1));
			}
			else {
				fileWriter.write(" 22" + transNum(operand));
			}
		}
		else if(operand.startsWith("#")) {
			fileWriter.write(" 44" + operand.substring(1));
		}
		else if(operand.startsWith("L")) {
			fileWriter.write(" " + transNum(operand));
		}
		else {
			fileWriter.write(" " + operand);
		}
	}
	
	private String transNum(String operand) {
		String operandToNum = null;
		for(Entry<String, String> entry : symbolTable.entrySet()) {
			if(entry.getValue().equals(operand)) {
				operandToNum = entry.getKey();
			}
		}
		return operandToNum;
	}
}
