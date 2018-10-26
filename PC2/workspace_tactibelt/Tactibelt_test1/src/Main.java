import jssc.SerialPort;
import jssc.SerialPortException;

public class Main {

	SerialPort serialPort;
	
	ControlFrame controlFrame;
	
	int previous=-1;
	
	public Main(){
		try {
			serialPort = new SerialPort("/dev/ttyACM0");
			serialPort.openPort();
			serialPort.setParams(19200, 8, 1, 0);
		} catch (SerialPortException e) {e.printStackTrace();}
		
		controlFrame=new ControlFrame(this);
	}
	
	public void close(){
		try {
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	public void sendMsg(float angle, int pwm, int pulse, int stack){
		
		System.out.println(angle);
		
		int id=-1;
		
		if (stack==0){
		
			if (angle<10 || angle>=347.5) id=04;
			else if (angle>=10 && angle<30) id=03;
			else if (angle>=30 && angle<50) id=02;
			else if (angle>=50 && angle<70) id=01;
			else if (angle>=70 && angle<90) id=00;
			
			else if (angle>=90 && angle<110) id=50;
			else if (angle>=110 && angle<130) id=51;
			else if (angle>=130 && angle<150) id=52;
			else if (angle>=150 && angle<170) id=53;
			else if (angle>=170 && angle<192.5) id=54;
			
			else if (angle>=192.5 && angle<218) id=55;
			else if (angle>=218 && angle<243.5) id=56;
			else if (angle>=243.5 && angle<269) id=57;
			
			else if (angle>=269 && angle<294.5) id=07;
			else if (angle>=294.5 && angle<320) id=06;
			else if (angle>=320 && angle<347.5) id=05;
		}
		
		if (stack==1){
			
			if (angle<10 || angle>=347.5) id=14;
			else if (angle>=10 && angle<30) id=13;
			else if (angle>=30 && angle<50) id=12;
			else if (angle>=50 && angle<70) id=11;
			else if (angle>=70 && angle<90) id=10;
			
			else if (angle>=90 && angle<110) id=40;
			else if (angle>=110 && angle<130) id=41;
			else if (angle>=130 && angle<150) id=42;
			else if (angle>=150 && angle<170) id=43;
			else if (angle>=170 && angle<192.5) id=44;
			
			else if (angle>=192.5 && angle<218) id=45;
			else if (angle>=218 && angle<243.5) id=46;
			else if (angle>=243.5 && angle<269) id=47;
			
			else if (angle>=269 && angle<294.5) id=17;
			else if (angle>=294.5 && angle<320) id=16;
			else if (angle>=320 && angle<347.5) id=15;
		}
		
		if (stack==2){
			
			if (angle<10 || angle>=347.5) id=24;
			else if (angle>=10 && angle<30) id=23;
			else if (angle>=30 && angle<50) id=22;
			else if (angle>=50 && angle<70) id=21;
			else if (angle>=70 && angle<90) id=20;
			
			else if (angle>=90 && angle<110) id=30;
			else if (angle>=110 && angle<130) id=31;
			else if (angle>=130 && angle<150) id=32;
			else if (angle>=150 && angle<170) id=33;
			else if (angle>=170 && angle<192.5) id=34;
			
			else if (angle>=192.5 && angle<218) id=35;
			else if (angle>=218 && angle<243.5) id=36;
			else if (angle>=243.5 && angle<269) id=37;
			
			else if (angle>=269 && angle<294.5) id=27;
			else if (angle>=294.5 && angle<320) id=26;
			else if (angle>=320 && angle<347.5) id=25;
		}
		
		if (id!=-1){
			
			// turn off previous motor
			if (id!=previous) stop();
			
			String msg="+";
			if (id<10) msg+="0";
			msg+=id+""+pwm+"000";
			System.out.println("sent : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}/**/
			
			/*String msg="-";
			if (id<10) msg+="0";
			msg+=id+""+pwm;
			System.out.println("sent : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}/**/
			
			previous=id;
		}
	}
	
	public void stop(){
		if (previous>=0){
			/*String msg="+";
			if (previous<10) msg+="0";
			msg+=previous+"0000";
			System.out.println("sent : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}*/
			
			String msg="-";
			if (previous<10) msg+="0";
			msg+=previous+"0";
			System.out.println("sent : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}
		}
	}
	
	
	public static void main(String[] args) {
		
		Main main=new Main();
		
		while (true){
			
			main.controlFrame.repaint();
			
			try {Thread.sleep(10);} 
			catch (InterruptedException e){e.printStackTrace();}
			
		}
	}
    
}