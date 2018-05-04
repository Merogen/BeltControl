import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Interface  implements SerialPortEventListener {

	SerialPort serialPort;
	
	Main main;
	
	int previous=-1;
	
	ArrayList<Character> message;
	
	int message_type=0;  // 0 waiting, 1 : x, 2 : y
	
	String posx="";
	String posy="";
	
	public boolean ready=false;
	public int joystickX=0;
	public int joystickY=0;
	
	
	public Interface(Main m){
		
		main=m;
		
		message=new ArrayList<Character>();
		
		try {
			serialPort = new SerialPort("/dev/ttyUSB0");
			serialPort.openPort();
			serialPort.setParams(9600, 8, 1, 0);
			
			//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
            //         					  SerialPort.FLOWCONTROL_RTSCTS_OUT);

			serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
			
			System.out.println("port opened");
		} catch (SerialPortException e) {e.printStackTrace();}
		
	}
	
	public void close(){
		try {
			
			serialPort.writeString("f000");
			serialPort.writeString("s000");
			serialPort.writeString("g500");
			serialPort.writeString("h500");
			
			serialPort.writeString("o500");
			serialPort.writeString("p500");
			
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	public void sendMsg(String msg){

		System.out.println("sent : "+msg);
		try {
			serialPort.writeString(msg);
		} catch (SerialPortException e) {e.printStackTrace();}
	}

	
	
	public void serialEvent(SerialPortEvent event) {

        if(event.isRXCHAR() && event.getEventValue() > 0) {
            try {
            	
                String received = serialPort.readString(event.getEventValue());
                //System.out.println("Received response: " + received);
                
                for(int i=0;i<received.length(); i++){
                	
                	// new message
                	if ((char)Character.codePointAt(received,i)=='a'){
                		message_type=1;
                		message.clear();
                	}
                	// pos x sended
                	else if ((char)Character.codePointAt(received,i)=='b'){
                		// correct
                		if (message_type==1){
                			posx="";
                    		for (int j=0;j<message.size();j++){
                    			posx+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=2;
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// pos y sended
                	else if ((char)Character.codePointAt(received,i)=='c'){
                		// correct
                		if (message_type==2){
                			posy="";
                    		for (int j=0;j<message.size();j++){
                    			posy+=message.get(j);
                    		}
                    		message.clear();
                    		message_type=0;
                    		
                    		if (!ready){
                    			joystickX=Integer.parseInt(posx);
                    			joystickY=Integer.parseInt(posy);
                    			ready=true;
                    		}
                    			
                    		//System.out.println(" +++ "+posx+" , "+posy);
                		}
                		// error in transmission
                		else{
                			message_type=0;
                			message.clear();
                			System.out.println("ERROR");
                		}
                	}
                	// sending data
                	else{
                		message.add((char)Character.codePointAt(received,i));
                	}
                }
                
            }
            catch (SerialPortException ex) {System.out.println("Error in receiving string from COM-port: " + ex);}
        }
    }

}