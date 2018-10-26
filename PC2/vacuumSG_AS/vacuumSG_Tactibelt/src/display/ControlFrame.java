package display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import platform.Agent;

/**
 * A frame to display the agent's point of view
 * @author simon
 *
 */
public class ControlFrame extends EnvFrame{
	
	private static final long serialVersionUID = 1L;

	Agent agent;
	
	public ControlFrame(Agent a){
		
		super(a);
		
		agent=a;
		
		this.setTitle("Control");
    	this.setSize(500, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
		this.addWindowListener(new WindowAdapter() {
	  	      public void windowClosing(WindowEvent e) {
	  	    	agent.communication.close();
	  	        System.exit(0);
	  	      }
	  	    });

    	panel=new ControlPanel(a);
    	
    	this.setContentPane(panel);
	}
}
