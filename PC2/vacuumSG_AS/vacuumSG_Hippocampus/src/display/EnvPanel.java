package display;

import java.awt.Graphics;

import platform.Agent;
import platform.PrintablePanel;


/**
 * generic class of Panels that display informations about an agent
 * @author simon
 */

public class EnvPanel extends PrintablePanel{

	private static final long serialVersionUID = 1L;
	
	protected Agent agent;
	
	public EnvPanel(Agent a){
		super();
		agent=a;
	}
	
	/**
	 * change the agent
	 */
	public void setAgent(Agent a){
		agent=a;
	}
	
	/**
	 * print the displayed image in a pdf file
	 */
	public void drawPDF(Graphics g){
		paintComponent(g);
	}
	
	public void paintComponent(Graphics g){
	
	}
}
