package environment;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import platform.Main;
import platform.PrintableFrame;


/**
 * virtual environment for the agent
 * @author simon
 */
public class Environment{

	public static String WindowTitle = "Vacuum-SG";

    public Main platform;
    
	private PrintableFrame m_board;
		
	// environment file
	public static final String DEFAULT_BOARD = "Board10x10_2.txt";
	
	private EnvironnementFrame m_envFrame;
	private EnvironnementPanel m_envPanel;
	private Block[][] m_blocks;
	
	//////////////////////////////////////////////////////////////////////
	// define environment properties
	
	// tactile properties
	public static final int EMPTY         = 0;
	public static final int SMOOTH        = 1;
	public static final int FOOD          = 2;
	public static final int HARD		  = 3;
	
	// visual properties
	public static final Color FIELD_COLOR = Color.white;
	public static final Color WALL1       = new Color(  0,128,  0);
	public static final Color ALGA1       = new Color(220,50, 50);
	public static final Color FISH1       = new Color(150,128,255);
	public static final Color AGENT_COLOR = new Color(128,128,128);
	
	// blocks
	public static Block empty=new Block(EMPTY, FIELD_COLOR,"empty");
	public static Block wall =new Block(HARD , WALL1,"wall1");
	public static Block alga=new Block(SMOOTH,ALGA1,"alga1");
	public static Block fish =new Block(FOOD  ,FISH1,"fish");
	public static Block marker=new Block(EMPTY  ,FISH1,"marker");
	
	/////////////////////////////////////////////////////////////////////////////////////////					
	private int id;								// ident nb of the selected agent
	private int index;							// index of the selected agent
	
	private int m_w;
	private int m_h;
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	public Environment(Main m){

		platform=m;
		
		try{this.init(DEFAULT_BOARD);}
		catch (Exception e){System.out.println("error initializing the board");}
		
		m_envPanel=new EnvironnementPanel(this);
		m_envFrame=new EnvironnementFrame(m_envPanel);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	// initialize environment
	public void init(String f) throws Exception{
		
		int index_new=0; // number of last created agents
		
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader(f));

			List<String> lines = new ArrayList<String>();
			String line = "";
			while ((line = br.readLine()) != null){ 
				line = line.trim();
				if (line.length() != 0)
					lines.add(line); 
			}
			
			m_h = lines.size();
			m_w = (lines.get(0).toString().length() + 1) / 2;
			m_blocks=new Block[m_w][m_h];
			
			if (m_h <= 0 || m_w <= 0)
				throw new IllegalStateException("Invalid width or height!");
			
			int y = 0;

			for (Iterator<String> i = lines.iterator(); i.hasNext(); ){
				line = (String)i.next();
				if (((line.length() + 1) / 2) != m_w)
					throw new IllegalStateException("Width must be consistent!");

				String[] square = line.split(" ");
				
				for (int x = 0; x < m_w; x++){
					m_blocks[x][m_h-1-y]=empty;
					
					// Ernest agents
					if (square[x].equalsIgnoreCase("^") || square[x].equalsIgnoreCase(">") ||
						square[x].equalsIgnoreCase("v") || square[x].equalsIgnoreCase("<"))
					{
						platform.addAgent(index_new);
						platform.lastAgent().setEnvironment(this);
						
						float[] position=new float[3];
						position[0]=x;
						position[1]=m_h-1-y;

						if (square[x].equalsIgnoreCase("^"))
							position[2]=(float) (Math.PI/2);
						else if (square[x].equalsIgnoreCase(">"))
							position[2]=0;
						else if (square[x].equalsIgnoreCase("v"))
							position[2]=(float)(-Math.PI/2);
						else if (square[x].equalsIgnoreCase("<"))
							position[2]=(float) Math.PI;
						
						platform.agentPosition(position);
						
						id=index_new;
						index=platform.agentIndex(id);
						
						index_new++;
					}
					
					// fish
					if (square[x].equals("*")){
						m_blocks[x][m_h-1-y]=fish;
					}
					
					// wall
					if (square[x].equalsIgnoreCase("w")){
						m_blocks[x][m_h-1-y]=wall;
					}
					
					// alga
					if (square[x].equalsIgnoreCase("a")){
						m_blocks[x][m_h-1-y]=alga;
					}
					
					// alga
					if (square[x].equalsIgnoreCase("o")){
						m_blocks[x][m_h-1-y]=marker;
					}
				}
				y++;
			}
			
			if (platform.nbAgent()<=0)
				throw new IllegalStateException("error 404 : Agents not found!");

		}
		catch (Exception e){throw e;}
		finally{try { br.close(); } catch (Exception e) {}}	
		
