package hippocampus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import platform.Agent;

public class Hippocampus {

	public static String PATH=System.getProperty("user.home") +"/EdVision/hippocampus1/";	// path of the saved/loaded files
	
	public static String NAME="hippocampus1"; 												// name of the saved/loaded file
	
	public static int NB_DIRECTIONS=8;
	
	public int counter=0;
	
	public DirectionCell[] directionCells;
	
	public GridCellMatrix gridCells;
	
	public float[][] currentMap;
	public float[][] currentMap2;
	
	public ArrayList<PlaceCell> placeList;
	
	public ArrayList<Cell> goals;
	
	public PlaceCell currentPlace=null;
	public Cell currentGoal=null;
	
	public ArrayList<ArrayList<Integer>> paths;
	public PlaceCell targetPlace=null;
	
	
	private Agent agent;
	
	private boolean initialized;
	
	
	public Hippocampus(Agent a){
		
		agent=a;
		
		directionCells=new DirectionCell[NB_DIRECTIONS];
		for (int i=0;i<NB_DIRECTIONS;i++){
			directionCells[i]=new DirectionCell(i*(360/NB_DIRECTIONS));
		}
		
		gridCells=new GridCellMatrix();
		
		currentMap=new float[NB_DIRECTIONS][10];
		currentMap2=new float[NB_DIRECTIONS][10];
		
		placeList=new ArrayList<PlaceCell>();
		
		goals=new ArrayList<Cell>();
		
		paths=new ArrayList<ArrayList<Integer>>();
		
		initialized=false;
		
	}
	
