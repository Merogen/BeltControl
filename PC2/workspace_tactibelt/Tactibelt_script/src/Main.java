import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortException;


public class Main {

	
	SerialPort serialPort;

	
	ArrayList<String> scriptList;
	
	ArrayList<Command> commandList;
	ArrayList<Integer> tempoList;
	
	ArrayList<Integer> pile_ret;
	ArrayList<Integer> counters;
	
	ControlFrame controlFrame;
	
	public int ip=0;
	
	public boolean run=false;
	public boolean pause=false;
	public boolean stop=false;
	
	public Main(){
		
		// open port
		try {
			//serialPort = new SerialPort("/dev/ttyACM0");
			serialPort = new SerialPort("/dev/ttyACM0");
			serialPort.openPort();
			serialPort.setParams(19200, 8, 1, 0);
		} catch (SerialPortException e) {e.printStackTrace();}
		
		
		// list script files
		String[] scripts= (new File(".")).list();
		scriptList=new ArrayList<String>();
		for (int s=0;s<scripts.length;s++){
			if (scripts[s].contains(".tbs")) scriptList.add(scripts[s]);
		}
		
		
		commandList=new ArrayList<Command>();
		tempoList=new ArrayList<Integer>();
		
		pile_ret=new ArrayList<Integer>();
		counters=new ArrayList<Integer>();
		
		controlFrame=new ControlFrame(this);
		
		// open and read file
        //readScript("tactibelt1.txt");
		//executeScript();
		
		
		// close port
		//close();
	}
	
	public void readScript(String fileName){
		
		commandList.clear();
		ip=0;
		try {
			InputStream ips=new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			String[] elements;
			
			line=br.readLine();
			
			while (line!=null){
				elements=line.split(" ");

				if (elements.length>0){
					
					// case comments
					if (elements[0].equals("#")){
						//System.out.println(line);
						commandList.add(new Command(4));
						commandList.get(commandList.size()-1).addMessage(line);
					}
					else if (elements[0].equals("t")){
						commandList.add(new Command(1));
						commandList.get(commandList.size()-1).addMessage(elements[1]);
					}
					else if (elements[0].equals("r")){
						commandList.add(new Command(2));
						commandList.get(commandList.size()-1).addMessage(elements[1]);
					}
					else if (elements[0].equals("loop")){
						commandList.add(new Command(3));
					}
					else{
						commandList.add(new Command(0));
						for (int i=0;i<elements.length;i++){
							commandList.get(commandList.size()-1).addMessage(elements[i]);
						}
					}
				}
				line=br.readLine();
			}
		}
		catch (Exception e) {
			System.out.println("no file found");
		}
	}
	
	
	public void executeScript(){

		// execute script
		for (int ip=0;ip<commandList.size();ip++){
			
			// pause
			while (pause){
				try {Thread.sleep(200);} 
				catch (InterruptedException e){e.printStackTrace();}
			}
			
			if (commandList.get(ip).type==0){
				sendMsg(commandList.get(ip).getMessage());
			}
			else if (commandList.get(ip).type==1){
				try {Thread.sleep(Integer.parseInt(commandList.get(ip).getMessage()));} 
				catch (InterruptedException e){e.printStackTrace();}
			}
			else if (commandList.get(ip).type==2){
				pile_ret.add(0,ip);
				counters.add(0,Integer.parseInt(commandList.get(ip).getMessage()));
			}
			else if (commandList.get(ip).type==3){
				counters.set(0, counters.get(0)-1);
				if (counters.get(0)<=0){
					counters.remove(0);
					pile_ret.remove(0);
				}
				else{
					ip=pile_ret.get(0);
				}
			}
			else if (commandList.get(ip).type==4){
				System.out.println(commandList.get(ip).getCommand());
			}
			
			controlFrame.repaint();
			
			if (stop){
				stop();
			}
			
		}
		
		
		if (stop) System.out.println("INTERRUPTED");
		else System.out.println("DONE");
		run=false;
		stop=false;
		pause=false;
		
	}
	
	
	public void stop(){
		commandList.clear();
		pile_ret.clear();
		for (int i=0;i<6;i++){
			for (int j=0;j<8;j++){
				sendMsg("-"+i+j+"0");
			}
		}
	}
	
	public void close(){
		try {
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	
	public void sendMsg(String msg){
		System.out.println("sent : "+msg);
		try {
			serialPort.writeBytes(msg.getBytes());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	
	
	public static void main(String[] args) {
		
		Main main=new Main();
		
		while (true){
			
			main.controlFrame.repaint();
			
			if (main.run) main.executeScript();
			
			try {Thread.sleep(10);} 
			catch (InterruptedException e){e.printStackTrace();}
		}
	}
	
}
