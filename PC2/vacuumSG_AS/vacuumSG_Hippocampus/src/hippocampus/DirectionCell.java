package hippocampus;

public class DirectionCell {

	public int angle;
	
	public float activity=0;
	
	public DirectionCell(int a){
		angle=a;
	}
	
	public void setActivity(int a){
		float delta=a-angle;
		if (delta<-180) delta+=360;
		if (delta> 180) delta-=360;
		activity=(float) Math.exp(-delta*delta/2000);
	}
	
}
