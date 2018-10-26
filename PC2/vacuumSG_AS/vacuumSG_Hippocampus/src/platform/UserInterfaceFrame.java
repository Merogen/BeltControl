package platform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import environment.Environment;

/** User interface
 * 
 * @author simon
 *
 */
public class UserInterfaceFrame extends JFrame implements Observer, ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;

	public String version="Version1";
	
	public Main main;
	
	private final JLabel m_statusBar = new JLabel();
	private JButton m_save = new JButton("Save");
	private JButton m_run = new JButton("Play");
	private JButton m_stop = new JButton("Pause");
	private JButton m_reset = new JButton("Reset");
	
	private JButton m_clear = new JButton("Clear");			// clear traces
	
	private JButton m_arun = new JButton("Play agent");		// play selected agent
	private JButton m_astop = new JButton("Pause Agent");   // pause selected agent

	
	public UserInterfaceFrame(Main m){
		
		main=m;
		
		this.setTitle("Interface");
    	this.setSize(900, 70);
    	this.setLocationRelativeTo(null);               
    	this.setVisible(true);
		
		m_stop.addActionListener(this);
		m_run.addActionListener(this);
		m_reset.addActionListener(this);
		
		
		
		JPanel buttonPanel = new JPanel();

		buttonPanel.addKeyListener(this);
		
		buttonPanel.add(m_save);
		
		buttonPanel.add(m_arun);
		buttonPanel.add(m_astop);

		buttonPanel.add(m_reset);		
		buttonPanel.add(m_clear);
		
		buttonPanel.add(m_run);
		buttonPanel.add(m_stop);
		
		setEnableButton();
		m_save.addActionListener(this);
		m_stop.addActionListener(this);
		m_run.addActionListener(this);
		m_reset.addActionListener(this);
		
		m_clear.addActionListener(this);
		
		m_astop.addActionListener(this);
		m_arun.addActionListener(this);

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.add(m_statusBar, BorderLayout.CENTER);
		statusPanel.add(buttonPanel, BorderLayout.EAST);
		statusPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		getContentPane().add(statusPanel, BorderLayout.SOUTH);

		m_statusBar.setText("Ready.");
		m_statusBar.setPreferredSize(new Dimension(200, m_statusBar.getHeight()));
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		    	//main.getAgent(0).communication.close();
		        main.stop();
		        System.exit(0); 
		    }
		});
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Run agents ******
		if (e.getSource() == m_run){
			for (int i=0;i<main.nbAgent();i++){
				main.selectAgent(i).startAgent();
			}
		}
		
		// Stops agents *****
		else if (e.getSource() == m_stop){
			for (int i=0;i<main.nbAgent();i++){
				main.selectAgent(i).stopAgent();
			}
		}
		
		// save agent *****
		if (e.getSource() == m_save){
			main.selectAgent(main.getAgentIndex()).save();
			main.getEnv().save();
		}
		
		// Run selected agent ******
		if (e.getSource() == m_arun){
			main.selectAgent(main.getAgentIndex()).startAgent();
		}
		
		// Stop selected agent *****
		else if (e.getSource() == m_astop){
			main.selectAgent(main.getAgentIndex()).stopAgent();
		}
		
		
		// Reset the board ******
		else if (e.getSource() == m_reset){

			try{ 
				main.resetAgentList();
				main.getEnv().setIndex(0);
				main.getEnv().init(Environment.DEFAULT_BOARD);
				
				if (main.nbAgent()>0){
					main.getAgent(0).setDisplay();
				}
			}
			catch (Exception ex){
			}
		}
		
		// clear agent trace
		else if (e.getSource() == m_clear){
			main.selectAgent(main.getAgentIndex()).clearTrace();
		}

		setEnableButton();
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setEnableButton(){
		m_stop.setEnabled(!main.isStopped());
		m_run.setEnabled(!main.isStarted());
		m_reset.setEnabled(true);
		
		m_arun.setEnabled(main.isStopped(main.getAgentIndex()));
		m_astop.setEnabled(main.isStarted(main.getAgentIndex()));
	}
}
