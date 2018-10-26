package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import decision.UserControl;
import environment.Environment;

import platform.Agent;


/**
 * A panel to display the agent's point of view
 * @author simon
 *
 */
public class ControlPanel extends EnvPanel  implements MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	private boolean display=false;
	
	Agent agent;
	
	public ControlPanel(Agent a){
		super(a);
		agent=a;
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 800);
		
		
		// draw surrounding environment
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform init = g2d.getTransform();

		if (display){
			int ix=Math.round(agent.body.position[0]);
			int jy=Math.round(agent.body.position[1]);
			
			float x1=agent.body.position[0];
			float y1=agent.body.position[1];
			
			float delta_x=ix-x1;
			float delta_y=jy-y1;
			
			g2d.setColor(Environment.WALL1);
			g2d.rotate(agent.body.position[2]-Math.PI/2, 245, 175);
			
			for (int i=-1;i<=1;i++){
				for (int j=-1;j<=1;j++){
					if (agent.main.env.isWalkthroughable(x1+i,y1-j))
						g2d.drawRect(245-100+200*i+(int)(200*delta_x), 175-100+200*j-(int)(200*delta_y), 200, 200);
					else
						g2d.fillRect(245-100+200*i+(int)(200*delta_x), 175-100+200*j-(int)(200*delta_y), 200, 200);
				}
			}
		}
		
		g2d.setTransform(init);
		
		
		// draw tactile feedback
		g.setColor(Color.black);
		g.drawOval(120, 50, 250, 250);
		g.drawOval(121, 51, 248, 248);
		
		// draw vibrators
		for (int v=0;v<UserControl.NB_VIBRATOR;v++){
			float x=-(float)Math.sin(v*UserControl.ANGLE*Math.PI/180);
			float y=(float)Math.cos(v*UserControl.ANGLE*Math.PI/180);
			
			g.setColor(Color.black);
			float val=agent.decision.vibrators[v]*2;
			g.fillOval((int)(120+125+x*125-val/2),(int)( 50+125+y*125-val/2), (int)val, (int)val);
			
			if (agent.decision.direction_target[v]>0){
				g.setColor(Color.blue);
				val=agent.decision.direction_target[v]*2;
				g.fillOval((int)(120+125+x*125-val/2),(int)( 50+125+y*125-val/2), (int)val, (int)val);
			}
			
			if (agent.decision.direction_path[v]>0){
				g.setColor(Color.red);
				val=agent.decision.direction_path[v]*2;
				g.fillOval((int)(120+125+x*125-val/2),(int)( 50+125+y*125-val/2), (int)val, (int)val);
			}
			
			if (agent.decision.direction_goals[v]>0){
				g.setColor(Color.green);
				val=agent.decision.direction_goals[v]*2;
				g.fillOval((int)(120+125+x*125-val/2),(int)( 50+125+y*125-val/2), (int)val, (int)val);
			}
		}
		
		if (display) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.fillRect(10, 10, 20, 20);
		
		for (int i=0;i<agent.decision.data.length;i++){
			if (agent.decision.data[i]) g.setColor(Color.red);
			else g.setColor(Color.black);
			g.fillRect(20+40*i, 600, 20, 20);
		}

		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		if (e.getKeyCode()==100) agent.decision.action[0] = true;
		if (e.getKeyCode()==102) agent.decision.action[1] = true;
		if (e.getKeyCode()==101) agent.decision.action[2] = true;
		if (e.getKeyCode()== 98) agent.decision.action[3] = true;
		if (e.getKeyCode()== 97) agent.decision.action[4] = true;
		if (e.getKeyCode()== 99) agent.decision.action[5] = true;
	}

	@Override
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
		if (e.getX()>=10 && e.getX()<=30 && e.getY()>=10 && e.getY()<=30) display=!display;
		
		for (int i=0;i<agent.decision.data.length;i++){
			if (e.getX()>=20+i*40 && e.getX()<=40+i*40 && e.getY()>=600 && e.getY()<=620) agent.decision.data[i]=!agent.decision.data[i];
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		agent.started=true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
