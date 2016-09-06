package klotski.solve;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

import klotski.util.ConstValue;

public class Layout implements Serializable, Comparable<Layout>{
	private static final long serialVersionUID = -9113466678034978593L;
	
	private int heuristicScore;
	private Node[] nodes = new Node[ConstValue.nodeNum];
	private int[][] mask;
	private int[][] maskReverse;
	public int step;
	transient private Layout beforeLayout = null;
	public void setBeforeLayout(Layout before){
		beforeLayout = before;
	}
	
	public Layout getBeforeLayout(){
		return beforeLayout;
	}
	
	public Layout(int step){
		this.step = step;
	}
	
	public void move(PriorityQueue<Layout> layoutQueue, HashMap<Layout, Integer> layoutVisitedSet) throws OptionalDataException, ClassNotFoundException, IOException{
		for(int i = 0; i < nodes.length; i++){
			if(nodes[i].getTop() > 0 && mask[nodes[i].getTop() - 1][nodes[i].getLeft()] == 0
					&& mask[nodes[i].getTop() - 1][nodes[i].getRight()] == 0){
				Layout next = this.deepClone();
				next.setBeforeLayout(this);
				next.step++;
				next.nodes[i].moveUp();
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.containsKey(next)){
					layoutVisitedSet.put(next, next.step);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getBottom() < ConstValue.rowNum - 1 && mask[nodes[i].getBottom() + 1][nodes[i].getLeft()] == 0
					&& mask[nodes[i].getBottom() + 1][nodes[i].getRight()] == 0){
				Layout next = this.deepClone();
				next.setBeforeLayout(this);
				next.step++;
				next.nodes[i].moveDown();
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.containsKey(next)){
					layoutVisitedSet.put(next, next.step);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getLeft() > 0 && mask[nodes[i].getTop()][nodes[i].getLeft() - 1] == 0
					&& mask[nodes[i].getBottom()][nodes[i].getLeft() - 1] == 0){
				Layout next = this.deepClone();
				next.setBeforeLayout(this);
				next.step++;
				next.nodes[i].moveLeft();
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.containsKey(next)){
					layoutVisitedSet.put(next, next.step);
					layoutQueue.add(next);
				}
			}
			
			if(nodes[i].getRight() < ConstValue.colNum - 1 && mask[nodes[i].getTop()][nodes[i].getRight() + 1] == 0
					&& mask[nodes[i].getBottom()][nodes[i].getRight() + 1] == 0){
				Layout next = this.deepClone();
				next.setBeforeLayout(this);
				next.step++;
				next.nodes[i].moveRight();
				next.toMask();
				next.calHeuristicScore();
				if(!layoutVisitedSet.containsKey(next)){
					layoutVisitedSet.put(next, next.step);
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
	
	public void printMask(){
		for(int i = 0; i < ConstValue.rowNum; i++){
			for(int j = 0; j < ConstValue.colNum; j++){
				System.out.print(mask[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
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
		if(nodes[0].getTop() == 0){
			if(nodes[1].getTop() == 2){
				heuristicScore = step + 80;
				return;
			}
			else if(nodes[1].getTop() == 3){
				heuristicScore = step + 70;
				return;
			}
			else if(nodes[1].getTop() == 5){
				heuristicScore = step + 40;
				return;
			}
		}
		else if(nodes[0].getTop() == 1){
			heuristicScore = step + 30;
			return;
		}
		else if(nodes[0].getTop() == 2){
			int sum = 0;
			for(int i = 2; i <= 5; i++){
				sum += nodes[i].getTop() * ConstValue.colNum + nodes[i].getLeft();
			}
			if(sum <= 37){
				heuristicScore = step + 20;
				return;
			}
			else{
				heuristicScore = step;
				return;
			}
		}
		else if(nodes[0].getTop() == 3){
			heuristicScore = step;
			return;
		}
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
		result = prime * result + Arrays.deepHashCode(mask) + Arrays.deepHashCode(maskReverse);
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
		if (!Arrays.deepEquals(mask, other.mask) && !Arrays.deepEquals(mask, other.maskReverse))
			return false;
		return true;
	}

	@Override
	public int compareTo(Layout o) {
		if (heuristicScore < o.heuristicScore){
			return -1;
		}
		else if(heuristicScore > o.heuristicScore){
			return 1;
		}
		else{
			if (step > o.step){
				return -1;
			}
			else if (step < o.step){
				return 1;
			}
		}
		return 0;
	}

}
