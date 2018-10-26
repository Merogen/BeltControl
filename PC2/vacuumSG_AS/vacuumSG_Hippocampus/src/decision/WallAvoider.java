package decision;

import platform.Agent;

public class WallAvoider extends Decision{

	
	
	public WallAvoider(Agent a) {
		super(a);
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
			
			// move forward
			command[1]=0.05;
			
			if (enacted[3]!=0) command[2]=-Math.PI/100;
			else command[2]=Math.PI/60;
		}
		else{
			// if wall in front, rotate right
			command[2]=-Math.PI/20;
		}
		
		return command;
	}

}
