package hippocampus;

public class GridCellMatrix {

	public static int MATRIX_SIZE=9;
	
	public GridCell[][] matrixGrid;
	
	public int grid_x=0;		// index of the current grid
	public int grid_y=0;
	
	public float pos_x=1;		// hard-coded position of the agent in the matrix
	public float pos_y=1;
	
	public GridCellMatrix(){
		matrixGrid=new GridCell[MATRIX_SIZE][MATRIX_SIZE];
		for (int i=0;i<MATRIX_SIZE;i++){
			for (int j=0;j<MATRIX_SIZE;j++){
				matrixGrid[i][j]=new GridCell((float)i/((float)MATRIX_SIZE),(float)j/((float)MATRIX_SIZE));
			}
		}
	}
	
	public void update(float vestibular_x, float vestibular_y){
		
		
		// update position in matrix
		pos_x+=vestibular_x;
		pos_y+=vestibular_y;
		
		if (pos_x>=1){
			pos_x=0;
			grid_x++;
		}
		if (pos_x<0){
			pos_x=1;
			grid_x--;
		}
		if (pos_y>=1){
			pos_y=0;
			grid_y++;
		}
		if (pos_y<0){
			pos_y=1;
			grid_y--;
		}
		//System.out.println(pos_x+" ; "+pos_y+" -> "+" ( "+grid_x+", "+grid_y+" )");
		
		// simulate grid cells activity
		for (int i=0;i<MATRIX_SIZE;i++){
			for (int j=0;j<MATRIX_SIZE;j++){
				matrixGrid[i][j].setActivity(pos_x,pos_y);
				
			}
		}
	}
	
}
