package display;

import platform.Agent;

/**
 * A frame to display the agent's point of view
 * @author simon
 *
 */
public class ControlFrame extends EnvFrame{
	
	private static final long serialVersionUID = 1L;

	public ControlFrame(Agent a){
		
		super(a);
		
		this.setTitle("Control");
    	this.setSize(500, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);

    	panel=new ControlPanel(a);
    	
    	this.setContentPane(panel);
	}
}
