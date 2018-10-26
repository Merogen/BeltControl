package decision;

import platform.Agent;

/**
 * A generic class of decisional system
 * @author simon
 */
public class Decision {

	Agent agent;
	
	public boolean[] action;
	
	public float[] vibrators;
	
	public float[] direction;
	
	public Decision(Agent a){
		agent=a;
		action=new boolean[6];
		for (int i=0;i<6;i++) action[i]=false;
	}
	
	// define the list of sensors needed for this decision system
	public void defineSensors(){
	
	}
	
	// get the interaction to enact
	public double[] decision(float[] enacted){
		return null;
	}
}
