package display;

import java.awt.Graphics;

import platform.Agent;
import platform.PrintableFrame;

/**
 * generic class of Frames that display informations about an agent
 * @author simon
 */
 
/* - inherited from PrintableFrame :
 *   boolean printable   : define if the frame can be printed
 *   int indexImage      : counter for image name
 */
public class EnvFrame extends PrintableFrame{

	private static final long serialVersionUID = 1L;
	
	protected EnvPanel panel;
	
	/**
	 * constructor
	 * @param a : the agent that provide information
	 */
	public EnvFrame(Agent a){
		super();
		panel=new EnvPanel(a);
	}
	
	/**
	 * print the displayed image in a pdf file
	 */
	public void drawPDF(Graphics g){
		panel.drawPDF(g);
	}
	
	/**
	 * change the agent
	 */
	public void setAgent(Agent a){
		panel.setAgent(a);
	}

	public void paint(){
    	panel.repaint();
    }
}
