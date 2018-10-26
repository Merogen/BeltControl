package environment;

import java.util.ArrayList;

import sensorySystem.*;



/**
 * representation of a physical agent in its environment
 * @author simon
 *
 */
public class Robot {

	public float[] position;			// position and orientation (x,y,rz)
	
	private int ident;
	private Environment env;
	
	//////////////////////////////////////////////////////////////////////
	// sensors
	public float[] sensors;		// sensors communicate their states
	public int[] display;   	// the robot is equipped with a ring of 8 "leds"
	
	public Probe probe1;
	public ArrayList<Sensor> sensorArray;
	
	//////////////////////////////////////////////////////////////////////
	
	public int marker_x=4;
	public int marker_y=15;
	
	public Robot(int id){
		
		ident=id;
		probe1=new Probe(this,0,0);
		
		// initialization of position vectors
		position=new float[3];
		position[0]=0;
		position[1]=0;
		position[2]=0;
		
		// initialization of sensors
		sensors=new float[8];
		for(int i=0;i<8;i++){
			sensors[i]=0;
		}
		
		// "leds" display
		display=new int[8];
		for(int i=0;i<8;i++){
			display[i]=0;
		}
		
		sensorArray=new ArrayList<Sensor>();
	}
	
	// set or change environment
	public void setEnvironment(Environment e){
		env=e;
		probe1.setEnvironment(e);
	}
	
	public void addSensor(Sensor s){
		sensorArray.add(s);
	}
	
	// enact action given by an agent
	public void move(double[] act){
		
		////////////////////////////////////////////////////////////////////
		// movements
		position[0]+= -act[0]*Math.sin(position[2])+act[1]*Math.cos(position[2]);
		position[1]+=  act[0]*Math.cos(position[2])+act[1]*Math.sin(position[2]);
		
		position[2]+=act[2];
		if (position[2]<0) position[2]+=2*Math.PI;
		if (position[2]>=2*Math.PI) position[2]-=2*Math.PI;
		
		//reinitialize sensors
		for(int k=0;k<6;k++){
			sensors[k]=0;
		}
		probe1.rendu();
		
		////////////////////////////////////////////////////////////////////
		// collision detection
		
		int i=Math.round(position[0]);
		int j=Math.round(position[1]);
		
		for (int w=-1;w<=1;w++){
			for (int h=-1;h<=1;h++){
				if (i!=0 && j!=0){
					
					if (i+w>=0 && i+w<env.getWidth() && j+h>=0 && j+h<env.getHeight()){
						if (!env.isWalkthroughable(i+w,j+h)){
							// case lateral block
							if (w==0){
								if ((j+h - position[1])*h<1){
									position[1]-= h*(1 - h*(j+h - position[1]));
									sensors[0]=1;
								}
							}
							else{ 
								if (h==0){
									if ((i+w - position[0])*w<1){
										position[0]-= w*(1 - w*(i+w - position[0]));
										sensors[0]=1;
									}
								}
								else{
									// case corner block
									float x2= (i+w)-w*0.5f;
									float y2= (j+h)-h*0.5f;
									
									double dist=Math.sqrt((position[0]-x2)*(position[0]-x2)+(position[1]-y2)*(position[1]-y2));

									if (dist<0.5){
										sensors[0]=1;
										position[0]+= (position[0]-x2) * (0.5-(dist-0.05)) /0.5;
										position[1]+= (position[1]-y2) * (0.5-(dist-0.05)) /0.5;
									}
								}
							}
						}
					}
				}
			}
		}	
		
		if (sensors[0]==1){
			try{Thread.currentThread();
			Thread.sleep(2);}
			catch(Exception ie){}
		}
		
		////////////////////////////////////////////////////////////////////
		// feel neighbor blocks with "antennas"
		display[0]=0;
		display[1]=0;
		display[2]=0;
		display[3]=0;
		if (act[3]==1){                    // feel front
			if (position[2]>=  Math.PI/4 && position[2]<3*Math.PI/4){
				if (env.isWall(i,j+1)) sensors[2]=1;
			}
			if (position[2]>=5*Math.PI/4 && position[2]<7*Math.PI/4){
				if (env.isWall(i,j-1)) sensors[2]=1;
			}
			if (position[2]>=7*Math.PI/4 || position[2]<  Math.PI/4){
				if (env.isWall(i+1,j)) sensors[2]=1;
			}
			if (position[2]>=3*Math.PI/4 && position[2]<5*Math.PI/4){
				if (env.isWall(i-1,j)) sensors[2]=1;
			}
			if (sensors[2]==0) display[0]=2;
			else display[0]=1;
		}
		if (act[4]==1){                    // feel left
			if (position[2]>=  Math.PI/4 && position[2]<3*Math.PI/4){
				if (env.isWall(i-1,j)) sensors[3]=1;
			}
			if (position[2]>=5*Math.PI/4 && position[2]<7*Math.PI/4){
				if (env.isWall(i+1,j)) sensors[3]=1;
			}
			if (position[2]>=7*Math.PI/4 || position[2]<  Math.PI/4){
				if (env.isWall(i,j+1)) sensors[3]=1;
			}
			if (position[2]>=3*Math.PI/4 && position[2]<5*Math.PI/4){
				if (env.isWall(i,j-1)) sensors[3]=1;
			}
			if (sensors[3]==0) display[1]=2;
			else display[1]=1;
		}
		if (act[5]==1){                    // feel right
			if (position[2]>=  Math.PI/4 && position[2]<3*Math.PI/4){
				if (env.isWall(i+1,j)) sensors[4]=1;
			}
			if (position[2]>=5*Math.PI/4 && position[2]<7*Math.PI/4){
				if (env.isWall(i-1,j)) sensors[4]=1;
			}
			if (position[2]>=7*Math.PI/4 || position[2]<  Math.PI/4){
				if (env.isWall(i,j-1)) sensors[4]=1;
			}
			if (position[2]>=3*Math.PI/4 && position[2]<5*Math.PI/4){
				if (env.isWall(i,j+1)) sensors[4]=1;
			}
			if (sensors[4]==0) display[2]=2;
			else display[2]=1;
		}
		if (act[6]==1){                    // feel back
			if (position[2]>=  Math.PI/4 && position[2]<3*Math.PI/4){
				if (env.isWall(i,j-1)) sensors[5]=1;
			}
			if (position[2]>=5*Math.PI/4 && position[2]<7*Math.PI/4){
				if (env.isWall(i,j+1)) sensors[5]=1;
			}
			if (position[2]>=7*Math.PI/4 || position[2]<  Math.PI/4){
				if (env.isWall(i-1,j)) sensors[5]=1;
			}
			if (position[2]>=3*Math.PI/4 && position[2]<5*Math.PI/4){
				if (env.isWall(i+1,j)) sensors[5]=1;
			}
			if (sensors[5]==0) display[3]=2;
			else display[3]=1;
		}
		
		if (act[3]>0 || act[4]>0 || act[5]>0 || act[6]>0){
			try{Thread.currentThread();
			Thread.sleep(1);}
			catch(Exception ie){}
		}
		
		// update sensors
	}
	
