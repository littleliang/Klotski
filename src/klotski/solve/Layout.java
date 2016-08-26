package klotski.solve;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

import klotski.util.ConstValue;

public class Layout implements Serializable, Comparable<Layout>{
	private static final long serialVersionUID = -9113466678034978593L;
	
	private int heuristicScore;
	private Node[] nodes = new Node[ConstValue.nodeNum];
	private int[][] mask;
	private int[][] maskReverse;
	public int step;
	
	
	public Layout(int step){
		this.step = step;
	}
	
	public void move(PriorityQueue<Layout> layoutQueue, HashSet<Layout> layoutVisitedSet) throws OptionalDataException, ClassNotFoundException, IOException{
		for(int i = 0; i < nodes.length; i++){
			if(nodes[i].getTop() > 0 && mask[nodes[i].getTop() - 1][nodes[i].getLeft()] == 0
					&& mask[nodes[i].getTop() - 1][nodes[i].getRight()] == 0){
				Layout next = deepClone();
				next.nodes[i].moveUp();
				next.step++;
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.contains(next)){
					layoutVisitedSet.add(next);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getBottom() < ConstValue.rowNum - 1 && mask[nodes[i].getBottom() + 1][nodes[i].getLeft()] == 0
					&& mask[nodes[i].getBottom() + 1][nodes[i].getRight()] == 0){
				Layout next = deepClone();
				next.nodes[i].moveDown();
				next.step++;
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.contains(next)){
					layoutVisitedSet.add(next);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getLeft() > 0 && mask[nodes[i].getTop()][nodes[i].getLeft() - 1] == 0
					&& mask[nodes[i].getBottom()][nodes[i].getLeft() - 1] == 0){
				Layout next = deepClone();
				next.nodes[i].moveLeft();
				next.step++;
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.contains(next)){
					layoutVisitedSet.add(next);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getLeft() > 0 && mask[nodes[i].getTop()][nodes[i].getRight() + 1] == 0
					&& mask[nodes[i].getBottom()][nodes[i].getRight() + 1] == 0){
				Layout next = deepClone();
				next.nodes[i].moveRight();
				next.step++;
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.contains(next)){
					layoutVisitedSet.add(next);
					layoutQueue.add(next);
				}
			}
		}
	}
	
	public void toMask(){
		mask = new int[ConstValue.rowNum][ConstValue.colNum];
		maskReverse = new int[ConstValue.rowNum][ConstValue.colNum];
		for(int i = 0; i < nodes.length; i++){
			for(int x = nodes[i].getTop(); x <= nodes[i].getBottom(); x++){
				for(int y = nodes[i].getLeft(); y <= nodes[i].getRight(); y++){
					mask[x][y] = nodes[i].getShape().getValue();
					maskReverse[x][ConstValue.colNum - y - 1] = nodes[i].getShape().getValue();
				}
			}
		}
	}
	
	public boolean insertNode(Node node, int index){
		if(index == 0 && node.getShape() == Shape.square){
			nodes[index] = node;
			return true;
		}
		else if(index == 1 && node.getShape() == Shape.vertical){
			nodes[index] = node;
			return true;
		}
		else if(index >= 2 && index <= 5 && node.getShape() == Shape.horizon){
			nodes[index] = node;
			return true;
		}
		else if(index >= 6 && index < ConstValue.nodeNum  && node.getShape() == Shape.single){
			nodes[index] = node;
			return true;
		}
		return false;
	}
	
	
	public void calHeuristicScore(){
		heuristicScore = 1;
	}
	
	public boolean isSolved()
	{
		return (nodes[0].getLeft() == 1 && nodes[0].getTop() == 3);
	}
	
	public Layout deepClone() throws IOException, OptionalDataException, ClassNotFoundException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(this);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		ObjectInputStream oi = new ObjectInputStream(bi);
		return (Layout) (oi.readObject());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(mask);
		result = prime * result + Arrays.deepHashCode(maskReverse);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Layout other = (Layout) obj;
		if (!Arrays.deepEquals(mask, other.mask))
			return false;
		if (!Arrays.deepEquals(mask, other.maskReverse))
			return false;
		return true;
	}

	@Override
	public int compareTo(Layout o) {
		if (heuristicScore > o.heuristicScore){
			return -1;
		}
		else if(heuristicScore < o.heuristicScore){
			return 1;
		}
		return 0;
	}

}
