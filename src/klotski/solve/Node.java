package klotski.solve;

import java.io.Serializable;

public class Node implements Serializable{
	private Shape shape;
	private int left;
	private int right;
	private int top;
	private int bottom;
	
	public Node(){
		shape = Shape.invalid;
		left = -1;
		top = -1;
	}
	
	public Node(Shape shape, int left, int top){
		this.shape = shape;
		this.left = left;
		this.top = top;
		this.right = left + shape.getWidth() - 1;
		this.bottom = top + shape.getLength() - 1;
	}
	
	public void moveDown(){
		bottom++;
		top++;
	}
	
	public void moveUp(){
		bottom--;
		top--;
	}
	
	public void moveLeft(){
		left--;
		right--;
	}
	
	public void moveRight(){
		left++;
		right++;
	}
}
