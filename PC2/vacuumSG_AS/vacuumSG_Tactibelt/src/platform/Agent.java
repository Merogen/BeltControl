package platform;

import hippocampus.Hippocampus;
import decision.*;
import display.*;
import environment.*;


/**
 * Agent
 * @author simon
 *
 */
public class Agent extends Thread{


	public Robot body;
	
	public Decision decision;
	
	public Hippocampus hippocampus;
	
	//public Communication communication;
	
	private int state;     						//  1 = active, 0 = stopped
	private int stepNb;
	private boolean running=true;
	
	public float[][] path;						// sequence of previous positions
	public static int pathLenght=100;			// length of the trace (in steps)
	
	private int ident;
	
	public Communication communication;
	
	public Main main;
	
	
	//////////////////////////////////////////////
	public Agent(int id, Main m){

		ident=id;
		
		main=m;
		
		communication=new Communication();
		
		body=new Robot(ident);
		state=1;
		stepNb=0;

		path=new float[pathLenght][2];
		
		//decision=new WallAvoider(this);			// define here the used decisional system
		//decision=new Braitenberg(this);
		decision=new UserControl(this);
		
		
		
		hippocampus=new Hippocampus(this);
	}
	
	// Main control loop
	public void run(){
		
		while(running){
			if (state>0){
				
				//////////////////////////////////////////////////////////////
				// define action from decisional system
				
				double[] command=decision.decision(body.sensors);
				
				
				///////////////////////////////////////////////////////////
				// send action to the body
				body.move(command);					// command the robot
				body.eatFish();						// eat action when the agent is over a prey
				//body.center();					// constraint the agent to a grid
				
				
				
				///////////////////////////////////////////////////////////
				// trace path
				for (int i=pathLenght-1;i>0;i--){
					path[i][0]=path[i-1][0];
					path[i][1]=path[i-1][1];
				}
				path[0][0]=body.position[0];
				path[0][1]=body.position[1];
				
				///////////////////////////////////////////////////////////
				// update hippocampus
				hippocampus.update();
				
				///////////////////////////////////////////////////////////
				// tempo
				try{Thread.currentThread();
				Thread.sleep(50);}
				catch(Exception ie){}
				
			}
			else{
				try{Thread.currentThread();
				Thread.sleep(200);}
				catch(Exception ie){}
			}
		}
	}
	
	// move an agent to a predefined position
	public void setPosition(float[] p){
		body.setPosition(p);
		for (int i=pathLenght-1;i>=0;i--){
			path[i][0]=body.position[0];
			path[i][1]=body.position[1];
		}
	}
	
	// set or change environment
	public void setEnvironment(Environment e){
		body.setEnvironment(e);
		decision.defineSensors();
	}
	
	// get state (1 = active, 0 = stopped)
	public int state(){
		return state;
	}
	
	// start and stop agent
	public void startAgent(){
		state=1;
	}
	public void stopAgent(){
		state=0;
	}
	
	//reset trace buffer
	public void clearTrace(){
		for (int i=0;i<pathLenght;i++){
			path[i][0]=body.position[0];
			path[i][1]=body.position[1];
		}
	}
	
	public void stopThread(){
		running=false;
	}
	
	// get the cycle counter
	public int getStepNb(){
		return stepNb;
	}
	
	// get agent ident number
	public int getIdent(){
		return ident;
	}

	public float getSensor(int i){
		return body.getSensor(i);
	}
	
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	// add displayer. The probeDisplay panel shows an example
	public void setDisplay(){
		
		boolean probeDisplay=true;     // boolean used to enable/disable panel
		boolean controlDisplay=true;
		boolean HippocampusDisplay=true;
		
		//////////////////////
		if (probeDisplay){
			int size=main.displaySize();
			int i=0;
			boolean found=false; 
			while (i<size && !found){
				if (main.getDisplay(i).getClass().getName().equals("display.ProbeFrame")) found=true;
				i++;
			}
			if   (!found) main.addDisplay(new ProbeFrame(this));
			else ((ProbeFrame)main.getDisplay(i-1)).setAgent(this);
		}
		//////////////////////
		if (controlDisplay){
			int size=main.displaySize();
			int i=0;
			boolean found=false; 
			while (i<size && !found){
				if (main.getDisplay(i).getClass().getName().equals("display.ControlFrame")) found=true;
				i++;
			}
			if   (!found) main.addDisplay(new ControlFrame(this));
			else ((ControlFrame)main.getDisplay(i-1)).setAgent(this);
		}
		//////////////////////
		if (HippocampusDisplay){
			int size=main.displaySize();
			int i=0;
			boolean found=false; 
			while (i<size && !found){
				if (main.getDisplay(i).getClass().getName().equals("display.HippocampusFrame")) found=true;
				i++;
			}
			if   (!found) main.addDisplay(new HippocampusFrame(this));
			else ((HippocampusFrame)main.getDisplay(i-1)).setAgent(this);
		}
		//////////////////////
	}
	

	// save data about the agent
	public void save(){
		hippocampus.save();
	}
	
}
