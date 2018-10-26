package platform;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * generic class of frame that can be exported as a pdf or jpeg file
 * @author simon
 *
 */
public class PrintableFrame extends JFrame implements Printable, KeyListener{

	private static final long serialVersionUID = 1L;
	
	protected boolean printable;
	protected int indexImage;
	
	public PrintableFrame(){
		addKeyListener(this);
		printable=false;
		indexImage=0;
	}
	
	// save a panel as a jpeg image
	public void saveImage(String path){
		
		String p=path;
		if (indexImage<10) p+="0000"+indexImage+".jpg";
		else if (indexImage<100 ) p+="000"+indexImage+".jpg";
		else if (indexImage<1000) p+="00" +indexImage+".jpg";
		else                      p+="0"  +indexImage+".jpg";
		
		 BufferedImage image = new BufferedImage(this.getWidth(),
				 this.getHeight(),
				 BufferedImage.TYPE_INT_RGB);
		 Graphics2D g2 = image.createGraphics();
		 this.paint(g2);
		 g2.dispose();
		 try {
			 ImageIO.write(image, "JPEG", new File(p));
		 } catch (Exception e) { }
		 indexImage++;
	}

	
	public void drawPDF(Graphics g){
	}
	
	// detect Ctrl + P and generate a pdf file
	public void keyPressed(KeyEvent e) {
			if (printable && e.isControlDown() && e.getKeyCode()==80 ){

				PrinterJob job = PrinterJob.getPrinterJob();

				job.setPrintable(this);
				boolean ok = job.printDialog();
				if (ok) {
					try {
						job.print();
					} catch (PrinterException ex) {}
				}
			}		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// generate a pdf file
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		
		// We have only one page, and 'page'
	    if (page > 0) {
	         return NO_SUCH_PAGE;
	    }
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.translate(pf.getImageableX(), pf.getImageableY());
	    
	    // Now we perform our rendering
	    drawPDF(g);

	    // tell the caller that this page is part
	    // of the printed document
	    return PAGE_EXISTS;
	}

	
	
}
