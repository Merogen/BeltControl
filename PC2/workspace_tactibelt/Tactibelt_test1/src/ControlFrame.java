import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;



public class ControlFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	ControlPanel panel;
	
	Main main;
	
	public ControlFrame(Main m){
		
		super();
		
		main=m;
		
		this.setTitle("Control");
    	this.setSize(500, 700);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
    	
    	this.addWindowListener(new WindowAdapter() {
    	      public void windowClosing(WindowEvent e) {
    	    	main.close();
    	        System.exit(0);
    	      }
    	    });

    	panel=new ControlPanel(m);
    	
    	this.setContentPane(panel);
	}
}