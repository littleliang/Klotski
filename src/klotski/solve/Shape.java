package klotski.solve;

import java.io.Serializable;

public enum Shape implements Serializable{
	invalid(0, 0, 0), single(1, 1, 1), horizon(2, 1, 2), vertical(1, 2, 3), square(2, 2, 4);
	
	private int length;
	private int width;
	private int value;
	
	private Shape(int length, int width, int value){
		this.length = length;
		this.width = width;
		this.value = value;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getValue(){
		return value;
	}
}
