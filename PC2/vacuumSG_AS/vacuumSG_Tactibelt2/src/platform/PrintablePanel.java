package platform;

import java.awt.Graphics;

import javax.swing.JPanel;

public class PrintablePanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public PrintablePanel(){

	}

	public void drawPDF(Graphics g){
		paintComponent(g);
	}
	
	public void paintComponent(Graphics g){

	}
	
}
