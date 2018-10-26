package environment;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.Color;

import platform.Agent;
import platform.PrintablePanel;

/**
 * Panel that display environment
 * @author simon
 *
 */
public class EnvironnementPanel extends PrintablePanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	
	private Environment m_env;
	
	private int m_clicked=0;
	private int m_clickX;
	private int m_clickY;
	
	private float m_FclickX;
	private float m_FclickY;

	private CubicCurve2D.Double petal1 = new CubicCurve2D.Double(0, 0,  0,80, 80,0,   0, 0);
	private CubicCurve2D.Double petal2 = new CubicCurve2D.Double(0, 0, 80, 0,  0,-80, 0, 0);
	private CubicCurve2D.Double petal3 = new CubicCurve2D.Double(0, 0,  0,-80,-80, 0, 0, 0);
	private CubicCurve2D.Double petal4 = new CubicCurve2D.Double(0, 0, -80, 0, 0, 80, 0, 0);

	private GeneralPath m_leaf = new GeneralPath();
	private GeneralPath m_fish = new GeneralPath();
		
	public EnvironnementPanel(Environment env){
		super();
		m_env=env;
		
		addMouseListener(this);
		
		m_leaf.append(petal1, false);
		m_leaf.append(petal2, false);
		m_leaf.append(petal3, false);
		m_leaf.append(petal4, false);
		
		m_fish.append(new CubicCurve2D.Double(-40, 15,  -30, 0, 40, -40,   40, 0), false);
		m_fish.append(new CubicCurve2D.Double( 40,  0,  40, 40, -30,  0,   -40, -15), true);
		m_fish.append(new Area(new Ellipse2D.Double( 20,  -10,  8, 8)), false);

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	// draw environment and generate a pdf file
	public void drawPDF(Graphics g){
		paintComponent(g,0.5f,0.5f);
	}
	public void paintComponent(Graphics g){
		paintComponent(g,1,1);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	public void paintComponent(Graphics g,float sx,float sy){

		Graphics2D g2d = (Graphics2D)g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform boardReference = g2d.getTransform();

		int h=this.getHeight();
		int w=this.getWidth();
		
		int c_w=(int) (sx*w/m_env.getWidth());
		int c_h=(int) (sy*h/m_env.getHeight());
		
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, w, h);
		
		// draw agents
		for (int i=0;i<m_env.platform().nbAgent();i++){
			
			int parameter=0;
			if (m_env.platform().selectAgent(i).body.sensors[0]==1) parameter=1;
			
			paintAgent((Graphics2D)g.create(),
					   (int) (   m_env.platform().selectAgent(i).body.position[0]   *c_w+(c_w/2)),
					   (int) (h-(m_env.platform().selectAgent(i).body.position[1]+1)*c_h+(c_h/2)),
					   (m_env.platform().selectAgent(i).body.position[2]),
					   (double)c_w/100,(double)c_h/100,
					   parameter);
		}
		
		// draw static elements
		for (int i=0;i<m_env.getWidth();i++){
			for (int j=0;j<m_env.getHeight();j++){
				
				// walls
				if (m_env.isWall(i, j)){
					g2d.setColor(m_env.seeBlock(i,j));
					g2d.fillRect(i*c_w, h-(j+1)*c_h, c_w+1, c_h+1);
				}
				
				// fish
				else if (m_env.isFood(i, j)){
					AffineTransform centerCell = new AffineTransform();
					centerCell.translate(i*c_w+c_w/2, h-(j+1)*c_h+c_h/2);
					centerCell.scale((double) c_w / 100, (double) c_h / 100); 
					g2d.transform(centerCell);
					g2d.setColor(m_env.seeBlock(i,j));
					g2d.fill(m_fish);
					g2d.setTransform(boardReference);
				}
				
				// leaf
				else if (m_env.isAlga(i, j)){
					AffineTransform centerCell = new AffineTransform();
					centerCell.translate(i*c_w+c_w/2, h-(j+1)*c_h+c_h/2);
					centerCell.scale((double) c_w / 100, (double) c_h / 100); 
					g2d.transform(centerCell);
					g2d.setColor(m_env.seeBlock(i,j));
					g2d.fill(m_leaf);
					g2d.setTransform(boardReference);
				}
				
				else if (m_env.isVisible(i,j)){
					g2d.setColor(m_env.seeBlock(i,j));
					g2d.fillOval(i*c_w, h-(j+1)*c_h, c_w+1, c_h+1);
				}
				
			}
		}
		
		
		// draw agents leds
		for (int i=0;i<m_env.platform().nbAgent();i++){
			
			drawLeds(g2d, h, c_h, c_w, i, 0, 0.4, 0);
			drawLeds(g2d, h, c_h, c_w, i, 1, 0.4,  Math.PI/2);
			drawLeds(g2d, h, c_h, c_w, i, 2, 0.4, -Math.PI/2);
			drawLeds(g2d, h, c_h, c_w, i, 3, 0.4,  Math.PI);

		}
		
		
		// draw agents trace
		for (int i=0;i<m_env.platform().nbAgent();i++){
			for (int l=1;l<Agent.pathLenght;l++){
				g2d.setColor(Color.black);
				g2d.drawLine((int)(  m_env.platform().getAgent(i).path[l-1][0]*c_w)+(c_w/2),
							 (int)(h-m_env.platform().getAgent(i).path[l-1][1]*c_h)-(c_h/2),
							 (int)(  m_env.platform().getAgent(i).path[l  ][0]*c_w)+(c_w/2),
							 (int)(h-m_env.platform().getAgent(i).path[l  ][1]*c_h)-(c_h/2));
			}
		}
		
		
		// draw agent and simulationinformations
		//String counter ="#"+m_env.getIdent()+": "+m_env.platform().selectAgent(m_env.getIdent()).getStepNb();
		
		float score=m_env.platform().selectAgent(m_env.getIdent()).body.score;
		float nbStep=m_env.platform().selectAgent(m_env.getIdent()).getStepNb();
		String counter ="#"+m_env.getIdent()+": "+score+ " -> "+ (score*1000/(nbStep+1));
		
		Font font = new Font("Dialog", Font.BOLD, 18);
		g2d.setFont(font);
		FontMetrics fm = getFontMetrics(font);

		int width = fm.stringWidth(counter);
				
		g2d.setColor(Color.GRAY);		
		g2d.drawString(counter, m_env.getWidth()*c_w - c_w*1.1f - width, c_h*1.5f + 5);
	}

	///////////////////////////////////////////////////////////////////////////////////
	
	
	
	// add and remove elements
	public void mouseClicked(MouseEvent e){

		int h=this.getHeight();
		int w=this.getWidth();
		
		m_clicked=0;

		m_clickX= (e.getX() / (int)( (float)w/(float)m_env.getWidth() ));
		m_clickY= (e.getY() / (int)( (float)h/(float)m_env.getHeight() ));
		
		m_FclickX=(float)e.getX() / ((float)w/(float)m_env.getWidth() );
		m_FclickY=(float)e.getY() / ((float)h/(float)m_env.getHeight() );
		
		if (e.getButton() == MouseEvent.BUTTON1)
			if (e.isShiftDown()) m_clicked = 4;
			else m_clicked = 1;
		if (e.getButton() == MouseEvent.BUTTON2)
				if (e.isShiftDown()) m_clicked = 5;
				else m_clicked = 2;
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			if (e.isShiftDown()) m_clicked = 6;
			else m_clicked = 3;
		}
		
		m_env.drawGrid(m_clicked);
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
	
	///////////////////////////////////////////////////////////////////////////////////
	
    public void paintAgent(Graphics2D g2d,int x,int y,double rz,double sx,double sy,int parameter){
        // The orientation
        AffineTransform orientation = new AffineTransform();
        orientation.translate(x,y);
        orientation.rotate(-rz+Math.PI/2);
        orientation.scale(sx,sy);
        g2d.transform(orientation);

        // The shark body
        Area shark = shape(1);
        Arc2D.Double focus = new Arc2D.Double(-10, -35, 20, 20,0, 180, Arc2D.PIE);
        
        // Draw the body
        g2d.setColor(Environment.AGENT_COLOR);
        
        if (parameter==1) g2d.setColor(Color.red);
        
        g2d.fill(shark);
        g2d.fill(focus);
    }
    
    // display a "led"
	public void drawLeds(Graphics2D g2d, int h, int c_h, int c_w, int id, int led, double r, double theta){
		double x= (   m_env.platform().selectAgent(id).body.position[0]   *c_w+(c_w/2));
		double y= (h-(m_env.platform().selectAgent(id).body.position[1]+1)*c_h+(c_h/2));

		double th=m_env.platform().selectAgent(id).body.position[2];

		if (m_env.platform().selectAgent(id).body.display[led]>0){
			if (m_env.platform().selectAgent(id).body.display[led]==1) g2d.setColor(Color.red);
			else if (m_env.platform().selectAgent(id).body.display[led]==2) g2d.setColor(Color.green);
			else g2d.setColor(Color.blue);
			g2d.fillOval( (int)(x+r*Math.cos(th+theta)*c_w) -5, (int)(y-r*Math.sin(th+theta)*c_h)-5, 10, 10);
		}
	}
	
	// define shark mask
    public static Area shape(int ID){
        GeneralPath body = new GeneralPath();
        body.append(new CubicCurve2D.Double(0,-40, -30,-40, -5, 45, 0, 45), false);
        body.append(new CubicCurve2D.Double(0, 45,   5, 45, 30,-40, 0,-40), false);
        
        GeneralPath leftPectoralFin = new GeneralPath();
        leftPectoralFin.append(new CubicCurve2D.Double(-15, -15, -30, -10, -40, 0, -40, 20), false);
        leftPectoralFin.append(new CubicCurve2D.Double(-40,  20, -30,  10, -20, 8, -10, 10), true);

        GeneralPath leftPelvicFin = new GeneralPath();
        leftPelvicFin.append(new CubicCurve2D.Double(-10, 15, -15, 18, -20, 25, -15, 30), false);
        leftPelvicFin.append(new CubicCurve2D.Double(-15, 30,  -10, 25, -10,  25,  -5, 28), true);

        GeneralPath rightPectoralFin = new GeneralPath();
        rightPectoralFin.append(new CubicCurve2D.Double(15, -15, 30, -10, 40, 0, 40, 20), false);
        rightPectoralFin.append(new CubicCurve2D.Double(40,  20, 30,  10, 20, 8, 10, 10), true);

        GeneralPath rightPelvicFin = new GeneralPath();
        rightPelvicFin.append(new CubicCurve2D.Double(10, 15, 15, 18, 20, 25, 15, 30), false);
        rightPelvicFin.append(new CubicCurve2D.Double(15, 30, 10, 25, 10, 25,  5, 28), true);

        GeneralPath caudalFin = new GeneralPath();
        caudalFin.append(new CubicCurve2D.Double(10, 50, 15, 20, -15, 20, -10, 50), false);
        caudalFin.append(new CubicCurve2D.Double(-10, 50, -15, 30, 15, 30, 10, 50), false);

        Area shark = new Area(body);
    	shark.add(new Area(leftPectoralFin));
        if ((ID & 1) == 0)
        	shark.add(new Area(leftPelvicFin));
        shark.add(new Area(rightPectoralFin));
        if ((ID & 2) == 0)
        	shark.add(new Area(rightPelvicFin));
        
        return shark;
    }
    
    
    ////////////////////////////////////////////////////////////////////////
    
    public int getClickX(){
    	return m_clickX;
    }
    public int getClickY(){
    	return m_clickY;
    } 
    
    public float getFClickX(){
    	return m_FclickX;
    }
    public float getFClickY(){
    	return m_FclickY;
    }
}
