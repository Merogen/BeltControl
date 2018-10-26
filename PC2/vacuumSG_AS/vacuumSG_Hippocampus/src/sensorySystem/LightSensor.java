package sensorySystem;

import environment.Environment;
import environment.Robot;

/**
 * An example of simulated light sensor that detects preys as light sources
 * @author simon
 */
public class LightSensor extends Sensor {

	int orientation;
	int span;
	
	float value;
	
	// orientation is the orientation (in degree) of the light sensor in agent's reference, span is the perception field span of the sensor
	public LightSensor(Robot r, float posX, float posY, int theta, int sp){
		super(r, posX, posY);
		orientation=theta;
		span=sp;
		
		value=0;
	}
	
	// compute and return the sensor value
	public float getValue(){
		
		value=0;
		
		probe.rendu();
		for (int i=orientation-(span/2);i<orientation+(span/2);i++){
			if (probe.colRetina[(i+360)%360].equals(Environment.FISH1)){
				value+= 1 / Math.max(1, probe.distRetina[(i+360)%360] * probe.distRetina[(i+360)%360]);
			}
		}
		return value;
	}
	
}