	public void update(){
		
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// update direction cells
		for (int i=0;i<NB_DIRECTIONS;i++){
			directionCells[i].setActivity((int)(agent.body.position[2]*180/Math.PI));
		}
		
		gridCells.update(agent.path[0][0]-agent.path[1][0],agent.path[0][1]-agent.path[1][1]);
		
		//////////////////////////////////////////////////////////////////////////////////////////
		// compute current map
		computeMap();
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// detect surrounding goals: a goal is defined as a local distance maximum on 20°
		detectGoals();
		
		if (!initialized){
			initialize();
			//loadFile();
			initialized=true;
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		// compute place cell activity
		for (int p=0;p<placeList.size();p++) placeList.get(p).compute(currentMap);
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		// check for goal change
		/////////////////////////////////////////////////////////////////////////////////////////
		float angle1=(float) Math.toDegrees(agent.body.position[2]);
		float angle2 = (float) Math.toDegrees(Math.atan2(currentGoal.pos_y-agent.body.position[1], currentGoal.pos_x-agent.body.position[0]));
		angle2=Math.abs(angle2-angle1)%360;
		if (angle2>180) angle2=360-angle2;
		
		if (angle2>45){
			/////////////////////////////////////////////////////////
			// if agent is close from place cell: just change goal
			if (agentDist2(currentPlace)<5){
				// a new goal can be added to the place cell
				Cell temp_goal=getAimedGoal();
				boolean found=false;
				int i=0;
				while (i<currentPlace.neighbors.size() && !found){
					if (dist2(temp_goal,currentPlace.neighbors.get(i))<5) found=true;
					else i++;
				}
				if (!found) currentPlace.neighbors.add(temp_goal);
				
			}
			
			else{
				/////////////////////////////////////////////////////
				// place cell already exists
				int p=0;
				boolean found=false;
				while (p<placeList.size() && !found){
					if (agentDist2(placeList.get(p))<5 && placeList.get(p).activity>0.8) found=true;
					else p++;
				}
				if (found){
					// connect the current cell with this other cell
					currentPlace.connectPlaceCell(placeList.get(p));
					
					// update current place
					currentPlace=placeList.get(p);
				}
				
				/////////////////////////////////////////////////////
				else{
					// create new place cell
					placeList.add(new PlaceCell(agent.path[0][0], agent.path[0][1], currentMap2, goals, counter));
					counter++;
					int index=placeList.size()-1;
					
					// connect the current cell with this new cell
					currentPlace.connectPlaceCell(placeList.get(index));
					
					// update current place
					currentPlace=placeList.get(index);
					
					paths.add(new ArrayList<Integer>());
					paths.get(paths.size()-1).add(currentPlace.ident);
				}
			}
			
				
			// look for the current goal
			angle1=(float) Math.toDegrees(agent.body.position[2]);
			
			float min_angle=181;
			int imin=0;
			
			for (int g=0;g<currentPlace.neighbors.size();g++){
				angle2 = (float) Math.toDegrees(Math.atan2(currentPlace.neighbors.get(g).pos_y-agent.path[0][1],
						                                   currentPlace.neighbors.get(g).pos_x-agent.path[0][0]));
				angle2=Math.abs(angle2-angle1)%360;
				if (angle2>180) angle2=360-angle2;
			    if (angle2<min_angle){
			    	imin=g;
			    	min_angle=angle2;
			    }
			}
			currentGoal=currentPlace.neighbors.get(imin);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// if the agent move near a known place cell, the connect to the current cell
		/////////////////////////////////////////////////////////////////////////////////////////
		if (agentDist2(currentPlace)>5){
			for (int p=0;p<placeList.size();p++){
				if (agentDist2(placeList.get(p))<5 && placeList.get(p).activity>0.5
			     && placeList.get(p).ident!=currentPlace.ident && placeList.get(p).ident!=currentGoal.ident){

					PlaceCell tempCell=placeList.get(p);
					
					// if goal is a place cell, the close cell is set between current and goal (if goal is a place){
					tempCell.connectPlaceCell(currentGoal);

					
					// for current cell : connect temp
					currentPlace.connectPlaceCell(tempCell);
					
					
					// disconnect current and goal if temp is aligned
					if (currentGoal.isPlaceCell){
						float a1 = (float) Math.toDegrees(Math.atan2(currentGoal.pos_y-currentPlace.pos_y,
								 									 currentGoal.pos_x-currentPlace.pos_x));
						float a2 = (float) Math.toDegrees(Math.atan2(tempCell.pos_y-currentPlace.pos_y,
								 									 tempCell.pos_x-currentPlace.pos_x));
						a2=Math.abs(a2-a1)%360;
						if (a2>180) a2=360-a2;
						if (a2<20){
							// disconnect current
							currentPlace.removePlaceCell(currentGoal);
						}
					}
					
					// change place
					currentPlace=tempCell;
				}
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// update paths
		/////////////////////////////////////////////////////////////////////////////////////////
		for (int i=0;i<paths.size();i++){
			int j=0;
			boolean found=false;
			while (j<paths.get(i).size() && !found){
				if (paths.get(i).get(j)==currentPlace.ident) found=true;
				j++;
			}
			if (found){
				while (paths.get(i).size()>j) paths.get(i).remove (j);
			}
			else paths.get(i).add(currentPlace.ident);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// check for environment update
		/////////////////////////////////////////////////////////////////////////////////////////
		angle2 = -(float) Math.toDegrees(Math.atan2(currentGoal.pos_y-agent.path[0][1],currentGoal.pos_x-agent.path[0][0]))+90;
		float goalDist=(float) agent.body.probe1.distRetinaAbs[(int)(angle2+360)%360]/10;
	
		if (Math.sqrt(agentDist2(currentGoal))>goalDist){	
			
			// remove connection between current place and current goal
			for (int i=0;i<currentPlace.neighbors.size();i++){
				if (dist2(currentGoal,currentPlace.neighbors.get(i))<5){
					currentPlace.neighbors.remove(i);
					i--;
				}
			}
			
			// remove connection between current place and current goal
			if (currentGoal.isPlaceCell){
				for (int i=0;i<currentGoal.neighbors.size();i++){
					if (dist2(currentPlace,currentGoal.neighbors.get(i))<5){
						currentGoal.neighbors.remove(i);
						i--;
					}
				}
			}
			
			currentGoal=getAimedGoal();
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		// exploitation: moving toward a target in the environment
		/////////////////////////////////////////////////////////////////////////////////////////
		
		// find the place that is the closest from the target
		int minId=0;
		float minDist=1000;
		for (int p=0;p<placeList.size();p++){
			placeList.get(p).reset();
			if (targetDist2(placeList.get(p))<minDist){
				minId=p;
				minDist=targetDist2(placeList.get(p));
			}
		}
		targetPlace=placeList.get(minId);
		
		targetPlace.setDistance(0);
		for (int p=0;p<placeList.size();p++) placeList.get(p).setPath();
		
	}
	
	public static float dist2(Cell c1, Cell c2){
		return (c1.pos_x-c2.pos_x)*(c1.pos_x-c2.pos_x) + (c1.pos_y-c2.pos_y)*(c1.pos_y-c2.pos_y);
	}
	public static float dist(Cell c1, Cell c2){
		return (float)Math.sqrt(dist2(c1, c2));
	}
	
	public float agentDist2(Cell c1){
		return (agent.body.position[0]-c1.pos_x)*(agent.body.position[0]-c1.pos_x) 
			 + (agent.body.position[1]-c1.pos_y)*(agent.body.position[1]-c1.pos_y);
	}
	
	public float targetDist2(Cell c1){
		return (agent.body.marker_x-c1.pos_x)*(agent.body.marker_x-c1.pos_x) 
			 + (agent.body.marker_y-c1.pos_y)*(agent.body.marker_y-c1.pos_y);
	}
	
	public float targetDist(Cell c1){
		return (float)Math.sqrt(targetDist2(c1));
	}
	
	public Cell getAimedGoal(){
		
		float angle1=(float) Math.toDegrees(agent.body.position[2]);
		
		float min_angle=181;
		int imin=0;
		for (int g=0;g<currentPlace.neighbors.size();g++){
			float angle2 = (float) Math.toDegrees(Math.atan2(currentPlace.neighbors.get(g).pos_y-agent.path[0][1], currentPlace.neighbors.get(g).pos_x-agent.path[0][0]));
			angle2=Math.abs(angle2-angle1)%360;
			if (angle2>180) angle2=360-angle2;
		    if (angle2<min_angle){
		    	imin=g;
		    	min_angle=angle2;
		    }
		}
		return currentPlace.neighbors.get(imin);
	}
	
	
	/////////////////////////////////////////////////////////////////////
	// compute current map
	private void computeMap(){
		float angle=360/NB_DIRECTIONS;
		for (int d=0;d<NB_DIRECTIONS;d++){
			double average=0;
			for (int a=0;a<angle;a++){
				average+=agent.body.probe1.distRetinaAbs[(int)(d*angle+a)]/15;
			}
			
			average=average/angle;
			
			float max=0;
			float imax=9;
			for (int i=0;i<10;i++){
				currentMap[d][i]=(float) Math.exp(-(i-average)*(i-average)/20);
				if (currentMap[d][i]>max){
					imax=i;
					max=currentMap[d][i];
				}
			}
			for (int i=0;i<10;i++){
				if (i==imax) currentMap2[d][i]=1;
				else currentMap2[d][i]=0;
			}
		}
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
				goals.add(new Cell(px+agent.body.position[0],py+agent.body.position[1],null,null,-1));
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
		
		// remove goals that are too close from a place cell (<45°)
		for (int g1=0;g1<goals.size();g1++){
			if (goals.get(g1).isPlaceCell){
				for (int g2=g1+1;g2<goals.size();g2++){
					if (!goals.get(g2).isPlaceCell){
						float angle1 = (float) Math.toDegrees(Math.atan2(goals.get(g1).pos_y-agent.path[0][1],
																		 goals.get(g1).pos_x-agent.path[0][0]));
						float angle2 = (float) Math.toDegrees(Math.atan2(goals.get(g2).pos_y-agent.path[0][1],
								 										 goals.get(g2).pos_x-agent.path[0][0]));
						angle2=Math.abs(angle2-angle1)%360;
						if (angle2>180) angle2=360-angle2;
						if (angle2<22.5){
							goals.remove(g2);
							if (g1>g2){
								g1--;
								g2--;
							}
							else g2--;
						}
					}
				}
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	// initialization of place cell system
	private void initialize(){

		placeList.add(new PlaceCell(agent.body.position[0], agent.body.position[1], currentMap2, goals, counter));
		counter++;
		currentPlace=placeList.get(0);
		
		paths.add(new ArrayList<Integer>());
		paths.get(paths.size()-1).add(currentPlace.ident);
		
		// look for the current goal
		currentGoal=getAimedGoal();
	}
	
	/////////////////////////////////////////////////////////////////////
	private void loadFile(){
		String fileName = PATH+NAME+".txt";
		String[] elements;

		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			
			// load place cells
			line=br.readLine();

			while (!line.equals("***")){
				elements=line.split(" ");
				placeList.add(new PlaceCell( Float.parseFloat(elements[1]) , Float.parseFloat(elements[2]), Integer.parseInt(elements[0])) );
				int index=3;
				for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){

					for (int j=0;j<10;j++){

						placeList.get(placeList.size()-1).map[i][j]=Float.parseFloat(elements[index]);
						index++;
					}

				}

				line=br.readLine();
			}
			
			// read next line
			line=br.readLine();
			
			// connect goals
			int p=0;
			while (line!=null){
				while (!line.equals("***")){
					elements=line.split(" ");
					
					if (elements.length==1){		// case of a place cell
						int id=Integer.parseInt(elements[0]);
						boolean found=false;
						int p2=0;
						while (p2<placeList.size() && !found){
							if (placeList.get(p2).ident==id) found=true;
							else p2++;
						}
						placeList.get(p).neighbors.add(placeList.get(p2));
					}
					else{							// case of a goal
						placeList.get(p).neighbors.add(new Cell(  Float.parseFloat(elements[0]) , Float.parseFloat(elements[1]), null, null, -1));
					}
					
					line=br.readLine();
				}
				p++; // next place cell
				line=br.readLine();
			}
			
			br.close();
		} 
		catch (Exception e) {
			System.out.println("no file found");
			initialize();
		}
		// look for the current goal
		currentPlace=placeList.get(0);
		currentGoal=getAimedGoal();
		
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	public void save() {
		System.out.println("=====================prepare to save...======================");
		
		// save second level patterns
		
		String fileName = PATH+NAME+".txt";
		
		try {
			PrintWriter file  = new PrintWriter(new FileWriter(fileName));
			
			// save place cell list
			for (int p=0;p<placeList.size();p++){
				
				file.print(placeList.get(p).ident+" "+placeList.get(p).pos_x+" "+placeList.get(p).pos_y+" ");
				for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){
					for (int j=0;j<10;j++){
						file.print(placeList.get(p).map[i][j]+" ");
					}
				}
				file.println();
			}
			
			// separator
			file.println("***");
			
			// save goals
			for (int p=0;p<placeList.size();p++){
				
				for (int g=0;g<placeList.get(p).neighbors.size();g++){
					// if goal is a place cell, just save index
					if (placeList.get(p).neighbors.get(g).isPlaceCell)
						file.println(placeList.get(p).neighbors.get(g).ident);
					else
						file.println(placeList.get(p).neighbors.get(g).pos_x+" "+placeList.get(p).neighbors.get(g).pos_y);
					
				}
				file.println("***");
			}
			file.close();
			System.out.println("memory file saved");
		}
		catch (Exception e) {e.printStackTrace();}
		
	}
	
	
}
