package hippocampus;

import java.util.ArrayList;

public class PlaceCell extends Cell{

	public float[][] map;

	
	
	
	public PlaceCell(float x, float y, float[][] m,  ArrayList<Cell> n, int id){
		super(x,y,m,n, id);
		isPlaceCell=true;
		neighbors=new ArrayList<Cell>();
		ident=id;
		
		map=new float[Hippocampus.NB_DIRECTIONS][10];
		for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){
			for (int j=0;j<10;j++){
				map[i][j]=m[i][j];
			}
		}
		
		for (int i=0;i<n.size();i++){
			neighbors.add(n.get(i));
		}
		
		distance=1000000;
		path=0;
	}
	
	public PlaceCell(float x, float y, int id){
		super(x,y,null,null, id);
		
		map=new float[Hippocampus.NB_DIRECTIONS][10];
		
		isPlaceCell=true;
		neighbors=new ArrayList<Cell>();
		ident=id;
		
		distance=1000000;
		path=-1;
	}
	
	public void compute(float[][] m){
		float act=0;
		for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){
			for (int j=0;j<10;j++){
				act+=m[i][j]*map[i][j];
			}
		}
		activity=act/Hippocampus.NB_DIRECTIONS;
	}
	
	
	public void setDistance(float dist){
		distance=dist;
		
		for (int p=0;p<neighbors.size();p++){
			if (neighbors.get(p).isPlaceCell){
				float next=dist+Hippocampus.dist(this,neighbors.get(p));
				if (neighbors.get(p).distance>next){
					neighbors.get(p).setDistance(next);
				}
			}
		}
		
	}
	
	public void setPath(){
		float minNext=1000000;
		path=-1;
		
		if (distance>0){
			for (int p=0;p<neighbors.size();p++){
				if (neighbors.get(p).isPlaceCell && neighbors.get(p).distance<minNext){
					minNext=neighbors.get(p).distance;
					path=p;
				}
			}
		}
	}
	
	
	public void reset(){
		distance=1000000;
	}
	
}
