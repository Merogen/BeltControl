package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import platform.Agent;



/**
 * A panel to display the agent's point of view
 * @author simon
 *
 */
public class HippocampusPanel extends EnvPanel  implements MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	
	private static int SCALE=30;
	
	private int display_goalLinks=1;
	private int display_pointLinks=1;
	
	
	public HippocampusPanel(Agent a){
		super(a);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, 1100, 640);
		
		float px=agent.body.position[0];
		float py=agent.body.position[1];
		float rz=agent.body.position[2];
		
		int h=agent.main.env.getHeight();
		int w=agent.main.env.getWidth();
		
		// draw the environment
		for (int i=0;i<w;i++){
			for (int j=0;j<h;j++){
				g.setColor(agent.main.env.seeBlock(i, h-j-1));
				g.fillRect(SCALE*i, SCALE*j, SCALE, SCALE);
			}
		}
		

		
		// draw agent position
		g.setColor(Color.red);
		g.fillOval((int)((px)*SCALE), h*SCALE-(int)((py+1)*SCALE),SCALE,SCALE);	
		
		// draw agent orientation
		float px2= px+0.6f*(float)((Math.cos(rz-Math.PI/4) - Math.sin(rz-Math.PI/4)));
		float py2= py+0.6f*(float)((Math.sin(rz-Math.PI/4) + Math.cos(rz-Math.PI/4)));
		g.drawLine((int)( px   *SCALE)+SCALE/2, h*SCALE-(int)((py)*SCALE)-SCALE/2,
				   (int)( px2  *SCALE)+SCALE/2, h*SCALE-(int)((py2)*SCALE)-SCALE/2);
		
		

		
		// draw visible points' links
		g.setColor(Color.black);
		for (int i=0;i<agent.hippocampus.pointList.size();i++){
			if (agent.hippocampus.pointList.get(i).visible && display_pointLinks==1){
				g.drawLine(		   (int)((px  )*SCALE)+SCALE/2,
						   h*SCALE-(int)((py+1)*SCALE)+SCALE/2,
						   		   (int)(agent.hippocampus.pointList.get(i).pos_x*SCALE),
						   h*SCALE-(int)(agent.hippocampus.pointList.get(i).pos_y*SCALE));
			}
		}
		
		// draw key points
		for (int i=0;i<agent.hippocampus.pointList.size();i++){
			if (agent.hippocampus.pointList.get(i).visible) g.setColor(Color.green);
			else g.setColor(Color.black);
			g.fillOval(		   (int)(agent.hippocampus.pointList.get(i).pos_x*SCALE)-SCALE/8,
					   h*SCALE-(int)(agent.hippocampus.pointList.get(i).pos_y*SCALE)-SCALE/8, SCALE/4, SCALE/4);
		}
		
		
		// draw goals
		/*g.setColor(Color.blue);
		for (int i=0;i<agent.hippocampus.goals.size();i++){
			g.fillOval(			(int)(agent.hippocampus.goals.get(i).pos_x*SCALE),
					    h*SCALE-(int)(agent.hippocampus.goals.get(i).pos_y*SCALE),5,5);
			
			if (display_goalLinks==1){
				g.drawLine(		   (int)((px  )*SCALE)+SCALE/2,
						   h*SCALE-(int)((py+1)*SCALE)+SCALE/2,
						           (int)(agent.hippocampus.goals.get(i).pos_x*SCALE)+2,
						   h*SCALE-(int)(agent.hippocampus.goals.get(i).pos_y*SCALE)+2);
			}
		}*/
		
		
		// draw buttons
		if (display_goalLinks==0)g.setColor(Color.black);
		else g.setColor(Color.red);
		
		g.fillRect(850,300,20,20);
		
		if (display_pointLinks==0)g.setColor(Color.black);
		else g.setColor(Color.red);
		
		g.fillRect(850,340,20,20);
		
		
		// draw background
		/*g.setColor(Color.blue);
		g.fillRect(0, 600, 720, 100);
		g.setColor(Color.orange);
		g.fillRect(0,700, 720, 100);
		
		// draw visual features
		g.setColor(Color.red);
		for (int i=0;i<agent.hippocampus.pointList.size();i++){
			double d=100/ (Math.max(0.1,agent.hippocampus.pointList.get(i).distance));
			int theta=(int) agent.hippocampus.pointList.get(i).angle;
			g.fillRect(theta*2, (int)( 700- d/2 ), 2, (int)(d));
		}*/
		
	}

	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		if (e.getKeyCode()==100) agent.decision.action[0] = true;
		if (e.getKeyCode()==102) agent.decision.action[1] = true;
		if (e.getKeyCode()==101) agent.decision.action[2] = true;
		if (e.getKeyCode()== 98) agent.decision.action[3] = true;
		if (e.getKeyCode()== 97) agent.decision.action[4] = true;
		if (e.getKeyCode()== 99) agent.decision.action[5] = true;
	}

	public void keyReleased(KeyEvent e) {
		//System.out.println(" - "+e.getKeyCode());
		if (e.getKeyCode()==100) agent.decision.action[0] = false;
		if (e.getKeyCode()==102) agent.decision.action[1] = false;
		if (e.getKeyCode()==101) agent.decision.action[2] = false;
		if (e.getKeyCode()== 98) agent.decision.action[3] = false;
		if (e.getKeyCode()== 97) agent.decision.action[4] = false;
		if (e.getKeyCode()== 99) agent.decision.action[5] = false;
	}

	public void keyTyped(KeyEvent arg0) {}

	public void mouseClicked(MouseEvent e) {
		float x=(float)( e.getX() )/(float)SCALE;
		float y=(float)(-e.getY() )/(float)SCALE + agent.main.env.getHeight();
		System.out.println(x+" ; "+y);
		
		
		boolean found=false;
		int i=0;
		while (i<agent.hippocampus.pointList.size() && !found){
			if ( (agent.hippocampus.pointList.get(i).pos_x-x)*(agent.hippocampus.pointList.get(i).pos_x-x) 
			   + (agent.hippocampus.pointList.get(i).pos_y-y)*(agent.hippocampus.pointList.get(i).pos_y-y) <0.2 ){
				found=true;
				agent.hippocampus.pointList.remove(i);
			}
			else i++;
		}
		
		
		if (!found) agent.hippocampus.addPoint(x, y);
		
		if (e.getX()>=850 && e.getX()<=870 && e.getY()>=300 && e.getY()<=320) display_goalLinks=(display_goalLinks+1)%2;
		if (e.getX()>=850 && e.getX()<=870 && e.getY()>=340 && e.getY()<=360) display_pointLinks=(display_pointLinks+1)%2;
	}
	
	
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
