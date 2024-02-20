package mcu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;

import mcu.CPU.Register;

public class Memory {
	Page[] storage;
	Map<Integer, SegmentInfo> segmentTable;
	Map<Integer, Integer> pageTable;
	
	private Register mar, mbr;
	private Register cs, ds, ss, hs;
	private KeyboardBuffer keyboardBuffer;
	private MonitorBuffer monitorBuffer;
	public int IOBuffer;
	private int logicalNumber;
	
	public Memory() {
		this.storage = new Page[100];
		this.segmentTable = new HashMap<>();
		this.pageTable = new HashMap<>();
		this.logicalNumber = 10;
		for(int i=0; i<5; i++) {
			this.storage[i].setIsUsed(true);
		}
		
		int count = 0;
		
		// Header Setting (OS)
		for(int i=0; i<100; i++) {
			if(count == 5) {
				break;
			}
			if(!(this.storage[i].getIsUsed())) {
				if(count == 0) {
					this.cs.setValue(logicalNumber);
				}
				else if(count == 1) {
					this.ds.setValue(logicalNumber);
				}
				else if(count == 2) {
					this.ss.setValue(logicalNumber);
				}
				else if(count == 3) {
					this.hs.setValue(logicalNumber);
				}
				else if(count == 4) {
					IOBuffer = logicalNumber;
				}
				
				this.pageTable.put(logicalNumber, i);
				logicalNumber++;
				this.storage[i].setIsUsed(true);
				count++;
			}
		}
		
		initSegmentTable();
		
		try {
			Scanner sc = new Scanner(new File("code/objectCode"));
			while(sc.hasNext()) {		
				String line = sc.nextLine();
				line = line.replaceAll(" ", "");
				this.storage[cs.getValue()].add(line);
			}
			sc.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initSegmentTable() {
		this.segmentTable.put(5, new SegmentInfo(100, this.storage[5]));
		this.segmentTable.put(6, new SegmentInfo(100, this.storage[6]));
		this.segmentTable.put(7, new SegmentInfo(100, this.storage[7]));
		this.segmentTable.put(8, new SegmentInfo(100, this.storage[8]));
		this.segmentTable.put(9, new SegmentInfo(100, this.storage[9]));
	}
		
	public void associate (Register mar, Register mbr, Register cs, Register ds, Register ss, Register hs) {
		this.mar = mar;
		this.mbr = mbr;
		this.cs = cs;
		this.ds = ds;
		this.ss = ss;
		this.hs = hs;
	}
	
	public void associate (KeyboardBuffer keyboardBuffer, MonitorBuffer monitorBuffer) {
		this.keyboardBuffer = keyboardBuffer;
		this.monitorBuffer = monitorBuffer;
	}
	
	public void load(Register sr) {
		int physicalPageNumber;
		
		if(sr == null) {
			physicalPageNumber = IOBuffer;
		}
		else {
			physicalPageNumber = pageTableCheck(sr);
		}
		
		try {
			if(segmentTableCheck(physicalPageNumber)) {
				mbr.setValue(this.storage[physicalPageNumber].getValue(mar.getValue()));
				return;
			}
			else {
				throw new SegmentFaultException("segment fault exception ! !");
			}
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
	}
	
	public void load(int logicalPageNumber, int startAddress, int idx) {
		int physicalPageNumber = pageTableCheck(logicalPageNumber);
		
		try {
			if(segmentTableCheck(physicalPageNumber)) {
				mbr.setValue(this.storage[physicalPageNumber].getValue(startAddress+idx));
				return;
			}
			else {
				throw new SegmentFaultException("segment fault exception ! !");
			}
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
	}

	public void store(Register sr) {
		int physicalPageNumber;
		
		if(sr == null) {
			physicalPageNumber = IOBuffer;
		}
		else {
			physicalPageNumber = pageTableCheck(sr);
		}
		
		try {
			if(segmentTableCheck(physicalPageNumber)) {
				int address = mar.getValue();
				int value = mbr.getValue();
				this.storage[physicalPageNumber].setValue(address, Integer.toString(value));
			}
			else {
				throw new SegmentFaultException("segment fault exception ! !");
			}
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
	}
	
	public int pop(int sp, int size) { 
		int physicalPageNumber = pageTableCheck(this.ss);
		
		for(int i=sp; i>sp-size; i--) {
			this.storage[physicalPageNumber].remove(i);
		}
		return sp-size;
	}
	
	public int allocate(int hp, Register saveSr, int address) {
		int physicalPageNumber = pageTableCheck(this.hs);
		
		try {
			if(hp >= 100) {
				throw new SegmentFaultException("segment fault exception ! !");
			}
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
		
		this.storage[physicalPageNumber].add("60");
		this.storage[physicalPageNumber].add("70");
		
		physicalPageNumber = pageTableCheck(saveSr);
		
		try {
			if(segmentTableCheck(physicalPageNumber)) {
				this.storage[physicalPageNumber].setValue(address, Integer.toString(this.hs.getValue()+hp));
			}
			else {
				throw new SegmentFaultException("segment fault exception ! !");
			}
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
		
		return this.storage[this.hs.getValue()].getIdx();
	}

	public void IOInterrupt(int interruptNumber) {
		// monitor write
		if(interruptNumber == 8) {
			monitorBuffer.setIsExecuted(true);
			monitorBuffer.write(this.storage[IOBuffer].getValue(0));
			monitorBuffer.setIsExecuted(false);
		}
		// keyboard read
		else if(interruptNumber == 9) {
			keyboardBuffer.setIsExecuted(true);
			this.storage[IOBuffer].setValue(0, Integer.toString(keyboardBuffer.read()));
			keyboardBuffer.setIsExecuted(false);
		}
	}
	
	private int pageTableCheck(Register sr) {	
		try {
			for(Entry<Integer, Integer> entry : pageTable.entrySet()) {
				if(entry.getKey() == sr.getValue()) {
					return entry.getValue();
				}
			}
			throw new SegmentFaultException("segment fault exception ! !");
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
		return -9999;
	}
	
	private int pageTableCheck(int logicalPageNumber) {	
		try {
			for(Entry<Integer, Integer> entry : pageTable.entrySet()) {
				if(entry.getKey() == logicalPageNumber) {
					return entry.getValue();
				}
			}
			throw new SegmentFaultException("segment fault exception ! !");
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
		return -9999;
	}
	
	private boolean segmentTableCheck(int physicalPageNumber) {
		try {
			for(Entry<Integer, SegmentInfo> entry : segmentTable.entrySet()) {
				if(physicalPageNumber == entry.getKey()) {
					if(mar.getValue() <= entry.getValue().getLimit()) 
						return true;
					return false;
				}
			}
			throw new SegmentFaultException("segment fault exception ! !");
		}
		catch (SegmentFaultException e) {
			e.getStackTrace();
		}
		return false;
	}
}
