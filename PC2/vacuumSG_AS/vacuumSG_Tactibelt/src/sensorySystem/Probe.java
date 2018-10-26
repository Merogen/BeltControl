package sensorySystem;

import java.awt.Color;

import environment.Environment;
import environment.Robot;

/**
 * sensory system that collect environment information
 * @author simon
 *
 */
public class Probe {

	private Environment m_env;
	private Robot robot;
	
	public double[] distRetina;
	public Color[] colRetina;
	public int[] tMap;
	public int[] corner;
	
	public double[] distRetinaAbs;
	
	private float offsetX=0;
	private float offsetY=0;
	
	public Probe(Robot r, float posX, float posY){
		robot=r;
		offsetX=posX;
		offsetY=posY;
		
		distRetina=new double[360];
		colRetina=new Color[360];
		tMap=new int[360];
		corner=new int[360];
		
		distRetinaAbs=new double[360];
	}
	
	public void setEnvironment(Environment e){
		m_env=e;
	}
	

	/**
	 * rendu function, draw the visual and tactile retina
	 * @return
	 */
	public void rendu(){
		double[] rv    = new double[360];          // visual distance vector (absolute orientation)
		double[] rv2   = new double[360];          // visual distance vector (agent orientation)
		double[] rt    = new double[360];          // tactile distance vector (absolute orientation)
		double[] rt2   = new double[360];          // tactile distance vector (agent orientation)
		
		double[] zVMap = new double[360];          // visual  Z-Map
		double[] zTMap = new double[360];          // tactile Z-Map
		
		Color[] colorMap =new Color[360];          // color vector (absolute orientation)
		int[] tactileMap =new int[360];            // tactile property vector (absolute orientation)
		
		//int[] cornerV = new int[360];              // allocentric visual corner vector
		int[] cornerT = new int[360];              // allocentric tactile corner vector
		
		
		double d=0;
		double d1,d2,d3;                   // distance of corners of a square
		double a1, a2, a3;                 // angles of corners of a square (in degree)
		int ai1,ai2,ai3;
		
		double imin,iplus,jmin,jplus;
		double imin2,jmin2;
		
		float m_w=m_env.getWidth();
		float m_h=m_env.getHeight();
		
		float ax=robot.position[0];
		float ay=robot.position[1];
		double theta=robot.position[2];
		
		float offX = (float)(offsetX * Math.cos(theta-Math.PI/2) - offsetY * Math.sin(theta-Math.PI/2));
		float offY =-(float)(offsetX * Math.sin(theta-Math.PI/2) - offsetY * Math.cos(theta-Math.PI/2));
		
		int Im_x=Math.round(robot.position[0]+offX);
		int Im_y=Math.round(robot.position[1]+offY);
		
		ax+=offX;
		ay+=offY;

		// reset vectors
		for (int i=0;i<360;i++){
			zVMap[i]=2000;
			zTMap[i]=2000;
			rv[i]=2000;
			rt[i]=2000;
			colorMap[i]=new Color(0,0,0);
			tactileMap[i]=0;
		}
		
		int sight=20;                                              // maximum distance
		
		
		// the area around the agent is divided into five parts
		// 4 4 4 4 5 1 1 1 1         block corner number :
		// 4 4 4 4 5 1 1 1 1         area 1 :     area 2 :    area 3 :   area 4 :    area 5 :
		// 4 4 4 4 5 1 1 1 1         2-----       1----3      3----1     -----3      ------
		// 3 3 3 3 A 1 1 1 1         |    |       |    |      |    |     |    |      |    |
		// 3 3 3 3 2 2 2 2 2         1----3       2-----      -----2     2----1      2----1
		// 3 3 3 3 2 2 2 2 2
		// 3 3 3 3 2 2 2 2 2

		// the five parts are computed in parallel. Only area 1 is commented, other areas are computed in the same way
		for (int i=0;i<sight;i++){
			for (int j=0;j<sight;j++){
				
				float Im_xpi=Im_x+i;
				float Im_ypj=Im_y+j;
				
				float Im_xmi=Im_x-i;
				float Im_ymj=Im_y-j;
				
				// (1) cells on the top right side
				if ( (i>0)&& (Im_xpi>=0) && (Im_xpi<m_w) && (Im_ypj>=0) && (Im_ypj<m_h) ){
					if (!m_env.isEmpty(Im_xpi,Im_ypj) ){
						// determine color and tactile property of a block
						Color bgc = m_env.seeBlock(Im_xpi,Im_ypj);
						int tactile=m_env.touchBlock(Im_xpi,Im_ypj);
						
						// determine the position of the three visible points of the block in polar reference
						imin =(double)i-0.5 - (ax-Im_x);
						imin2=imin*imin;
						iplus=(double)i+0.5 - (ax-Im_x);
						jmin =(double)j-0.5 - (ay-Im_y);
						jmin2=jmin*jmin;
						jplus=(double)j+0.5 - (ay-Im_y);
						
						// determine distance of these points
						d1=  imin2 + jmin2;
						d1=Math.sqrt(d1);
						d2=  imin2 + (jplus*jplus);
						d2=Math.sqrt(d2);
						d3=  (iplus*iplus) + jmin2;
						d3=Math.sqrt(d3);
						
						// compute angles in degrees of these points
						a1=  Math.toDegrees( Math.acos( jmin/d1));
						a2=  Math.toDegrees( Math.acos( jplus/d2));
						a3=  Math.toDegrees( Math.acos( jmin/d3));
						
				    	ai1=(int)a1;
				    	ai2=(int)a2;
				    	ai3=(int)a3;
						
				    	// fill the output vectors with the first visible segment (1-2)
						for (int k=ai2;k<=ai1;k++){
							//d=10* imin/Math.cos((double)(90-k)*Math.PI/180);
							d= d2*10 +   (d1-d2)*10*(k-ai2)/(ai1-ai2);
							
							// visual vector if the block is visible
							if (m_env.isVisible(Im_xpi,Im_ypj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]= d;				   // fill Z-Map
									colorMap[k]=bgc;
								}
							}
							// tactile vector
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]= d;                       // fill Z-Map
								tactileMap[k]=tactile;
								if      (k==ai2) cornerT[k]=1;
								else if (k==ai1) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}
						
						// fill the output vectors with the second visible segment (1-3) (if visible)
						if (imin>0){
						for (int k=ai1;k<=ai3;k++){
							//d=10* jmin/Math.cos((k)*Math.PI/180);							
							d= d1*10 +   (d3-d1)*10*(k-ai1)/(ai3-ai1);
							
							// visual vector if the block is visible
							if (m_env.isVisible(Im_xpi,Im_ypj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]= d;
									colorMap[k]=bgc;
								}
							}
							// tactile vector
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]= d;
								tactileMap[k]=tactile;
								if      (k==ai1) cornerT[k]=1;
								else if (k==ai3) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}
						}
					}
				}

				// (2) cells on the bottom right side
				if ( (j>0) && (Im_xpi>=0) && (Im_xpi<m_w) && (Im_ymj>=0) &&  (Im_ymj<m_h)){
					if (!m_env.isEmpty(Im_xpi,Im_ymj) ){
						Color bgc = m_env.seeBlock(Im_xpi,Im_ymj);
						int tactile=m_env.touchBlock(Im_xpi,Im_ymj);

						imin =(double)i-0.5 - (ax-Im_x);
						imin2=imin*imin;
						iplus=(double)i+0.5 - (ax-Im_x);
						jmin =(double)j-0.5 + (ay-Im_y);
						jmin2=jmin*jmin;
						jplus=(double)j+0.5 + (ay-Im_y);
						
						d1=  imin2 + jmin2;
						d1=Math.sqrt(d1);
						d2=  (iplus*iplus) + jmin2;
						d2=Math.sqrt(d2);
						d3=  imin2 + (jplus*jplus);
						d3=Math.sqrt(d3);
						
						a1=  Math.toDegrees( Math.acos( jmin/d1));
						a2=  Math.toDegrees( Math.acos( jmin/d2));
						a3=  Math.toDegrees( Math.acos( jplus/d3));
				    	
				    	if (i-0.5>=0){
				    		ai1=180-(int)a1;
				    		ai3=180-(int)a3;
				    	}
				    	else{
				    		ai1=180+(int)a1;
				    		ai3=180+(int)a3;
				    	}
				    	ai2=180-(int)a2;
						
						for (int k=ai2;k<=ai1;k++){
							d= ( d2*10 +   (d1-d2)*10*(k-ai2)/(ai1-ai2));
							if (m_env.isVisible(Im_xpi,Im_ymj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]= d;
									colorMap[k]=bgc;
								}
							}
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]= d;
								tactileMap[k]=tactile;
							
								if      (k==ai1) cornerT[k]=1;
								else if (k==ai2) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}		
						for (int k=ai1;k<=ai3;k++){
							d= ( d1*10 +   (d3-d1)*10*(k-ai1)/(ai3-ai1));
							if (m_env.isVisible(Im_xpi,Im_ymj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]= d;
									colorMap[k]=bgc;
								}
							}
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]= d;
								tactileMap[k]=tactile;
							
								if      (k==ai1) cornerT[k]=1;
								else if (k==ai3) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}
					}
				}
				
				
				// (3) cells on the bottom left side
				if ( (i>0) && (Im_xmi>=0) && (Im_xmi<m_w) && (Im_ymj>=0) && (Im_ymj<m_h) ){
					if (!m_env.isEmpty(Im_xmi,Im_ymj) ){
						Color bgc = m_env.seeBlock(Im_xmi,Im_ymj);
						int tactile=m_env.touchBlock(Im_xmi,Im_ymj);
						
						imin =(double)i-0.5 + (ax-Im_x);
						imin2=imin*imin;
						iplus=(double)i+0.5 + (ax-Im_x);
						jmin =(double)j-0.5 + (ay-Im_y);
						jmin2=jmin*jmin;
						jplus=(double)j+0.5 + (ay-Im_y);
						
						d1=  imin2 + jmin2;
						d1=Math.sqrt(d1);
						d2=  imin2 + (jplus*jplus);
						d2=Math.sqrt(d2);
						d3=  (iplus*iplus) + jmin2;
						d3=Math.sqrt(d3);
						
						a1=  Math.toDegrees( Math.acos( jmin/d1));
						a2=  Math.toDegrees( Math.acos( jplus/d2));
						a3=  Math.toDegrees( Math.acos( jmin/d3));
						
				    	ai1=180+(int)a1;
				    	ai2=180+(int)a2;
				    	ai3=180+(int)a3;
						
						for (int k=ai2;k<=ai1;k++){
							d=   d2*10 +   (d1-d2)*10*(k-ai2)/(ai1-ai2);
							if (m_env.isVisible(Im_xmi,Im_ymj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]=d;
									colorMap[k]=bgc;
								}
							}
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]=d;
								tactileMap[k]=tactile;
								if      (k==ai2) cornerT[k]=1;
								else if (k==ai1) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}		
						for (int k=ai1;k<=ai3;k++){
							d=  d1*10 +   (d3-d1)*10*(k-ai1)/(ai3-ai1);
							if (m_env.isVisible(Im_xmi,Im_ymj)){
								if (zVMap[k]>d){
									rv[k]=d;
									zVMap[k]=d;
									colorMap[k]=bgc;
								}
							}
							if (zTMap[k]>d){
								rt[k]=d;
								zTMap[k]=d;
								tactileMap[k]=tactile;
								if      (k==ai1) cornerT[k]=1;
								else if (k==ai3) cornerT[k]=2;
								else             cornerT[k]=0;
							}
						}
					}
				}
				
				// (4) cells on the top left side
				if ( (j>0) && (i>0) && (Im_xmi>=0) && (Im_xmi<m_w) && (Im_ypj>=0) && (Im_ypj<m_h) ){
					if (!m_env.isEmpty(Im_xmi,Im_ypj) ){
						Color bgc = m_env.seeBlock(Im_xmi,Im_ypj);
						int tactile=m_env.touchBlock(Im_xmi,Im_ypj);
						
						imin =(double)i-0.5 + (ax-Im_x);
						imin2=imin*imin;
						iplus=(double)i+0.5 + (ax-Im_x);
						jmin =(double)j-0.5 - (ay-Im_y);
						jmin2=jmin*jmin;
						jplus=(double)j+0.5 - (ay-Im_y);
						
						d1=  imin2 + jmin2;
						d1=Math.sqrt(d1);
						d2=  (iplus*iplus) + jmin2;
						d2=Math.sqrt(d2);
						d3=  imin2 + (jplus*jplus);
						d3=Math.sqrt(d3);
						
						a1=  Math.toDegrees( Math.acos( jmin/d1));
						a2=  Math.toDegrees( Math.acos( jmin/d2));
						a3=  Math.toDegrees( Math.acos( jplus/d3));
						
						ai1=360-(int)a1;
						ai3=360-(int)a3;
						if (ai1==360) ai1=359;
						if (ai3==360) ai3=359;
				    	ai2=360-(int)a2;
						
				    	for (int k=ai2;k<=ai1;k++){
				    		d= d2*10 +   (d1-d2)*10*(k-ai2)/(ai1-ai2);
				    		if (m_env.isVisible(Im_xmi,Im_ypj)){
				    			if (zVMap[k]>d){
				    				rv[k]=d;
				    				zVMap[k]= d;
				    				colorMap[k]=bgc;
				    			}
				    		}
				    		if (zTMap[k]>d){
			    				rt[k]=d;
			    				zTMap[k]= d;
			    				tactileMap[k]=tactile;
			    				if      (k==ai2) cornerT[k]=1;
			    				else if (k==ai1) cornerT[k]=2;
			    				else             cornerT[k]=0;
			    			}
				    	}		
				    	for (int k=ai1;k<=ai3;k++){
				    		d= d1*10 +   (d3-d1)*10*(k-ai1)/(ai3-ai1);
				    		if (m_env.isVisible(Im_xmi,Im_ypj)){
				    			if (zVMap[k]>d-0.01){
				    				rv[k]=d;
				    				zVMap[k]=d;
				    				colorMap[k]=bgc;
				    			}
				    		}
				    		if (zTMap[k]>d-0.01){
			    				rt[k]=d;
			    				zTMap[k]=d;
			    				tactileMap[k]=tactile;
			    				if      (k==ai1) cornerT[k]=1;
			    				else if (k==ai3) cornerT[k]=2;
			    				else             cornerT[k]=0;
			    			}
				    	}
					}
				}
				
				// (5) cells exactly on the top
				// In this case, there is only two visible points and one visible segment
				if ( (j>0) && (i==0) && (Im_ypj>=0) && (Im_ypj<m_h) && (Im_xmi>=0) && (Im_xmi<m_w) ){
					if (!m_env.isEmpty(Im_xmi,Im_ypj) ){
						Color bgc = m_env.seeBlock(Im_xmi,Im_ypj);
						int tactile=m_env.touchBlock(Im_xmi,Im_ypj);
						
						imin =(double)i-0.5 + (ax-Im_x);
						imin2=imin*imin;
						iplus=(double)i+0.5 + (ax-Im_x);
						jmin =(double)j-0.5 - (ay-Im_y);
						jmin2=jmin*jmin;
						
						d1=  imin2 + jmin2;
						d1=Math.sqrt(d1);
						d2=  (iplus*iplus) + jmin2;
						d2=Math.sqrt(d2);
						
						a1=  Math.toDegrees( Math.acos( jmin/d1));
						a2=  Math.toDegrees( Math.acos( jmin/d2));

						ai1=(int)a1;
				    	ai2=360-(int)a2;
				    	if (ai2==360) ai2=359;
				    	
				    	int count=0;
				    	for (int k=ai2;k<360;k++){
				    		d= d2*10 +   (d1-d2)*10*(k-ai2)/((ai1-ai2+360)%360);
				    		if (m_env.isVisible(Im_xmi,Im_ypj)){
				    			if (zVMap[k]>d){
				    				rv[k]=d;
				    				zVMap[k]= d;
				    				colorMap[k]=bgc;
				    			}
				    		}
				    		if (zTMap[k]>d){
			    				rt[k]=d;
			    				zTMap[k]= d;
			    				tactileMap[k]=tactile;
			    				if      (k==ai2) cornerT[k]=1;
			    				else             cornerT[k]=0;
			    			}
				    		count++;
				    	}
				    	for (int k=0;k<=ai1;k++){
				    		d= d2*10 +   (d1-d2)*10*(k+count)/((ai1-ai2+360)%360);
				    		if (m_env.isVisible(Im_xmi,Im_ypj)){
				    			if (zVMap[k]>d){
				    				rv[k]=d;
				    				zVMap[k]= d;
				    				colorMap[k]=bgc;
				    			}
				    		}
				    		if (zTMap[k]>d){
			    				rt[k]=d;
			    				zTMap[k]= d;
			    				tactileMap[k]=tactile;
			    				if (k==ai1) cornerT[k]=2;
			    				else        cornerT[k]=0;	
			    			}
				    	}
					}
				}
				
				
			}
		}
		
		
		
		// agents detection
		for (int a=0;a<m_env.platform.nbAgent();a++){
			// for each other agent (agent or fish)
			if (m_env.platform.getAgent(a).getIdent()!=robot.getId()){
				// compute distance
				d= (ax-m_env.platform.getAgent(a).body.position[0])*(ax-m_env.platform.getAgent(a).body.position[0])
				  +(ay-m_env.platform.getAgent(a).body.position[1])*(ay-m_env.platform.getAgent(a).body.position[1]);
				d=Math.sqrt(d);
				
				// compute angle
				int ai4=0;
				if (ax-m_env.platform.getAgent(a).body.position[0]<=0){
					a1=Math.toDegrees( Math.acos( (ay-m_env.platform.getAgent(a).body.position[1])/d));
					ai1=180-(int)a1;
				}
				else{
					a1=Math.toDegrees( Math.acos( (ay-m_env.platform.getAgent(a).body.position[1])/d));
					ai1=(int)a1+180;
				}
				
				// compute border angles
				a2=Math.atan(0.4/d);
				a2=Math.toDegrees(a2);
				
				ai2= (int)a2;
				
				ai3=ai1-ai2+360;
				ai4=ai1+ai2+360;
				
				for (int k=ai3;k<=ai4;k++){
					if (zVMap[k%360]>d*10){
						rv[k%360]=d*10 ;
						zVMap[k%360]= d*10 ;
						colorMap[k%360]=Environment.AGENT_COLOR;
					}
					
					if (zTMap[k%360]>d*10){
						rt[k%360]=d*10 ;
						zTMap[k%360]= d*10 ;
					}
				}
			}
		}
		
		// correction of the distance vector (d is supposed constant for each points between 2 corners)
		int j=0;
		double x,y;
		for (int i=0;i<360;i++){
			j=(int) rv2[(359-i+180)%360]*2;
			double angle =theta + (double)i*Math.PI/180;
			boolean found=false;
			while (j>0 && !found){
				x= ax + ((double)j/20)*Math.cos(angle);
				y= ay + ((double)j/20)*Math.sin(angle);
				if (Math.round(x)>=0 && Math.round(y)>=0 && Math.round(x)<m_w && Math.round(y)<m_h){
					if (m_env.seeBlock(Math.round(x),Math.round(y)).equals(Environment.FIELD_COLOR)){
						found=true;
						rv2[(359-i+180)%360]=(double)j/2;
						rt2[(359-i+180)%360]=(double)j/2;
					}
				}
				else found=true;
				j--;
				
			}
		}

		
		// fill the output vectors (agent orientation)
		int orientationDeg= (int)(theta * 180 / Math.PI); // compute offset
		for (int i=0;i<360;i++){
			int offset=(i-orientationDeg+630)%360; // 630 = 2 X 360 -90
			distRetina[i]=rv[offset];
			colRetina[i] =colorMap[offset];
			tMap[i]=tactileMap[offset];
			corner[i]=cornerT[offset];
			
			distRetinaAbs[i]=rv[i];
		}

	}
	
}
