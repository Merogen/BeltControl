
public class Main {

	public static int LENGTH=500;
	
	public static float ALPHA=0.5f;
	public static float BETA=0.5f;
	
	public Interface inter;
	private ImageFrame frame;
	
	public Image image;
	
	public float jx=0;
	public float jy=0;
	
	public float x=0;
	public float y=0;
	public float dx=0;
	public float dy=0;
	
	public float friction_fluid=0;
	public float friction_solid=0;
	public float gx=0;
	public float gy=0;
	
	public int flowX=0;
	public int flowY=0;
	
	public float[][] trace;
	public int time;
	public int count;
	
	
	int previous_fluid=0;
	int previous_solid=0;
	int previous_gx=0;
	int previous_gy=0;
	
	int previous_flowX=0;
	int previous_flowY=0;
	
	public float[] gauss;
	
	public Main(){
		
		image=new Image();
		
		inter=new Interface(this);
		frame=new ImageFrame(this);
		
		time=0;
		count=0;
		trace=new float[LENGTH][2];
		
		gauss=new float[9];
		gauss[0]=0.018f;//-4
		gauss[1]=0.1f;	//-3
		gauss[2]=0.37f;	//-2
		gauss[3]=0.78f;	//-1
		gauss[4]=1; 	// 0
		gauss[5]=0.78f;	// 1
		gauss[6]=0.37f;	// 2
		gauss[7]=0.1f;	// 3
		gauss[8]=0.018f;// 4

		while (true){
			
			while (!inter.ready){
				try {Thread.sleep(1);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			
			jx=inter.joystickX;
			jy=inter.joystickY;
			
			dx=jx/500;
			dy=jy/500;
			
			//System.out.println(" +++ "+inter.joystickX+" ; "+inter.joystickY);
			inter.ready=false;
			

			
			/*inter.interaction((float)Math.sqrt(dx*dx+dy*dy), gx, gy, friction_fluid, friction_solid);
			frame.repaint();
			//System.out.println(x+" ; "+y+"  ;  "+dx+" ; "+dy);
			*/
			
			friction_fluid=image.image[350+(int)(x+dx)][270-(int)(y+dy)][0]/255;
			friction_solid=image.image[350+(int)(x+dx)][270-(int)(y+dy)][2]/255;
			
			friction_fluid=(int)(friction_fluid*450);
			
			if (friction_fluid!=previous_fluid){
				String msg="f";
				if (friction_fluid<10) msg+="00";
				else if (friction_fluid<100) msg+="0";
				msg+=(int)friction_fluid;
				inter.sendMsg(msg);
				
				previous_fluid=(int) friction_fluid;
			}
			
			friction_solid=(int)(friction_solid*450);
			
			if (friction_solid!=previous_solid){
				String msg="s";
				if (friction_solid<10) msg+="00";
				else if (friction_solid<100) msg+="0";
				msg+=(int)friction_solid;
				inter.sendMsg(msg);
				
				previous_solid=(int) friction_solid;
			}/**/
			
			
			gx=0;
			gy=0;
			float sum=0;
			
			float val=0;
			
			for (int i=1;i<3;i++){
				
				//val=((float)image.image[350+(int)(x+dx+i)][270-(int)(y+dy)][1]-(float)image.image[350+(int)(x+dx)][270-(int)(y+dy)][1])*10/255f/i;
				//if (Math.abs(val)>Math.abs(gx)) gx=val;
				
				//val=((float)image.image[350+(int)(x+dx)][270-(int)(y+dy)][1]-(float)image.image[350+(int)(x+dx-i)][270-(int)(y+dy)][1])*10/255f/i;
				//if (Math.abs(val)>Math.abs(gx)) gx=val;
				
				
				gx+=((float)image.image[350+(int)(x+i)][270-(int)(y)][1]-(float)image.image[350+(int)(x)][270-(int)(y)][1])/i;
				gx+=((float)image.image[350+(int)(x)][270-(int)(y)][1]-(float)image.image[350+(int)(x-i)][270-(int)(y)][1])/i;
				
				//val=((float)image.image[350+(int)(x+dx)][270-(int)(y+dy+i)][1]-(float)image.image[350+(int)(x+dx)][270-(int)(y+dy)][1])*10/255f/i;
				//if (Math.abs(val)>Math.abs(gy)) gy=val;
				
				//val=((float)image.image[350+(int)(x+dx)][270-(int)(y+dy)][1]-(float)image.image[350+(int)(x+dx)][270-(int)(y+dy-i)][1])*10/255f/i;
				//if (Math.abs(val)>Math.abs(gy)) gy=val;
				
				gy+=((float)image.image[350+(int)(x)][270-(int)(y+i)][1]-(float)image.image[350+(int)(x)][270-(int)(y)][1])/i;
				gy+=((float)image.image[350+(int)(x)][270-(int)(y)][1]-(float)image.image[350+(int)(x)][270-(int)(y-i)][1])/i;
				sum+=1/i;
			}
			
			if (gx!=0 || gy!=0) System.out.println(gx+" , "+gy);
			
			gx=gx/sum;
			gy=gy/sum;
			
			//gx+=((float)image.image[350+(int)(x+3)][270-(int)(y)][1]-(float)image.image[350+(int)(x-3)][270-(int)(y)][1]);
			//gy+=((float)image.image[350+(int)(x)][270-(int)(y+3)][1]-(float)image.image[350+(int)(x)][270-(int)(y-3)][1]);
			
			
			gx=gx/255f;
			gy=gy/255f;
			
			//gx=Math.max(1, Math.min(999, gx*2f/255f));
			
			//gy=Math.max(1, Math.min(999, gy*2f/255f));
			
			gx= (int)(gx*500)+500;
			gy= (int)(gy*500)+500;
			
			if (gx>=1000) gx=999;
			if (gy>=1000) gy=999;
			if (gx<=0) gx=1;
			if (gy<=0) gy=1;
			
			if (gx!=previous_gx){
				String msg="g";
				if (gx<10) msg+="00";
				else if (gx<100) msg+="0";
				msg+=(int)gx;
				inter.sendMsg(msg);
				previous_gx=(int)gx;
			}
			if (gy!=previous_gy){
				String msg="h";
				if (gy<10) msg+="00";
				else if (gy<100) msg+="0";
				msg+=(int)gy;
				inter.sendMsg(msg);
				previous_gy=(int)gy;
			}
			
			
			
			flowX=image.vectors[350+(int)(x+dx)][270-(int)(y+dy)][0]*4;
			flowY=image.vectors[350+(int)(x+dx)][270-(int)(y+dy)][1]*4;
			
			if (flowX!=previous_flowX){
				String msg="o";
				if (flowX<10) msg+="00";
				else if (flowX<100) msg+="0";
				msg+=flowX;
				inter.sendMsg(msg);
				previous_flowX=flowX;
			}
			if (flowY!=previous_flowY){
				String msg="p";
				if (flowY<10) msg+="00";
				else if (flowY<100) msg+="0";
				msg+=flowY;
				inter.sendMsg(msg);
				previous_flowY=flowY;
			}
			
			
			
			if (jx>-25 && jx<25 && jy>-25 && jy<25){}
			else{
				//System.out.println(" +++ "+x+" , "+y+" ; "+friction_solid+" , "+friction_fluid);
				x+=(float)(dx*2*(500-friction_fluid)/500 * (500-friction_solid)/500) + 2*(500-gx)/500;
				y+=(float)(dy*2*(500-friction_fluid)/500 * (500-friction_solid)/500) + 2*(500-gy)/500;
			}
			x+=((float)flowX-500)/500;
			y+=((float)flowY-500)/500;
			
			frame.repaint();
			
			
			
			try {Thread.sleep(1);} 
			catch (InterruptedException e){e.printStackTrace();}
			
			if (count==0){
			
				trace[time][0]=x;
				trace[time][1]=y;
				time++;
				if (time>=LENGTH) time=0;
			}
			count++;
			if (count>=5) count=0;
			
			
			
		}
	}
	
	
	public static void main(String[] args){
		new Main();
	}
	
}
