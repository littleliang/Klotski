package klotski.solve;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Arrays;

import klotski.util.ConstValue;

public class Layout implements Serializable{
	private int heuristicScore;
	private Node[] nodes = new Node[ConstValue.nodeNum];
	private int[][] mask = new int[ConstValue.rowNum][ConstValue.colNum];
	private int[][] maskReverse = new int[ConstValue.rowNum][ConstValue.colNum];
	private int step;
	
	private int getStep(){
		return step;
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

}
