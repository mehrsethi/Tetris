//BlockFactory is used to simulate making a Block of a given type
//actually makes the UI Rectangles for each element of the block

package view;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//TODO singleton!!
public class BlockFactory {
	private int boxSize; //size of each element/box of the Block
	private Random rand;
	
	//constructor
	public BlockFactory(int bs) {
		boxSize = bs;
		rand = new Random();
	}
	
	//format each element/box of the Block by making it a random color (out of the colors of the theme)
	private void formatBox(Rectangle[] arr) {
		int r = rand.nextInt(66);
		int g = rand.nextInt(140) + 60;
		int b = rand.nextInt(50) + g;
		Color color = Color.rgb(r, g, b);
		for (int i=0; i<arr.length; i++) {
			arr[i].setFill(color);
			arr[i].setStroke(Color.rgb(206, 218, 234));
			arr[i].setStrokeWidth(2);
		}
	}
	
	
	//MAKE METHODS
	//input for all the make methods is the x and y value of the pivot of a Block
	//all the make methods always return in the order pivot, box1, box2, box3 (to correspond with the model)
	
	//makeBox - makes a Box block
	public Rectangle[] makeBox(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px-boxSize, py, boxSize, boxSize);
		arr[2] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		arr[3] = new Rectangle(px-boxSize, py+boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}

	//makeLine - makes a Line block
	public Rectangle[] makeLine(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		arr[2] = new Rectangle(px, py-boxSize, boxSize, boxSize);
		arr[3] = new Rectangle(px, py-2*boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
	
	//makeT - makes a T block
	public Rectangle[] makeT(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		arr[2] = new Rectangle(px+boxSize, py, boxSize, boxSize);
		arr[3] = new Rectangle(px-boxSize, py, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
	
	//makeLeftL - makes a LeftL block
	public Rectangle[] makeLeftL(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px+boxSize, py, boxSize, boxSize);
		arr[2] = new Rectangle(px+2*boxSize, py, boxSize, boxSize);
		arr[3] = new Rectangle(px, py-boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
	
	//makeRightL - makes a RightL block
	public Rectangle[] makeRightL(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px+boxSize, py, boxSize, boxSize);
		arr[2] = new Rectangle(px+2*boxSize, py, boxSize, boxSize);
		arr[3] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
	
	//makeLeftFour - makes a LeftFour block
	public Rectangle[] makeLeftFour(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px-boxSize, py, boxSize, boxSize);
		arr[2] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		arr[3] = new Rectangle(px+boxSize, py+boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
	
	//makeRightFour - makes a RightFour block
	public Rectangle[] makeRightFour(int px, int py) {
		Rectangle[] arr = new Rectangle[4];
		arr[0] = new Rectangle(px, py, boxSize, boxSize);
		arr[1] = new Rectangle(px+boxSize, py, boxSize, boxSize);
		arr[2] = new Rectangle(px, py+boxSize, boxSize, boxSize);
		arr[3] = new Rectangle(px-boxSize, py+boxSize, boxSize, boxSize);
		formatBox(arr);
		return arr;
	}
}