		System.out.println("initialized ") ;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////
	//repaint environment
	public void drawGrid(){
		resizeGrid();
		m_envFrame.repaint();
	}
	
	// add or remove elements
	public void drawGrid(int c){
		
		int l_h = getHeight();
		
		// click left : change agent
		if (c == 1){
			int ident=platform.agentId(m_envPanel.getFClickX(), m_envPanel.getFClickY() );
			int index2=platform.agentIndex(m_envPanel.getFClickX(), m_envPanel.getFClickY() );
			if (ident!=-1){
				id=ident;
				index=index2;
				platform.selectAgent(index2).setDisplay();
				
				System.out.println("selected agent #"+id);

			}
		}
		
		// click wheel : add or remove marker
		/*if (c == 2){
			if (isFood(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
				m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= empty;
			}
			else{
				if (isEmpty(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
					m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= fish;
				}
			}
		}*/
		if (c == 2){
			if (isEmpty(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
				
				// remove previous marker
				for (int i=0;i<m_w;i++){
					for (int j=0;j<m_h;j++){
						if (isEmpty(i,j) ){
							m_blocks[i][j]= empty;
						} 
					}
				}
				
				m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= marker;
				platform.getAgent(id).body.marker_x=m_envPanel.getClickX();
				platform.getAgent(id).body.marker_y=m_envPanel.getClickY();
			}
		}
		
		// click right : add or remove wall
		if (c == 3){
			if (isWall(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
				m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= empty;

			}
			else{
				if (isEmpty(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
					m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= wall;
				}
			}
		}
		
		// click right + shift : add or remove alga
		if (c == 6){
			if (isAlga(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
				m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= empty;
			}
			else{
				if (isEmpty(m_envPanel.getClickX(),l_h-1-m_envPanel.getClickY() )){
					m_blocks[m_envPanel.getClickX()][l_h-1-m_envPanel.getClickY() ]= alga;
				}
			}
		}
		
		resizeGrid();
		m_envFrame.repaint();
	}
	
	// change environment window size
	private void resizeGrid(){
		
		m_envPanel.setPreferredSize(new Dimension(40*m_w,40*m_h));
		
		if (m_board != null)
			m_board.add(m_envPanel);
		
		m_envFrame.repaint();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////

	public void update(Observable o, Object arg){	
		m_envPanel.repaint();
	}

	public boolean isEmpty(float x, float y){
		return m_blocks[Math.round(x)][Math.round(y)].isEmpty();
	}
	public boolean isWall(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].isWall()); 
	}
	public boolean isFood(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].isFood()); 
	}
	public boolean isAlga(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].isAlga()); 
	}
	
	public Color seeBlock(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].seeBlock()); 
	}
	public int touchBlock(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].touchBlock()); 
	}
	public boolean isVisible(float x, float y){ 
		return 	(m_blocks[Math.round(x)][Math.round(y)].isVisible()); 
	}
	
	public boolean isWalkthroughable(float x, float y){ 
		return m_blocks[Math.round(x)][Math.round(y)].isWalkthroughable(); 
	}
	
	public void setBlock(float x, float y,Block b){
		m_blocks[Math.round(x)][Math.round(y)]=b;
	}
	
	public int getWidth()
	{ return m_w; }

	public int getHeight()
	{ return m_h; }
	
	
	public Main platform(){
		return platform;
	}
	
	public int getIndex(){
		return index;
	}
	public void setIndex(int i){
		index=i;
	}
	
	public int getIdent(){
		return id;
	}
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	// save information about environment
	public void save(){
		
	}
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	
}
