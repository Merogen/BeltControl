import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image {

	public static int SIZE=700;
	
	private String img = System.getProperty("user.home")+"/Bureau/tablette_force_pilot/rectangles.png";
	//private String img=null;
	
	//private String flow = System.getProperty("user.home")+"/Bureau/tablette_force_pilot/vectorflow.png";
	private String flow=null;
	
	public int[][][] image;
	
	public int[][][] vectors;
	
	public Image(){
		
		image=new int[SIZE][SIZE][3];
		
		if (img!=null){
			BufferedImage bf = null;
			try {
				bf = ImageIO.read(new File(img));
			} catch (IOException e) {System.out.print(e);}
		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					image[i][j][0]=(bf.getRGB(i, j)>> 16) & 0x000000FF;
					image[i][j][1]=(bf.getRGB(i, j)>>  8) & 0x000000FF;
					image[i][j][2]=(bf.getRGB(i, j)     ) & 0x000000FF;
				}
			}
		}
		else{
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					image[i][j][0]=0;
					image[i][j][1]=0;
					image[i][j][2]=0;
				}
			}
		}
		
		
		vectors=new int[SIZE][SIZE][2];
		
		if (flow!=null){
			BufferedImage bf = null;
			try {
				bf = ImageIO.read(new File(flow));
			} catch (IOException e) {System.out.print(e);}

		
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					vectors[i][j][0]=(bf.getRGB(i, j)>> 16) & 0x000000FF;
					vectors[i][j][1]=(bf.getRGB(i, j)>>  8) & 0x000000FF;
				}
			}
		}
		else{
			for (int i=0;i<SIZE;i++){
				for (int j=0;j<SIZE;j++){
					vectors[i][j][0]=125;
					vectors[i][j][1]=125;
				}
			}
		}
	}
	
}
