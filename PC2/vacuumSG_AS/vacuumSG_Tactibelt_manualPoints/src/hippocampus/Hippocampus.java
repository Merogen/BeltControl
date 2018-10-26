package hippocampus;

import java.util.ArrayList;

import platform.Agent;

public class Hippocampus {


	public int counter=0;
	
	
	public ArrayList<Cell> pointList;
	
	
	public ArrayList<Cell> goals;
	
	public Cell currentPlace=null;
	public Cell currentGoal=null;
	
	public Cell targetPlace=null;

	private Agent agent;
	
	
	public Hippocampus(Agent a){
		
		agent=a;
		
		pointList=new ArrayList<Cell>();
		goals=new ArrayList<Cell>();
	}
	
	public void addPoint(float x, float y){
		pointList.add(new Cell(x,y,counter));
		counter++;
	}
	
	public void update(){
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// detect surrounding goals: a goal is defined as a local distance maximum on 20°
		detectGoals();
		for (int i=0;i<goals.size();i++){
			// get distance
			goals.get(i).distance=agentDist(goals.get(i));
			
			// get angle
			float x=agent.body.position[0]+0.5f-goals.get(i).pos_x;
			float y=agent.body.position[1]+0.5f-goals.get(i).pos_y;
						
			float theta= -(float)Math.toDegrees(Math.atan2(y, x))+(float)Math.toDegrees(agent.body.position[2]);
						
			while (theta<0) theta+=360;
			while (theta>=360) theta-=360;
			goals.get(i).angle=theta;
		}
		
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// define visible way points
		for (int i=0;i<pointList.size();i++){
			// get distance
			pointList.get(i).distance=agentDist(pointList.get(i));
			
			// get angle
			float x=agent.body.position[0]+0.5f-pointList.get(i).pos_x;
			float y=agent.body.position[1]+0.5f-pointList.get(i).pos_y;
			
			float theta= -(float)Math.toDegrees(Math.atan2(y, x))+(float)Math.toDegrees(agent.body.position[2]);
			
			while (theta<0) theta+=360;
			while (theta>=360) theta-=360;
			pointList.get(i).angle=theta;
			
			//System.out.println(Math.toDegrees(agent.body.position[2])+" ; "+pointList.get(i).angle);
			
			pointList.get(i).visible=(pointList.get(i).distance < agent.body.probe1.distRetina[(int) theta]/10);
		}
		
	}
	
	public static float dist2(Cell c1, Cell c2){
		return (c1.pos_x-c2.pos_x)*(c1.pos_x-c2.pos_x) + (c1.pos_y-c2.pos_y)*(c1.pos_y-c2.pos_y);
	}
	public static float dist(Cell c1, Cell c2){
		return (float)Math.sqrt(dist2(c1, c2));
	}
	
	public float agentDist2(Cell c1){
		return (agent.body.position[0]+0.5f-c1.pos_x)*(agent.body.position[0]+0.5f-c1.pos_x) 
			 + (agent.body.position[1]+0.5f-c1.pos_y)*(agent.body.position[1]+0.5f-c1.pos_y);
	}
	public float agentDist(Cell c1){
		return (float)Math.sqrt(agentDist2(c1));
	}
	
	public float targetDist2(Cell c1){
		return (agent.body.marker_x-c1.pos_x)*(agent.body.marker_x-c1.pos_x) 
			 + (agent.body.marker_y-c1.pos_y)*(agent.body.marker_y-c1.pos_y);
	}
	public float targetDist(Cell c1){
		return (float)Math.sqrt(targetDist2(c1));
	}
	
	/////////////////////////////////////////////////////////////////////
	// detect surrounding goals
	private void detectGoals(){
		float[] d=new float[360];
		for (int i=0;i<360;i++){
			for (int a=0;a<20;a++){
				d[i]+=agent.body.probe1.distRetinaAbs[(i+a)%360];
			}
			d[i]=d[i]/200-0.5f;
		}
		goals.clear();
		for (int i=1;i<=360;i++){
			if (d[i%360]>d[i-1] && d[i%360]>=d[(i+1)%360]){
				float px=(float)(d[i%360]*Math.cos( ( -(i+10)%360)*Math.PI/180 +Math.PI/2) );
				float py=(float)(d[i%360]*Math.sin( ( -(i+10)%360)*Math.PI/180 +Math.PI/2) );
				goals.add(new Cell(px+agent.body.position[0],py+agent.body.position[1],-1));
			}
		}
	
		// remove goals that are too close from each other
		for (int g1=0;g1<goals.size();g1++){
			for (int g2=g1+1;g2<goals.size();g2++){
				if (dist2(goals.get(g1), goals.get(g2))<10){
					// only keep the closest
					if (agentDist2(goals.get(g1))<=agentDist2(goals.get(g2))){
						goals.remove(g2);
						g2--;
					}
					else{
						goals.remove(g1);
						g1--;
						g2=goals.size();
					}
				}
			}
		}
	}
		
}
