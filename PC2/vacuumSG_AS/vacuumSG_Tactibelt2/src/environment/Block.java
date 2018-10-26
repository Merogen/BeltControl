package environment;

import java.awt.Color;

/**
 * definition of a block in the environment
 * @author simon
 *
 */
public class Block {

	private Color color;
	private int touch;
	private String name;
	
	public Block(int tactile, Color visual, String n){
		color=visual;
		touch=tactile;
		name=n;
	}
	
	public Color seeBlock(){
		return color;
	}
	
	public int touchBlock(){
		return touch;
	}
	
	public boolean isVisible(){
		return (!color.equals(Environment.FIELD_COLOR));
	}
	
	public boolean isWall(){
		return (touch==Environment.HARD);
	}
	
	public boolean isFood(){
		return (touch==Environment.FOOD);
	}
	
	public boolean isAlga(){
		return (touch==Environment.SMOOTH);
	}
	
	public boolean isEmpty(){
		return (touch==Environment.EMPTY);
	}
	
	public boolean isWalkthroughable(){
		return (touch!=Environment.HARD);
	}
	
	public boolean isTraversableObject(){
		return (touch==Environment.FOOD || touch==Environment.SMOOTH);
	}
	
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
	
	public int getTactile(){
		return touch;
	}
	
	public void setBlock(Color visual, int tactile, String n){
		color=visual;
		touch=tactile;
		name=n;
	}
}