	// round agent position. Used for discrete environments
	public void center(){
		position[0]=Math.round(position[0]);
		position[1]=Math.round(position[1]);
		
		if (position[2]>=  Math.PI/4 && position[2]<3*Math.PI/4){
			position[2]=(float) (  Math.PI/2);		
		}
		if (position[2]>=3*Math.PI/4 && position[2]<5*Math.PI/4){
			position[2]=(float) (  Math.PI  );
		}
		if (position[2]>=5*Math.PI/4 && position[2]<7*Math.PI/4){
			position[2]=(float) (3*Math.PI/2);		
		}
		if (position[2]>=7*Math.PI/4 || position[2]<  Math.PI/4){
			position[2]= 0;
		}
	}
	
	// detect and remove preys
	public void eatFish(){
		
		// feel block under the agent
		for (int i=-1;i<=1;i++){
			for (int j=-1;j<=1;j++){
				// compute distance of surrounding preys
				if (env.isFood(Math.round(position[0])+i,Math.round(position[1])+j)){
					double d=Math.sqrt( (Math.round(position[0])+i-position[0] )*(Math.round(position[0])+i-position[0] )
							           +(Math.round(position[1])+j-position[1] )*(Math.round(position[1])+j-position[1] ));
					
					if (d<=0.8){
						sensors[2]=1;
						env.setBlock(Math.round(position[0])+i,Math.round(position[1])+j,Environment.empty);
						
						// randomly place a new agent
						int rx=(int) (Math.random()*env.getWidth());
						int ry=(int) (Math.random()*env.getHeight());
						
						while (!env.isEmpty(rx,ry)){
							rx=(int) (Math.random()*env.getWidth());
							ry=(int) (Math.random()*env.getHeight());
						}
						env.setBlock(rx,ry,Environment.fish);
					}
				}
				
				// replace marker
				if (env.isEmpty(Math.round(position[0])+i,Math.round(position[1])+j) && env.isVisible(Math.round(position[0])+i,Math.round(position[1])+j)){
					double d=Math.sqrt( (Math.round(position[0])+i-position[0] )*(Math.round(position[0])+i-position[0] )
							           +(Math.round(position[1])+j-position[1] )*(Math.round(position[1])+j-position[1] ));
					
					if (d<=0.8){
						env.setBlock(Math.round(position[0])+i,Math.round(position[1])+j,Environment.empty);
						
						// randomly place a new marker
						int rx=(int) (Math.random()*env.getWidth());
						int ry=(int) (Math.random()*env.getHeight());
						
						while (!env.isEmpty(rx,ry)){
							rx=(int) (Math.random()*env.getWidth());
							ry=(int) (Math.random()*env.getHeight());
						}
						env.setBlock(rx,ry,Environment.marker);
						
						marker_x=rx;
						marker_y=ry;
					}
				}
			}
		}
	}
	
	
	public float getSensor(int i){
		return sensorArray.get(i).getValue();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////
	
	public float[] getPosition(){
		return position;
	}
	public void setPosition(float[] p){
		position[0]=p[0];
		position[1]=p[1];
		position[2]=p[2];
	}
	
	
	public int getId(){
		return ident;
	}
	
	public Environment getEnvironment(){
		return env;
	}

}
