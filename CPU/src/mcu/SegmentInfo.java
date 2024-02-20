package mcu;

public class SegmentInfo {
	private int limit;
	private Page base;
	
	public SegmentInfo(int limit, Page base) {
		this.limit = limit;
		this.base = base;
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Page getBase() {
		return base;
	}
	public void setBase(Page base) {
		this.base = base;
	}
}
