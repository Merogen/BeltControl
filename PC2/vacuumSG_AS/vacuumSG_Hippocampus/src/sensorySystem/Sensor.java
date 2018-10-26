package sensorySystem;

import environment.*;

/**
 * A generic class of sensor
 * @author simon
 */
public class Sensor {

	protected Probe probe;
	
	public Sensor(Robot r, float posX, float posY){
		probe = new Probe(r, posX, posY);
		probe.setEnvironment(r.getEnvironment());
	}
	
	// get the state of this sensor
	public float getValue(){
		return 0;
	}
	
}
