package klotski.solve;

import java.io.Serializable;

public class Node implements Serializable{
	private static final long serialVersionUID = 7499024249724519228L;
	
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
	
	public Shape getShape(){
		return shape;
	}
	
	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
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
