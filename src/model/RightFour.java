//This is the following shape -
//        ____ ____
//       | P  |    |
//   ____|____|____|
//  |    |    |
//  |____|____|

//In the above diagram, P denotes the pivot element
//assume that box1 is the one on the pivot's closest (the top right in the above diagram),
//box2 on the on the longer side but closer (bottom right in the above diagram),
//box3 on the longer side but further (bottom left in the above diagram)
package model;

import java.util.ArrayList;

public class RightFour extends Block { 
	
	//constructor
	public RightFour(double pivotX1, double pivotY1, double boxSize) {
		super(pivotX1, pivotY1, boxSize);
		this.side = Facing.RIGHT; //faces right by default (the orientation depicted in the above diagram)
		this.type = BlockType.RIGHT_FOUR;
	}

	
	//the following three methods return the x and y values of the respective elements to simulate a rotation using the moveAdjacent method
	//assuming that the arraylist always has the order x, y for the top left corner of where we want to place the box in the GUI
	@Override
	public ArrayList<Double> getBox1Coordinates() {
		//if box1 was on the bottom of the pivot, it now moves to the left of the pivot
		//if box1 was on the left of the pivot, it now moves to the top of the pivot
		//if box1 was on the top of the pivot, it now moves to the right of the pivot
		//if box1 was on the right of the pivot, it now moves to the bottom of the pivot
		//so box1 is always on the side that the Block is facing
		return moveAdjacent(side, 1);
	}
	
	@Override
	public ArrayList<Double> getBox2Coordinates() {
		if (this.side==Facing.BOTTOM) {
			//box2 was on the left of the pivot, now moves to the top of the pivot
			return moveAdjacent(Facing.LEFT, 1);
		}
		else if (this.side==Facing.LEFT) {
			//box2 was on the top of the pivot, now moves to the right of the pivot
			return moveAdjacent(Facing.TOP, 1);
		}
		else if (this.side==Facing.TOP) {
			//box2 was on the right of the pivot, now moves to the bottom of the pivot
			return moveAdjacent(Facing.RIGHT, 1);
		}
		else { //block is facing right
			//box2 was on the bottom of the pivot, now moves to the left of the pivot
			return moveAdjacent(Facing.BOTTOM, 1);
		}
	}

	@Override
	public ArrayList<Double> getBox3Coordinates() {
		if (this.side==Facing.BOTTOM) {
			//box3 was on the left of the pivot, now moves to the top of the pivot
			return moveFurther(Facing.LEFT, 1);
		}
		else if (this.side==Facing.LEFT) {
			//box3 was on the top of the pivot, now moves to the right of the pivot
			return moveFurther(Facing.TOP, 1);
		}
		else if (this.side==Facing.TOP) {
			//box3 was on the right of the pivot, now moves to the bottom of the pivot
			return moveFurther(Facing.RIGHT, 1);
		}
		else { //block is facing right
			//box3 was on the bottom of the pivot, now moves to the left of the pivot
			return moveFurther(Facing.BOTTOM, 1);
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
		if (side==Facing.TOP) {
			int[][] arr = {{i,j}, {i-1,j}, {i,j+1}, {i+1,j+1}};
			int[][] arr2 = {{i,j}, {i+1,j+1}};
			int[][] arr3 = {{i,j+1},{i+1,j+1}};
			int[][] arr4 = {{i-1,j}, {i,j}};
			arrList.add(arr);
			arrList.add(arr2);
			arrList.add(arr3);
			arrList.add(arr4);
		}
		else if (side==Facing.BOTTOM){
			int[][] arr = {{i,j}, {i+1,j}, {i,j-1}, {i-1,j-1}};
			int[][] arr2 = {{i+1,j}, {i,j-1}};
			int[][] arr3 = {{i,j}, {i+1, j}};
			int[][] arr4 = {{i-1,j-1}, {i,j-1}};
			arrList.add(arr);
			arrList.add(arr2);
			arrList.add(arr3);
			arrList.add(arr4);
		}
		else if (side==Facing.LEFT){
			int[][] arr = {{i,j}, {i,j-1}, {i-1,j}, {i-1,j+1}};
			int[][] arr2 = {{i,j}, {i,j-1}, {i-1,j+1}};
			int[][] arr3 = {{i-1,j+1}};
			int[][] arr4 = {{i,j-1}};
			arrList.add(arr);
			arrList.add(arr2);
			arrList.add(arr3);
			arrList.add(arr4);
		}
		else { //block is facing right
			int[][] arr = {{i,j}, {i,j+1}, {i+1,j}, {i+1,j-1}};
			int[][] arr2 = {{i,j+1}, {i+1,j}, {i+1,j-1}};
			int[][] arr3 = {{i,j+1}};
			int[][] arr4 = {{i+1,j-1}};
			arrList.add(arr);
			arrList.add(arr2);
			arrList.add(arr3);
			arrList.add(arr4);
		}
		return arrList;
	}
	
	//getFourDirectionsArr returns the indices of all the elements for all the directions a block could be facing
	@Override
	public ArrayList<int[][]> getFourDirectionsArr() {
		int i = (int)(getPivotY1()/getBoxSize());
		int j = (int)(getPivotX1()/getBoxSize());
		int[][] top = {{i,j}, {i-1,j}, {i,j+1}, {i+1,j+1}}; //top
		int[][] bottom = {{i,j}, {i+1,j}, {i,j-1}, {i-1,j-1}}; //bottom
		int[][] left = {{i,j}, {i,j-1}, {i-1,j}, {i-1,j+1}}; //left
		int[][] right = {{i,j}, {i,j+1}, {i+1,j}, {i+1,j-1}}; //right
		ArrayList<int[][]> arrList = new ArrayList<int[][]>();
		arrList.add(top);
		arrList.add(bottom);
		arrList.add(left);
		arrList.add(right);
		return arrList;
	}

	//method that gives the x and y coordinates of the pivot when centering in a heightXwidth area
	public int[] getCenterPivotCoordinates(int height, int width) {
		if (side==Facing.BOTTOM) {
			int[] toReturn = {width/2, height/2-10};
			return toReturn;
		}
		else if (side==Facing.LEFT) {
			int[] toReturn = {width/2-10, height/2};
			return toReturn;
		}
		else if (side==Facing.TOP) {
			int[] toReturn = {width/2-20, height/2-10};
			return toReturn;
		}
		else { //facing right
			int[] toReturn = {width/2-10, height/2-20};
			return toReturn;
		}
	}
}
