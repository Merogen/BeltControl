package platform;

import java.io.IOException;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.util.Enumeration;


public class Communication{

	SerialPort serialPort;
	/** The port we’re normally going to use. */
	private static final String PORT_NAME = "/dev/ttyS80";

	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public Communication() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(PORT_NAME)) {
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			output = serialPort.getOutputStream();

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}


	public void writeVibrator(int id, int pwm, int tempo){
		try {
			output.write(stringToBytesASCII("+0"+id+pwm+tempo));
			/*if (id<10){
				output.write(stringToBytesASCII("+0"+id+pwm+tempo));
			}
			else{
				output.write(stringToBytesASCII("+"+id+pwm+tempo));
			}*/
		} catch (IOException e1) {e1.printStackTrace();	}
	}
	
	//public void writeVibrator(int[][])
	
	/*public static void main(String[] args) throws Exception {
		Communication main = new Communication();
		
		System.out.println("Started");
		
		while (true){
			
			for (int i=1;i<10;i++){
				for (int j=1;j<10;j++){
					try {
						main.output.write(stringToBytesASCII("+0"+j+i));
					} catch (IOException e1) {e1.printStackTrace();	}
					
					try{Thread.currentThread();
					Thread.sleep(1000);}
					catch(Exception ie){}
				}
			}
		}
	}*/
	
	public static byte[] stringToBytesASCII(String str) {
		char[] buffer = str.toCharArray();
		byte[] b = new byte[buffer.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) buffer[i];
		}
		return b;
	}
}

/*import gnu.io.*;
import java.util.Enumeration;

public class Communication {

	public static void main(String[] args) {
		System.out.println("Program Started!!!");

		CommPortIdentifier serialPortId;

		Enumeration enumComm;

		enumComm = CommPortIdentifier.getPortIdentifiers();

		while(enumComm.hasMoreElements()){
			serialPortId = (CommPortIdentifier)enumComm.nextElement();
			if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL){
				System.out.println(serialPortId.getName());
			}
		}

		System.out.println("Program Finished Sucessfully");
	}

}

/*import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
 
import java.io.OutputStream;


public class Communication {

	private int rate=19200;
	
	public OutputStream outArduino;
	
	public Communication(){
		super();
		
		try {
			connect("/dev/ttyACM0");
		} catch (Exception e) {	e.printStackTrace();}
	}
	
	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);
 
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				//serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				//serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.setSerialPortParams(rate, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
 
 
				outArduino = serialPort.getOutputStream();
 
			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}
	
}*/
