package hippocampus;

public class GridCell {

	public float activity=0;
	
	public float pos_x;
	public float pos_y;
	
	public GridCell(float x, float y){
		pos_x=x;
		pos_y=y;
	}
	
	public void setActivity(float x,float y){
		float delta_x=x-pos_x;
		if (delta_x<-0.5) delta_x+=1;
		if (delta_x> 0.5) delta_x-=1;
		
		float delta_y=y-pos_y;
		if (delta_y<-0.5) delta_y+=1;
		if (delta_y> 0.5) delta_y-=1;
		
		float dist2=delta_x*delta_x+delta_y*delta_y;
		
		activity=(float) Math.exp(-dist2*10);
	}
	
}
