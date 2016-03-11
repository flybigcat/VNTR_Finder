package vntr_search;

public class VNTR {
	public int st;
	public int count;
	public int length;
	
	public VNTR(int st, int count, int length) {
		this.st = st;
		this.count = count;
		this.length = length;
	}
	
	public String toString() {
		return "length = " + length + ", count =  " + count + ", string position = " + st;
	}
}
