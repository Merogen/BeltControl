package display;

import hippocampus.GridCellMatrix;
import hippocampus.Hippocampus;
import hippocampus.PlaceCell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jssc.SerialPortException;

import platform.Agent;


/**
 * A panel to display the agent's point of view
 * @author simon
 *
 */
public class HippocampusPanel extends EnvPanel  implements MouseListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	private static float SCALE=20;
	
	private int selected_cell=-1;
	
	private int display_mode=0;
	private int selected_mode=0;
	
	public HippocampusPanel(Agent a){
		super(a);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		
		// draw background
		g.setColor(Color.white);
		//g.fillRect(0, 0, 1100, 250);
		//g.fillRect(0, 240, 350, 400);
		g.fillRect(0, 0, 1100, 640);
		
		// draw direction cells
		g.setColor(Color.black);
		for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){
			g.fillRect(20+50*i,220-(int)(200*agent.hippocampus.directionCells[i].activity),50,(int)(200*agent.hippocampus.directionCells[i].activity));
		}
		
		// draw matrix and grid cells
		g.drawRect(20,300,300,300);
		
		for (int i=0;i<GridCellMatrix.MATRIX_SIZE;i++){
			for (int j=0;j<GridCellMatrix.MATRIX_SIZE;j++){
				g.setColor(new Color(agent.hippocampus.gridCells.matrixGrid[i][j].activity,0f,0f));
				g.fillOval((int)(i*300/GridCellMatrix.MATRIX_SIZE),580-(int)(j*300/GridCellMatrix.MATRIX_SIZE),40,40);
			}
		}
		
		g.setColor(Color.black);
		g.fillOval(18+(int)(300*agent.hippocampus.gridCells.pos_x), 598-(int)(300*agent.hippocampus.gridCells.pos_y),5,5);

		
		// draw current map
		for (int i=0;i<Hippocampus.NB_DIRECTIONS;i++){
			for (int j=0;j<10;j++){
				g.setColor(new Color(agent.hippocampus.currentMap[i][j],0f,0f));
				g.fillOval((int)(450+i*80/GridCellMatrix.MATRIX_SIZE),100-(int)(j*80/GridCellMatrix.MATRIX_SIZE),10,10);
				
				for (int p=0;p<agent.hippocampus.placeList.size();p++){
					g.setColor(new Color(agent.hippocampus.placeList.get(p).map[i][j],0f,0f));
					g.fillOval((int)(550+80*p+i*80/GridCellMatrix.MATRIX_SIZE),100-(int)(j*80/GridCellMatrix.MATRIX_SIZE),10,10);
					
					g.setColor(Color.black);
					g.fillRect(550+80*p, 240-(int)(100*agent.hippocampus.placeList.get(p).activity),50,(int)(100*agent.hippocampus.placeList.get(p).activity));
				}
			}
		}
		
		// draw agent position
		g.setColor(Color.red);
		g.fillOval(360+(int)(agent.body.position[0]*SCALE), 260+350-(int)(agent.body.position[1]*SCALE),5,5);	
		
		
		// draw current goals
		g.setColor(Color.black);
		for (int i=0;i<agent.hippocampus.goals.size();i++){
			g.fillOval(360-2+    (int)((agent.hippocampus.goals.get(i).pos_x)*SCALE),
					   260-2+350-(int)((agent.hippocampus.goals.get(i).pos_y)*SCALE),5,5);
		}
		
		// draw place cells' goals
		for (int p=0;p<agent.hippocampus.placeList.size();p++){
			if (p==0) g.setColor(Color.red);
			else if (p==1) g.setColor(Color.green);
			else if (p==2) g.setColor(Color.blue);
			else if (p==3) g.setColor(Color.magenta);
			else if (p==4) g.setColor(Color.yellow);
			else if (p==5) g.setColor(Color.cyan);
			else g.setColor(Color.gray);
			
			g.drawOval(360-2+(int)(agent.hippocampus.placeList.get(p).pos_x*SCALE), 260-2+350-(int)(agent.hippocampus.placeList.get(p).pos_y*SCALE),10,10);
			if (display_mode==0){
				for (int i=0;i<agent.hippocampus.placeList.get(p).neighbors.size();i++){
					g.drawOval(360+    (int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_x)*SCALE),
							   260+350-(int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_y)*SCALE),5,5);
				}
			}
		}
		
		// draw connections between place cells and their goals
		if (display_mode==0){
		g.setColor(Color.lightGray);
		for (int p=0;p<agent.hippocampus.placeList.size();p++){
			for (int i=0;i<agent.hippocampus.placeList.get(p).neighbors.size();i++){
					g.drawLine(360+    (int)((agent.hippocampus.placeList.get(p).pos_x)*SCALE),
							   260+350-(int)((agent.hippocampus.placeList.get(p).pos_y)*SCALE),
							   360+    (int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_x)*SCALE),
							   260+350-(int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_y)*SCALE));
			}
		}
		}
		
		// draw place cell connections
		g.setColor(Color.black);
		for (int p=0;p<agent.hippocampus.placeList.size();p++){
			for (int i=0;i<agent.hippocampus.placeList.get(p).neighbors.size();i++){
				if (agent.hippocampus.placeList.get(p).neighbors.get(i).isPlaceCell){
					g.drawLine(360+    (int)((agent.hippocampus.placeList.get(p).pos_x)*SCALE),
							   260+350-(int)((agent.hippocampus.placeList.get(p).pos_y)*SCALE),
							   360+    (int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_x)*SCALE),
							   260+350-(int)((agent.hippocampus.placeList.get(p).neighbors.get(i).pos_y)*SCALE));
				}
			}
		}
		
		if (selected_mode==0){
			// draw link between current place cell and aimed goal
			g.setColor(Color.green);
			g.drawLine(360+    (int)(agent.hippocampus.currentPlace.pos_x*SCALE),
					   260+350-(int)(agent.hippocampus.currentPlace.pos_y*SCALE),
					   360+    (int)(agent.hippocampus.currentGoal.pos_x*SCALE),
					   260+350-(int)(agent.hippocampus.currentGoal.pos_y*SCALE));
			
			// display selected path
			if (selected_cell>=0){
				g.setColor(Color.red);
				for (int i=0;i<agent.hippocampus.paths.get(selected_cell).size()-1;i++){
					int index1=agent.hippocampus.paths.get(selected_cell).get(i);
					int index2=agent.hippocampus.paths.get(selected_cell).get(i+1);
					g.drawLine(360+    (int)(agent.hippocampus.placeList.get(index1).pos_x*SCALE),
							   260+350-(int)(agent.hippocampus.placeList.get(index1).pos_y*SCALE),
							   360+    (int)(agent.hippocampus.placeList.get(index2).pos_x*SCALE),
							   260+350-(int)(agent.hippocampus.placeList.get(index2).pos_y*SCALE));
				}
			}
		}
		
		
		// draw target path
		if (selected_mode==1){
			
			g.setColor(Color.red);
			g.fillOval(360+(int)(agent.hippocampus.targetPlace.pos_x*SCALE)-5, 260+350-(int)(agent.hippocampus.targetPlace.pos_y*SCALE)-5,15,15);
			
			PlaceCell temp=agent.hippocampus.currentPlace;
			boolean found=true;
			while (found && temp.path!=-1){
				g.drawLine(360+    (int)(temp.pos_x*SCALE), 260+350-(int)(temp.pos_y*SCALE),
						   360+    (int)(temp.neighbors.get(temp.path).pos_x*SCALE), 260+350-(int)(temp.neighbors.get(temp.path).pos_y*SCALE));
				
				if (temp.path!=-1) temp=(PlaceCell) temp.neighbors.get(temp.path);
				else found=false;
			}
			
		}
		
		
		// draw buttons
		if (display_mode==0)g.setColor(Color.black);
		else g.setColor(Color.red);
		
		g.fillRect(850,300,20,20);
		
		if (selected_mode==0)g.setColor(Color.black);
		else g.setColor(Color.red);
		
		g.fillRect(850,340,20,20);
		
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
		float x=(e.getX()-360)/SCALE;
		float y=(-e.getY()+260+350)/SCALE;
		System.out.println(x+" ; "+y);
		
		float dmin=10/SCALE;
		int imin=-1;
		for (int i=0;i<agent.hippocampus.placeList.size();i++){
			float d=(x-agent.hippocampus.placeList.get(i).pos_x)*(x-agent.hippocampus.placeList.get(i).pos_x)
				   +(y-agent.hippocampus.placeList.get(i).pos_y)*(y-agent.hippocampus.placeList.get(i).pos_y);
			
			if (d<dmin){
				dmin=d;
				imin=i;
			}
		}
		selected_cell=imin;
		System.out.println(imin);
		
		if (e.getX()>=850 && e.getX()<=870 && e.getY()>=300 && e.getY()<=320) display_mode=(display_mode+1)%2;
		if (e.getX()>=850 && e.getX()<=870 && e.getY()>=340 && e.getY()<=360) selected_mode=(selected_mode+1)%2;
	}
	
	
	
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
