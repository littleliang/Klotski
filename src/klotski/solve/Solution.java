package klotski.solve;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Solution {

	public static void main(String[] args) throws OptionalDataException, ClassNotFoundException, IOException {
		long start =System.currentTimeMillis();
		PriorityQueue<Layout> layoutQueue = new PriorityQueue<>();
		HashSet<Layout> layoutVisitedSet = new HashSet<>();
		Layout layout = new Layout(0);
		layout.insertNode(new Node(Shape.square, 1, 0), 0);
		layout.insertNode(new Node(Shape.vertical, 1, 2), 1);
		layout.insertNode(new Node(Shape.horizon, 0, 0), 2);
		layout.insertNode(new Node(Shape.horizon, 3, 0), 3);
		layout.insertNode(new Node(Shape.horizon, 0, 2), 4);
		layout.insertNode(new Node(Shape.horizon, 3, 2), 5);
		layout.insertNode(new Node(Shape.single, 1, 3), 6);
		layout.insertNode(new Node(Shape.single, 2, 3), 7);
		layout.insertNode(new Node(Shape.single, 0, 4), 8);
		layout.insertNode(new Node(Shape.single, 3, 4), 9);
		
		layout.toMask();
		layout.calHeuristicScore();
		layoutQueue.add(layout);
		layoutVisitedSet.add(layout);
		Layout curLayout;
		
		while (!layoutQueue.isEmpty()) {
			curLayout = layoutQueue.poll();
			if (curLayout.isSolved()) {
				System.out.println("found solution with " + curLayout.step  +" steps");
				break;
			}
			curLayout.move(layoutQueue, layoutVisitedSet);
		}
		long end =System.currentTimeMillis();
		System.out.println(end - start);
	}
}
