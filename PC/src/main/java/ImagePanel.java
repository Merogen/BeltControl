import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Generic class of panel that can be exported as pdf file and jpeg image
 * @author simon
 */
public class ImagePanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	protected Main main;
	
	public ImagePanel(Main m){
		main=m;
		
		addMouseListener(this);
	}

	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 800);
		
		for (int i=0;i<Image.SIZE;i++){
			for (int j=0;j<Image.SIZE;j++){
				g.setColor(new Color(main.image.image[i][j][0],main.image.image[i][j][1],main.image.image[i][j][2]));
				g.fillRect(i, j, 1, 1);
			}
		}
		
		g.setColor(Color.cyan);
		for (int t=0;t<Main.LENGTH;t++){
			g.fillOval(348+(int)main.trace[t][0], 268-(int)main.trace[t][1], 5, 5);
		}
		
		
		g.setColor(Color.white);
		g.drawOval(325+(int)main.x, 245-(int)main.y, 50, 50);
		
		g.drawLine(350+(int)main.x, 270-(int)main.y, 350+(int)main.x+(int)main.dx*5, 270-(int)main.y-(int)main.dy*5);
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		main.x=0;
		main.y=0;
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}