
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
public class ControlPanel extends JPanel  implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	public boolean[] action;
	
	public Main main;
	
	
	public int selected_script=-1;
	
	
	
	public ControlPanel(Main m){
		
		super();
		addMouseListener(this);
		setFocusable(true);
		main=m;
		action=new boolean[8];
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 800);
		
		// draw list
		for (int s=0;s<main.scriptList.size();s++){
			if (s==selected_script) g.setColor(Color.red);
			else g.setColor(Color.black);
			g.fillOval(20, 70+20*s, 10, 10);
			g.setColor(Color.black);
			g.drawString(main.scriptList.get(s), 35, 80+s*20);
		}
		
		// display script
		g.setColor(Color.black);
		for (int c=0;c<main.commandList.size();c++){
			g.drawString(main.commandList.get(c).getCommand(), 300, 80+c*20);
		}
		
		// execute button
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(20, 20, 120, 40);
		g.setColor(Color.black);
		g.drawRect(20, 20, 120, 40);
		g.drawString("RUN", 65, 45);
		
		// pause button
		if (main.pause) g.setColor(Color.GRAY);
		else g.setColor(Color.LIGHT_GRAY);
		g.fillRect(150, 20, 120, 40);
		g.setColor(Color.black);
		g.drawRect(150, 20, 120, 40);
		g.drawString("PAUSE", 190, 45);
		
		// stop button
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(280, 20, 120, 40);
		g.setColor(Color.black);
		g.drawRect(280, 20, 120, 40);
		g.drawString("STOP", 320, 45);
		
	}


	public void mouseClicked(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		
		for (int s=0;s<main.scriptList.size();s++){
			if (x>=15 && x<=35 && y>=65+20*s && y<=85+20*s){
				selected_script=s;
				main.readScript(main.scriptList.get(s));
			}
		}
		
		if (selected_script>=0 && selected_script<main.scriptList.size()){
			if (x>=20 && x<=140 && y>=20 && y<=60){
				main.run=true;
			}
		}
		
		if (x>=150 && x<=270 && y>=20 && y<=60){
			main.pause=!main.pause;
			System.out.println("PAUSE");
		}
		
		if (x>=280 && x<=400 && y>=20 && y<=60){
			System.out.println("STOP");
			main.stop=true;
		}
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent arg0) {}


}