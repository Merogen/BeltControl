package display;

import platform.Agent;

/**
 * A frame to display the agent's point of view
 * @author simon
 *
 */
public class ProbeFrame extends EnvFrame{
	
	private static final long serialVersionUID = 1L;

	public ProbeFrame(Agent a){
		
		super(a);
		
		this.setTitle("Visual system");
    	this.setSize(720, 225);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);

    	panel=new ProbePanel(a);
    	
    	this.setContentPane(panel);
	}
}
