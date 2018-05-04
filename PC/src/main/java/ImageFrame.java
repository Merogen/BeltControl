import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class ImageFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	Main main;
	
	protected ImagePanel panel;
	
	public ImageFrame(Main m){
		
		main=m;
		
		this.setTitle("Image");
    	this.setSize(650, 600);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	panel=new ImagePanel(m);
    	this.setContentPane(panel);
    	
    	this.addWindowListener(new WindowAdapter() {
  	      public void windowClosing(WindowEvent e) {
  	    	main.inter.close();
  	        System.exit(0);
  	      }
  	    });
	}
	
}
