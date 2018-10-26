package decision;

import platform.Agent;
import sensorySystem.LightSensor;


public class Braitenberg extends Decision{

	public Braitenberg(Agent a){
		super(a);
	}
	
	// define the list of sensors needed for this decision system
	public void defineSensors(){
		
	}
	
	// get the interaction to enact
	public double[] decision(float[] enacted){
		
		double[] command=new double[8];
		for (int i=0;i<8;i++){
			command[i]=0;
		}
		
		command[3]=1;
		command[4]=1;
		command[5]=1;
		
		if (enacted[2]==0){
			
			// read light sensor inputs
			float valG=10*agent.getSensor(0);
			float valD=10*agent.getSensor(1);

			command[1]=Math.min(0.2,0.05+valG+valD);

			// if wall at left side, turn rigth
			if (enacted[3]!=0) command[2]=-Math.PI/100;
			else{
				command[2]=(valG-valD)*Math.abs(valG-valD)/Math.max(valG*valG+0.001,valD*valD+0.001)/10;
			}
		}
		else{
			// if wall in front, rotate right
			command[2]=-Math.PI/20;
		}
		
		return command;
		
	}
}
