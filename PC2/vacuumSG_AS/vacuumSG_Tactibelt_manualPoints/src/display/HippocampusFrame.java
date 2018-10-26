package display;

import platform.Agent;

/**
 * A frame to display the agent's point of view
 * @author simon
 *
 */
public class HippocampusFrame extends EnvFrame{
	
	private static final long serialVersionUID = 1L;

	public HippocampusFrame(Agent a){
		
		super(a);
		
		this.setTitle("Hippocampus");
    	this.setSize(1000, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);

    	panel=new HippocampusPanel(a);
    	
    	this.setContentPane(panel);
	}
}
