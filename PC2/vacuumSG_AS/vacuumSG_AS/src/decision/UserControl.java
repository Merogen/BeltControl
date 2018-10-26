package decision;

import platform.Agent;


public class UserControl extends Decision{

	public static float RANGE=30;
	
	public static int NB_VIBRATOR=18;
	
	public static int ANGLE=360/NB_VIBRATOR;
	
	
	public UserControl(Agent a){
		super(a);
		
		vibrators=new float[NB_VIBRATOR];
		direction=new float[NB_VIBRATOR];
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
		
		if (action[0] && !action[1]){ // turn left
			command[2]= Math.PI/50;
		}
		if (!action[0] && action[1]){ // turn right
			command[2]=-Math.PI/50;
		}
		
		if (action[2] && !action[3]){ // forward
			command[1]= 0.1;
		}
		if (!action[2] && action[3]){ // backward
			command[1]=-0.1;
		}
		
		if (action[4] && !action[5]){ // left
			command[0]= 0.1;
		}
		if (!action[4] && action[5]){ // right
			command[0]=-0.1;
		}
		
		// define surrounding environment
		for (int v=0;v<NB_VIBRATOR;v++){
			float min=RANGE;
			for (int a=0;a<ANGLE;a++){
				if (agent.body.probe1.distRetina[(a+360-(ANGLE/2)+v*ANGLE)%360]<RANGE) min=(float)agent.body.probe1.distRetina[(a+360-(ANGLE/2)+v*ANGLE)%360];
			}
			vibrators[v]=RANGE-min;
		}
		
		// define target position
		float x1=agent.body.getPosition()[0];
		float y1=agent.body.getPosition()[1];
		
		float x2=agent.body.marker_x;
		float y2=agent.body.marker_y;
		
		float dist=(float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		
		float angle=0;
		
		if (y1<y2){
			angle=(float) ((agent.body.getPosition()[2]-Math.acos((x2-x1)/dist))*180/Math.PI);
		}
		else{
			angle=(float) ((agent.body.getPosition()[2]+Math.acos((x2-x1)/dist))*180/Math.PI);
		}
		
		angle=(angle+360)%360;
		
		//System.out.println(angle+" ; "+dist+" ; "+agent.body.getPosition()[0]+" ; "+agent.body.getPosition()[1]);
		
		/*for (int v=0;v<NB_VIBRATOR;v++) direction[v]=0;
		direction[(int)((angle+180)%360/ANGLE)]=Math.max(10, RANGE-dist*5);
		
		if (direction[NB_VIBRATOR/2]!=0){
			System.out.println("+"+(int)direction[NB_VIBRATOR/2]);
			int val=(int)(direction[NB_VIBRATOR/2]-15);
			if (val>9) val=9;
			agent.communication.writeVibrator(0, 5, 9-val);
		}
		else{
			if (vibrators[NB_VIBRATOR/2]==0){
				System.out.println("-");
				agent.communication.writeVibrator(0, 0, 0);
			}
			else{
				System.out.println("*"+(int)vibrators[NB_VIBRATOR/2]);
				int val=(int)(vibrators[NB_VIBRATOR/2]/2);
				if (val>9) val=9;
				agent.communication.writeVibrator(0, val, 0);
			}
		}/**/
		
		return command;
		
	}
}
