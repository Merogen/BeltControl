package hippocampus;

import java.util.ArrayList;

public class Cell {

	public boolean isPlaceCell;
	
	public int ident;
	
	public float pos_x;
	public float pos_y;
	
	public float activity;
	
	public float distance;
	
	public int path;
	
	public ArrayList<Cell> neighbors;
	
	public Cell(float x, float y, float[][] m, ArrayList<Cell> n, int id){
		pos_x=x;
		pos_y=y;
		
		isPlaceCell=false;
		
		neighbors=null;
		
		ident=-1;
		path=-1;
	}
	
	public void compute(float[][] m){
		activity=0;
	}
	
	public void setDistance(float dist){
		distance=dist;
	}
	
	public void printPos(){
		System.out.println(" {"+pos_x+","+pos_y+"} ");
	}
	
	public void connectPlaceCell(Cell cell){
		if (cell.isPlaceCell){
			int pt=0;
			boolean found=false;
			while (pt<neighbors.size() && !found){
				if (neighbors.get(pt).ident==cell.ident) found=true;
				else pt++;
			}
			if (!found){ // connect goal and temp
				neighbors.add(cell);
				cell.neighbors.add(this);
			}
		}
	}
	
	public void removePlaceCell(Cell cell){
		if (cell.isPlaceCell){
			int pt=0;
			boolean found=false;
			while (pt<neighbors.size() && !found){
				if (neighbors.get(pt).ident==cell.ident) found=true;
				else pt++;
			}
			if (found) this.neighbors.remove(pt);
			
			// disconnect goal
			pt=0;
			found=false;
			while (pt<cell.neighbors.size() && !found){
				if (cell.neighbors.get(pt).ident==this.ident) found=true;
				else pt++;
			}
			if (found) cell.neighbors.remove(pt);
		}
	}
	
}
