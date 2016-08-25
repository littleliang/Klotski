package klotski.solve;

public enum Shape {
	invalid(0, 0), single(1, 1), horizon(2, 1), vertical(1, 2), square(2, 2);
	
	private int length;
	private int width;
	
	private Shape(int length, int width){
		this.length = length;
		this.width = width;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getWidth(){
		return width;
	}
}
