//Block class - abstract (superclass for all the block types)
//allows for ploymorphism in the controller, which can call methods regardless of what the type of the block is
//models the idea of a block -- each block has a pivot element and 3 other elements
//store the x and y for the pivot and use those within each subclass to calculate the x and y of the other three elements
//(the x and y for the other elements change depending on which direction the block is facing and the type of the block, but
//the pivot's x and y on change through user events)

package model;

import java.util.ArrayList;

public abstract class Block {
	//x and y of the pivot
	protected Double pivotX1;
	protected Double pivotY1;
	//size of each element/box
	protected Double boxSize; 
	//which direction the block is currently facing
	protected Facing side;
	//the type of the block - one of seven possibilities
	protected BlockType type;
	
	
	//constructor for the subclasses to call
	protected Block(double pivotX1, double pivotY1, double boxSize) {
		this.pivotX1 = Double.valueOf(pivotX1);
		this.pivotY1 = Double.valueOf(pivotY1);
		this.boxSize = Double.valueOf(boxSize);
	}

	
	//abstract methods to get the x and y values of the non-pivot elements
	public abstract ArrayList<Double> getBox1Coordinates();
	public abstract ArrayList<Double> getBox2Coordinates();
	public abstract ArrayList<Double> getBox3Coordinates();
	
	//abstract methods to get the relevant arrays for the gamegrid
	//getArr returns the indices of the elements that face the bottom/right/left based on the direction the block is currently facing
	public abstract ArrayList<int[][]> getArr();
	//getFourDirectionsArr returns the indices of all the elements for all the directions a block could be facing (in the order top-bottom-left-right)
	public abstract ArrayList<int[][]> getFourDirectionsArr();
	
	//method that gives the x and y coordinates of the pivot when centering in a heightXwidth area
	public abstract int[] getCenterPivotCoordinates(int height, int width);
	
	
	//changeSide updates the value of the side attribute to be the direction that the block is facing after rotation
	//rotation is ALWAYS clockwise
	public void changeSide() {
		if (this.side==Facing.BOTTOM) {
			this.side = Facing.LEFT;
		}
		else if (this.side==Facing.LEFT) {
			this.side = Facing.TOP;
		}
		else if (this.side==Facing.TOP) {
			this.side = Facing.RIGHT;
		}
		else if (this.side==Facing.RIGHT) {
			this.side = Facing.BOTTOM;
		}
	}
	
	
	//moveAdjacent moves an element that is linearly around the pivot to logically simulate a rotation of the block
	//"linearly around" means that it is either in the same row or in the same column as the pivot
	//this is done based on the direction the adjacent element is facing and its distance from the pivot
	//the distance is measured in number of elements it is away from the pivot
	protected ArrayList<Double> moveAdjacent(Facing f, int distance) {
		ArrayList<Double> list = new ArrayList<Double>();
		if (f==Facing.RIGHT) {
			list.add(pivotX1);
			list.add(pivotY1+boxSize*distance);
		}
		else if (f==Facing.BOTTOM) {
			list.add(pivotX1-boxSize*distance);
			list.add(pivotY1);
		}
		else if (f==Facing.LEFT) {
			list.add(pivotX1);
			list.add(pivotY1-boxSize*distance);
		}
		else if (f==Facing.TOP) {
			list.add(pivotX1+boxSize*distance);
			list.add(pivotY1);
		}
		return list;
	}
	
	//moveFurther moves an element that is diagonally around the pivot to logically simulate a rotation of the block
	//this is also done based on the direction the adjacent element is facing and its distance from the pivot
	//the distance is measured in number of elements it is away from the pivot
	protected ArrayList<Double> moveFurther(Facing f, int distance) {
		ArrayList<Double> list = new ArrayList<Double>();
		if (f==Facing.RIGHT) {
			list.add(pivotX1-boxSize*distance);
			list.add(pivotY1+boxSize*distance);
		}
		else if (f==Facing.BOTTOM) {
			list.add(pivotX1-boxSize);
			list.add(pivotY1-boxSize);
		}
		else if (f==Facing.LEFT) {
			list.add(pivotX1+boxSize*distance);
			list.add(pivotY1-boxSize*distance);
		}
		else if (f==Facing.TOP) {
			list.add(pivotX1+boxSize);
			list.add(pivotY1+boxSize);
		}
		return list;
	}

	
	//increases the x value of the pivot to simulate a rightward movement of the block
	public void incrementPivotX1() {
		this.pivotX1 = pivotX1 + boxSize;
	}
	
	//decreases the x value of the pivot to simulate a leftward movement of the block
	public void decrementPivotX1() {
		this.pivotX1 = pivotX1 - boxSize;
	}

	//increases the y value of the pivot to simulate a downward movement of the block
	public void incrementPivotY1() {
		this.pivotY1 = pivotY1 + boxSize;
	}
	
	
	//getters and setters for the various attributes of the Block class
	public void setSide(Facing side) {
		this.side = side;
	}
	
	public void setPivotX1(double x1) {
		this.pivotX1 = x1;
	}
	
	public void setPivotY1(double y1) {
		this.pivotY1 = y1;
	}
	
	public BlockType getType() {
		return type;
	}

	public Double getPivotX1() {
		return pivotX1;
	}

	public Double getPivotY1() {
		return pivotY1;
	}
	
	public Double getBoxSize() {
		return boxSize;
	}
	
	public Facing getSide() {
		return side;
	}
}
