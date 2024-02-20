package mcu;


public class Page {
	private String[] page;
	private boolean isUsed;
	private int idx;
	
	public Page() {
		page = new String[100];
		isUsed = false;
		idx = 0;
	}
	
	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	public boolean getIsUsed() {
		return this.isUsed;
	}
	
	public String[] getPage() {
		return this.page;
	}
	
	public int getIdx() {
		return this.idx;
	}
	
	public void add(String line) {
		this.page[idx] = line;
		idx++;
	}
	
	public int getValue(int idx) {
		return Integer.parseInt(this.page[idx]);
	}
	
	public void remove(int idx) {
		this.page[idx] = null;
		this.idx = idx;
	}
	
	public void setValue(int address, String value) {
		this.page[address] = value;
	}
}
