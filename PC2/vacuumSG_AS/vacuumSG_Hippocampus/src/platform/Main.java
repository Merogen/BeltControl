package platform;

import java.util.ArrayList;

import display.EnvFrame;

import environment.Environment;
import platform.Agent;



public class Main {

	public Environment env;
	private ArrayList<EnvFrame> frameList;
	private ArrayList<Agent> agentList;
	
	private boolean run=true;
	
	public Main(){
		
		agentList=new ArrayList<Agent>();
		frameList=new ArrayList<EnvFrame>();
		env=new Environment(this);
		
		new UserInterfaceFrame(this);
		
		// main loop of simulation
		while (run){
			int nb=nbAgent();
			if (nbAgent()>0){
				selectAgent(0).setDisplay();
			}
	
			for (int i=0;i<nb;i++){
				selectAgent(i).start();
			}
			
			boolean end=false;
			while (!end){
	
				env.drawGrid();
				
				for (int i=0;i<frameList.size();i++){
					frameList.get(i).repaint();
				}

				try{Thread.currentThread();
				Thread.sleep(10);}
				catch(Exception ie){}
				
				end=true;
				for (int i=0;i<nbAgent();i++){
					if (selectAgent(i).isAlive()) end=false;
				}
			}
		}

	}
	
	
	////////////////////////////////////////////////////////
	
	public void addAgent(int ident){
		agentList.add(new Agent(ident,this));
	}
	public void resetAgentList(){
		for (int i=0;i<nbAgent();i++){
			getAgent(i).stopThread();
		}
		agentList.clear();
	}
	public int nbAgent(){
		return agentList.size();
	}
	
	public Agent selectAgent(int index){
		if (index<nbAgent()) return agentList.get(index);
		else              return null;
	}
	
	public Agent getAgent(int i){
		return agentList.get(i);
	}
	
	public Agent lastAgent(){
		return agentList.get(nbAgent()-1);
	}

	public void agentPosition(float[] p){
		lastAgent().setPosition(p);
	}
	
	public int agentId(float x, float y){
		int ident=-1;
		float min=2;
		float d=0;
		
		for (int i=0;i<nbAgent();i++){
			d= (selectAgent(i).body.position[0] - x)*(selectAgent(i).body.position[0] - x)
			  +(selectAgent(i).body.position[1] - (env.getHeight()-y))*(selectAgent(i).body.position[1] - (env.getHeight()-y));
			d=(float) Math.sqrt(d);
			
			if (d<=2 && d<min){
				min=d;
				ident=selectAgent(i).getIdent();
			}
		}
		return ident;
	}
	
	public int agentIndex(float x, float y){
		int index=-1;
		float min=2;
		float d=0;
		
		for (int i=0;i<nbAgent();i++){
			d= (selectAgent(i).body.position[0] - x)*(selectAgent(i).body.position[0] - x)
			  +(selectAgent(i).body.position[1] - (env.getHeight()-y))*(selectAgent(i).body.position[1] - (env.getHeight()-y));
			d=(float) Math.sqrt(d);
			
			if (d<=2 && d<min){
				min=d;
				index=i;
			}
		}
		return index;
	}
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
	public int agentIndex(int id){
		int index=-1;
		
		for (int i=0;i<nbAgent();i++){
			if (selectAgent(i).getIdent()==id) index=i;
		}
		
		return index;
	}
	
	public boolean isStopped(){
		int nb=nbAgent();
		boolean test=true;
		for (int i=0;i<nb;i++){
			if (selectAgent(i).state()==1) test=false;
		}
		return test;
	}
	
	public boolean isStopped(int index){
		return (selectAgent(index).state()==0);
	}
	
	public boolean isStarted(){
		int nb=nbAgent();
		boolean test=true;
		for (int i=0;i<nb;i++){
			if (selectAgent(i).state()==0) test=false;
		}
		return test;
	}
	
	public boolean isStarted(int index){
		return (selectAgent(index).state()==1);
	}
	
	public int getAgentIndex(){
		return env.getIndex();
	}
	
	public void addDisplay(EnvFrame frame){
		frameList.add(frame);
	}
	
	public int displaySize(){
		return frameList.size();
	}
	
	public EnvFrame getDisplay(int i){
		return frameList.get(i);
	}
	
	///////////////////////////////////////////////////////
	public void run(){
		run=true;
	}
	public void stop(){
		run=false;
	}
	
	///////////////////////////////////////////////////////
	
	public Environment getEnv(){
		return env;
	}
	
	///////////////////////////////////////////////////////
	// main function
	public static void main(String[] args){
		new Main();
	}
	
}

