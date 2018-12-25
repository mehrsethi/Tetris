//This is the following shape -
//   ____ ____
//  |    |  P |
//  |____|____|
//  |    |    |
//  |____|____|

//In the above diagram, P denotes the pivot element
//assume that box1 is the one on the pivot's left, box2 on the bottom, box3 on the left bottom

package model;

import java.util.ArrayList;

public class Box extends Block {
	
	//constructor
	public Box(double pivotX1, double pivotY1, double boxSize) {
		super(pivotX1, pivotY1, boxSize);
		this.side = Facing.BOTTOM; //faces downwards by default
		this.type = BlockType.BOX;
	}
	
	
	//the following three methods return the x and y values of the respective elements to simulate a rotation
	//since this is a box, rotation does not affect any of the elements
	//the arraylist always has the order x, y for the top left corner of where we want to place the box in the GUI
	@Override
	public ArrayList<Double> getBox1Coordinates() {
		ArrayList<Double> list = new ArrayList<Double>();
		//top left
		list.add(pivotX1-boxSize);
		list.add(pivotY1);
		return list;
	}
	
	@Override
	public ArrayList<Double> getBox2Coordinates() {
		ArrayList<Double> list = new ArrayList<Double>();
		//bottom right
		list.add(pivotX1);
		list.add(pivotY1+boxSize);
		return list;
	}

	@Override
	public ArrayList<Double> getBox3Coordinates() {
		ArrayList<Double> list = new ArrayList<Double>();
		//bottom left
		list.add(pivotX1-boxSize);
		list.add(pivotY1+boxSize);
		return list;
	}

	
	//getArr returns the indices of the elements that face the bottom/right/left based on the direction the block is currently facing
	@Override
	public ArrayList<int[][]> getArr() {
		int i = (int)(getPivotY1()/getBoxSize());
		int j = (int)(getPivotX1()/getBoxSize());
		int[][] arr = {{i,j}, {i+1,j-1}, {i+1,j}, {i,j-1}}; //indices for all the elements in the block
		int[][] arr2 = {{i+1, j-1}, {i+1,j}}; //indices of the elements facing downwards
		int[][] arr3 = {{i,j},{i+1,j}}; //indices of the elements facing right
		int[][] arr4 = {{i,j-1},{i+1,j-1}}; //indices of the elements facing left
		ArrayList<int[][]> arrList = new ArrayList<int[][]>();
		arrList.add(arr);
		arrList.add(arr2);
		arrList.add(arr3);
		arrList.add(arr4);
		return arrList;
	}

	//getFourDirectionsArr returns the indices of all the elements for all the directions a block could be facing (in the order top-bottom-left-right)
	//returns nothing because we never need this information for a Box Block
	@Override
	public ArrayList<int[][]> getFourDirectionsArr() {
		return null;
	}
	
	//method that gives the x and y coordinates of the pivot when centering in a heightXwidth area
	public int[] getCenterPivotCoordinates(int height, int width) {
		int[] toReturn = {(width/2), (height/2)-20};
		return toReturn;
	}

}
