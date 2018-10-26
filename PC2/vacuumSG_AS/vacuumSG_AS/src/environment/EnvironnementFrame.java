package environment;

import java.awt.Graphics;

import platform.PrintableFrame;


public class EnvironnementFrame extends PrintableFrame{

	private static final long serialVersionUID = 1L;
	
	private EnvironnementPanel m_envp;
	
    public EnvironnementFrame(EnvironnementPanel panel){
    	super();
    	this.setTitle(Environment.WindowTitle);
    	this.setSize(670, 610);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	printable=true;
    	m_envp = panel;
    	getContentPane().add(m_envp);
		indexImage=0;
    }
	
	public void drawPDF(Graphics g){
		m_envp.drawPDF(g);
	}
	
    public void repaint(){
    	m_envp.repaint();
    }

}
