//the GameGrid class contains a 2D array of booleans that models the playing field for the game
//if a particular position in the array is true, it means that that spot on the board is already occupied by another block
//allows checking for when a block stops "falling", when a row needs to be deleted, and when the user has lost
package model;

import java.util.ArrayList;

public class GameGrid {
	private boolean[][] grid; //2D array/grid of the playing field
	
	//constructor
	public GameGrid(int numRows, int numCols) {
		//start the board by making all the positions unoccupied (call the reset method)
		grid = new boolean[numRows][numCols];
		reset();
	}
	
	//reset the board by making all the positions unoccupied
	public void reset() {
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[i].length; j++) {
				grid[i][j] = false;
			}
		}
	}
	
	
	//SWITCHING METHODS
	//makes the spot at the ith row and jth column true/occupied
	public void switchOn(int i, int j) {
		if (i>=0&&j>=0&&i<grid.length&&j<grid[0].length) {
			grid[i][j] = true;
		}
	}
	//makes the spot at the ith row and jth column false/unoccupied
	public void switchOff(int i, int j) {
		if (i>=0&&j>=0&&i<grid.length&&j<grid[0].length) {
			grid[i][j] = false;
		}
	}
	
	
	//CHECKING METHODS
	//based on the Block b's block type and the direction it is facing
	//check if any of the bottom facing elements come in contact with anything that is true/any spot that was already occupied
	//if they do, return true to indicate that the block is touching something on the bottom
	private boolean checkTouchDown(int[][] arr) {
		for (int[] k: arr) {
			int i = k[0];
			int j = k[1];
			if (i>=-1&&j>=0&&i<grid.length-1&&j<grid[0].length) {
				if (grid[i+1][j]) {
					return true;
				}
			}
			else { //return true if the block has reached the bottom of the board/playing field
				return true;
			}
		}
		return false;
	}
	
	//based on the Block b's block type and the direction it is facing
	//check if any of the left facing elements come in contact with anything that is true/any spot that was already occupied
	//or if they exceed the bounds of the board/playing field
	//if they do, return true to indicate that the block is touching something on the left
	private boolean checkTouchLeft(int[][] arr) {
		for (int[] k: arr) {
			int i = k[0];
			int j = k[1];
			if (i>=0&&j>=1&&i<grid.length&&j<grid[0].length+1) {
				if (grid[i][j-1]) {
					return true;
				}
			}
			else {
					return true;
			}
		}
		return false;
	}
	
	//based on the Block b's block type and the direction it is facing
	//check if any of the right facing elements come in contact with anything that is true/any spot that was already occupied
	//or if they exceed the bounds of the board/playing field
	//if they do, return true to indicate that the block is touching something on the right
	private boolean checkTouchRight(int[][] arr) {
		for (int[] k: arr) {
			int i = k[0];
			int j = k[1];
			if (i>=0&&j>=-1&&i<grid.length&&j<grid[0].length-1) {
				if (grid[i][j+1]) {
					return true;
				}
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	
	//MOVEMENT METHODS
	//uses the above three methods to check if movement in a certain direction is possible
	//i.e. the block hasn't reached the edge or is touching another block in that direction
	//if movement is possible, move the block in that direction by calling the moveHelper method
	//the only exception is if the direction is "downC", which means we just want to know if movement in the downward direction is possible but we do not actually want to move in that direction
	private boolean checkAndMove(int[][] arr, int[][] arr2, int[][] arr3, int[][] arr4, String d) { //arr2 is for down, arr3 for right and arr4 for left
		boolean check;
		if (d.equals("downC")) { //only checking not moving
			check = checkTouchDown(arr2);
			return check;
		}
		else if (d.equals("down")) {
			check = checkTouchDown(arr2);
		}
		else if (d.equals("right")) {
			check = checkTouchRight(arr3);
		}
		else { //direction of movement is left
			check = checkTouchLeft(arr4);
		}
		if (check) {
			return true;
		}
		else {
			moveHelper(arr, d);
			return false;
		}
	}
	
	//simulates moving the block (assumes that movement in that direction is possible)
	//logic here is essentially switch off all the current positions occupied by the block
	//then "move" the block one step in the appropriate direction and switch on all the positions in the grid occupied by the block
	private void moveHelper(int[][] arr, String direction) {
		//switch off all the current positions
		for (int[] k: arr) {
			int i = k[0];
			int j = k[1];
			switchOff(i, j);
		}
		//switch on the position the block would occupy after movement in the required direction
		for (int[] k: arr) {
			int i = k[0];
			int j = k[1];
			if (direction.equals("down")) {
				switchOn(i+1, j);
			}
			else if (direction.equals("left")) {
				switchOn(i, j-1);
			}
			else if (direction.equals("right")) {
				switchOn(i, j+1);
			}
		}
	}
	
	//moveBlock gives a public interface to move a block in a certain direction
	//calls the private checkAndMove method to do the checking and movement
	public boolean moveBlock(Block b, String d) {
		ArrayList<int[][]> arr = b.getArr();
		return checkAndMove(arr.get(0), arr.get(1), arr.get(2), arr.get(3), d);
	}
	
	//helper method to check the current status of the grid - only for testing
	private void printGrid() {
		for (boolean[] b : grid) {
			for (boolean bool : b) {
				System.out.print(bool + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	//ROTATION METHODS
	//simulates rotating a block
	//similar to moveHelper, it switches off all the current positions occupied by the block
	//and switches on all positions occupied by the rotated version of that block
	private void rotate(int[][] arr, int[][] arr2) { //arr is the indices associated with the current position of the block, arr2 is the indices associates with the rotated position
		for (int[] k : arr) {
			switchOff(k[0], k[1]);
		}
		for (int[] k : arr2) {
			switchOn(k[0], k[1]);
		}
	}
	
	//calls the rotate method by passing in the relevant arrays based on the direction the block is facing
	private void rotateHelper(int[][] left, int[][] right, int[][] top, int[][] bottom, Block b) {
		if (b.getSide()==Facing.TOP) {
			rotate(left,top);
		}
		else if (b.getSide()==Facing.RIGHT){
			rotate(top,right);
		}
		else if (b.getSide()==Facing.BOTTOM){
			rotate(right,bottom);
		}
		else{ //block is facing left
			rotate(bottom,left);
		}
	}
	
	//public interface for rotating a block
	//gets the relevant position info from the block and calls the rotate/rotateHelper method
	public void changeSides(Block b) {
		ArrayList<int[][]> arr = b.getFourDirectionsArr(); //return in the order top-bottom-left-right (except for line, which is top/bottom-left/right)
		if (b.getType()==BlockType.LINE) {
			if (b.getSide()==Facing.BOTTOM||b.getSide()==Facing.TOP) {
				rotate(arr.get(1),arr.get(0));
			}
			else {
				rotate(arr.get(0),arr.get(1));
			}
		}
		else if (b.getType()!=BlockType.BOX) { //do nothing for a Box because rotation does not affect it
			rotateHelper(arr.get(2),arr.get(3),arr.get(0),arr.get(1),b);
		}
	}
	
	//CHECKING WINNING AND LOSING
	//checks if a row is "full" or not i.e. whether all boxes within the row are occupied or not
	//in the grid, this means the every element of the row is set to true
	//returns the index of the row is a full row is found, returns -1 otherwise
	public int checkFullRow() {
		for (int i=0; i<grid.length; i++) {
			boolean check = true;
			for (int j=0; j<grid[i].length; j++) {
				if (!grid[i][j]) {
					check = false;
				}
			}
			if (check) {
				return i;
			}
		}
		return -1;
	}
	
	//takes in the index of the row to be deleted as input
	//simulates deleting a row by shifting all the rows above it down by 1
	public void deleteRow(int r) {
		for (int i=r-1; i>=0; i--) {
			for (int j=0; j<grid[i].length; j++) {
				grid[i+1][j] = grid[i][j];
			}
		}
		for (int j=0; j<grid[0].length;j++) {
			grid[0][j] = false;
		}
	}
	
	//takes in indices for the row and col of a element as input
	//determines whether that position is occupied/true or not
	public boolean isOn(int i, int j) {
		if (i>=0&&j>=0&&i<grid.length&&j<grid[0].length) {
			return grid[i][j];
		}
		return true;
	}
}
