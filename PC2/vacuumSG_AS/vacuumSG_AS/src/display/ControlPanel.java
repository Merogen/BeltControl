package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import decision.UserControl;

import platform.Agent;


/**
 * A panel to display the agent's point of view
 * @author simon
 *
 */
public class ControlPanel extends EnvPanel  implements MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	public boolean[] action;
	
	public ControlPanel(Agent a){
		super(a);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		
		action=new boolean[8];
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 800);;
		
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
			
			if (agent.decision.direction[v]>0){
				g.setColor(Color.blue);
				val=agent.decision.direction[v]*2;
				g.fillOval((int)(120+125+x*125-val/2),(int)( 50+125+y*125-val/2), (int)val, (int)val);
			}
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

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//System.out.println("test");
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
