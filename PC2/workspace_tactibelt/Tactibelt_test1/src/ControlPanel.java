
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;


/**
 * A panel to display the agent's point of view
 * @author simon
 *
 */
public class ControlPanel extends JPanel  implements MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	
	public boolean[] action;
	
	public Main main;
	
	public boolean pressed=false;
	
	public int px=0;
	public int py=0;
	public int distance=0;
	public int angle=0;
	
	private int button=-1;
	
	public ControlPanel(Main m){
		
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		setFocusable(true);
		main=m;
		action=new boolean[8];
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 800);
		
		// draw target
		g.setColor(Color.black);
		for (int i=0;i<9;i++){
			g.drawOval(250-i*20, 300-i*20, i*40,i*40);
		}
		
		
		if (pressed){
			//System.out.println(px+" ; "+py);
			
			g.setColor(Color.red);
			g.fillOval(px-5,py-5,10,10);
		}
		
	}


	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
	
	public void mousePressed(MouseEvent e) {
		button=e.getButton();
		pressed=true;
		
		
	}
	public void mouseReleased(MouseEvent arg0) {
		pressed=false;
		px=-1;
		py=-1;
		main.stop();
		button=-1;
	}

	public void mouseDragged(MouseEvent e) {
		if (pressed){
			px=e.getX();
			py=e.getY();
			
			double dist=Math.sqrt((px-250)*(px-250) + (py-300)*(py-300) );
			
			int distance2=(int)(dist/20);
			
			boolean change=false;
			
			if (distance2!=distance){
				distance=distance2;
				change=true;
			}
			
			
			int angle2=0;
			if (dist==0){
				angle=0;
			}
			else{
				angle2 = (int)(Math.toDegrees(Math.atan2(py-300, px-250)));
				angle2=(angle2+360+180)%360;
			}
			
			if (angle2!=angle){
				angle=angle2;
				change=true;
				
			}
			
			if (change && button>0) main.sendMsg(angle, Math.min(9,  Math.max(0,10-distance)), 0, button-1);
			
			System.out.println(px+" ; "+py+ " -> "+distance+ " ; "+angle2);
		}
		
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
}