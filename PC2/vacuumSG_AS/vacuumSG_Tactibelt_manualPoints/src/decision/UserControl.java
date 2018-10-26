package decision;

import platform.Agent;


public class UserControl extends Decision{

	public static float RANGE=30;
	
	public static int NB_VIBRATOR=360;
	
	public static int ANGLE=360/NB_VIBRATOR;
	
	
	public int angle_previous=0;
	public int pwm_previous=5;

	

	
	public UserControl(Agent a){
		super(a);
		
		vibrators=new float[NB_VIBRATOR];
		direction_target=new float[NB_VIBRATOR];
		direction_path=new float[NB_VIBRATOR];
		direction_goals=new float[NB_VIBRATOR];
		
		data=new boolean[4];	// 0 walls, 1 target, 2 way points, 3 goals
		for (int i=0;i<data.length;i++){
			data[i]=true;
		}
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
			command[2]= Math.PI/50*2;
		}
		if (!action[0] && action[1]){ // turn right
			command[2]=-Math.PI/50*2;
		}
		
		if (action[2] && !action[3]){ // forward
			command[1]= 0.1*2;
		}
		if (!action[2] && action[3]){ // backward
			command[1]=-0.1*2;
		}
		
		if (action[4] && !action[5]){ // left
			command[0]= 0.1*2;
		}
		if (!action[4] && action[5]){ // right
			command[0]=-0.1*2;
		}
		
		// define surrounding environment
		for (int v=0;v<NB_VIBRATOR;v++){
			float min=RANGE;
			for (int a=0;a<ANGLE;a++){
				if (agent.body.probe1.distRetina[(a+360-(ANGLE/2)+v*ANGLE)%360]<RANGE) min=(float)agent.body.probe1.distRetina[(a+360-(ANGLE/2)+v*ANGLE)%360];
			}
			if (agent.decision.data[0])	vibrators[v]=RANGE-min;
			else vibrators[v]=0;
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
		
		for (int v=0;v<NB_VIBRATOR;v++) direction_target[v]=0;
		
		if (agent.decision.data[1]){
			direction_target[(int)((angle+180)%360/ANGLE)]=Math.max(10, RANGE-dist*5);
			
			if (direction_target[NB_VIBRATOR/2]!=0){
				int val=(int)(direction_target[NB_VIBRATOR/2]-15);
				if (val>9) val=9;
			}
			else{
				if (vibrators[NB_VIBRATOR/2]==0){
	
				}
				else{
					int val=(int)(vibrators[NB_VIBRATOR/2]/2);
					if (val>9) val=9;
				}
			}
		}
		
		
		// directions of way points
		for (int v=0;v<NB_VIBRATOR;v++) direction_path[v]=0;
		if (agent.decision.data[2]){
			for (int i=0;i<agent.hippocampus.pointList.size();i++){
				if (agent.hippocampus.pointList.get(i).visible){
					direction_path[(int)((agent.hippocampus.pointList.get(i).angle+360)%360/ANGLE)]=Math.max(10, RANGE-agent.hippocampus.pointList.get(i).distance*5);
				}
			}
		}
		
		// directions of way points
		for (int v=0;v<NB_VIBRATOR;v++) direction_goals[v]=0;
		if (agent.decision.data[3]){
			for (int i=0;i<agent.hippocampus.goals.size();i++){
				direction_goals[(int)((agent.hippocampus.goals.get(i).angle+360)%360/ANGLE)]=Math.max(10, RANGE-agent.hippocampus.goals.get(i).distance*5);
			}
		}
		
		if (agent.started) agent.communication.sendMsg(vibrators, direction_target);
		
		return command;
		
	}
}
