//This is the following shape -
//   ____
//  |    |
//  |____|
//  |    |
//  |____|
//  | P  |
//  |____|
//  |    |
//  |____|

//In the above, P denotes the pivot element
//assume that box1 is the one the shorter side (on the pivot's bottom in the diagram),
//box2 is the one closer to the pivot on the longer side (on top of the pivot in the diagram),
//and box3 is the furthest on the longer side (the topmost in the diagram)
package model;

import java.util.ArrayList;

public class Line extends Block { 

	//constructor
	public Line(double pivotX1, double pivotY1, double boxSize) {
		super(pivotX1, pivotY1, boxSize);
		this.side = Facing.BOTTOM; //starts facing bottom by default (the orientation depicted above in the diagram)
		this.type = BlockType.LINE;
	}

	
	//the following three methods return the x and y values of the respective elements to simulate a rotation using the moveAdjacent method
	//assuming that the arraylist always has the order x, y for the top left corner of where we want to place the box in the GUI
	@Override
	public ArrayList<Double> getBox1Coordinates() {
		//if box1 was on the bottom of the pivot, it now moves to the left of the pivot
		//if box1 was on the left of the pivot, it now moves to the bottom of the pivot
		if (side==Facing.BOTTOM||side==Facing.TOP) {
			return moveAdjacent(Facing.BOTTOM, 1);
		}
		else {
			return moveAdjacent(Facing.RIGHT, 1);
		}
	}
	
	@Override
	public ArrayList<Double> getBox2Coordinates() {
		//if box2 was on the top of the pivot, it now moves to the right of the pivot
		//if box2 was on the right of the pivot, it now moves to the top of the pivot
		if (side==Facing.BOTTOM||side==Facing.TOP) {
			return moveAdjacent(Facing.TOP, 1);
		}
		else {
			return moveAdjacent(Facing.LEFT, 1);
		}
	}

	@Override
	public ArrayList<Double> getBox3Coordinates() {
		//if box2 was on the top of the pivot, it now moves to the right of the pivot
		//if box2 was on the right of the pivot, it now moves to the top of the pivot
		if (side==Facing.BOTTOM||side==Facing.TOP) {
			return moveAdjacent(Facing.TOP, 2);
		}
		else {
			return moveAdjacent(Facing.LEFT, 2);
		}
	}
	
	//a line only ever faces left or bottom so override the parent's changeSide method
	@Override
	public void changeSide() {
		if (this.side==Facing.BOTTOM||this.side==Facing.TOP) {
			this.side = Facing.LEFT;
		}
		else if (this.side==Facing.LEFT||this.side==Facing.RIGHT) {
			this.side = Facing.BOTTOM;
		}
	}

	
	//getArr returns the indices of the elements that face the bottom/right/left based on the direction the block is currently facing
	//arr contains the indices for all the elements in the block
	//arr2 contains the indices of the elements facing downwards
	//arr3 contains the indices of the elements facing right
	//arr4 contains the indices of the elements facing left
	@Override
	public ArrayList<int[][]> getArr() {
		int i = (int)(getPivotY1()/getBoxSize());
		int j = (int)(getPivotX1()/getBoxSize());
		ArrayList<int[][]> arrList = new ArrayList<int[][]>();
		if (side==Facing.LEFT||side==Facing.RIGHT) {
			int[][] arr = {{i,j}, {i,j+2}, {i,j+1}, {i,j-1}};
			int[][] arr3 = {{i, j+2}};
			int[][] arr4 = {{i, j-1}};
			arrList.add(arr);
			arrList.add(arr);
			arrList.add(arr3);
			arrList.add(arr4);
		}
		else { //block is facing bottom (or top)
			int[][] arr = {{i,j}, {i-1,j}, {i+1,j}, {i-2,j}};
			int[][] arr2 = {{i+1, j}};
			arrList.add(arr);
			arrList.add(arr2);
			arrList.add(arr);
			arrList.add(arr);
		}
		return arrList;
	}
	
	//getFourDirectionsArr returns the indices of all the elements for all the directions a block could be facing
	//since line only ever faces left or bottom, the returned array has the order bottom-left
	@Override
	public ArrayList<int[][]> getFourDirectionsArr() {
		int i = (int)(getPivotY1()/getBoxSize());
		int j = (int)(getPivotX1()/getBoxSize());
		int[][] bottom = {{i,j}, {i-1,j}, {i+1,j}, {i-2,j}}; //top-bottom
		int[][] left = {{i,j}, {i,j+2}, {i,j+1}, {i,j-1}}; //left-right
		ArrayList<int[][]> arrList = new ArrayList<int[][]>();
		arrList.add(bottom);
		arrList.add(left);
		return arrList;
	}

	//method that gives the x and y coordinates of the pivot when centering in a heightXwidth area
	public int[] getCenterPivotCoordinates(int height, int width) {
		if (side==Facing.LEFT||side==Facing.RIGHT) {
			int[] toReturn = {width/2-20, height/2-10};
			return toReturn;
		}
		else {
			int[] toReturn = {width/2-10, height/2};
			return toReturn;
		}
	}
}
