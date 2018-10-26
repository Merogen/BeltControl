package platform;


import jssc.SerialPort;
import jssc.SerialPortException;

public class Communication {

	public SerialPort serialPort;
	
	
	int previous=-1;
	
	float[] histogram;
	
	int[] hist;

	
	public Communication(){
		try {
			serialPort = new SerialPort("/dev/ttyACM0");
			serialPort.openPort();
			serialPort.setParams(9600, 8, 1, 0);
		} catch (SerialPortException e) {e.printStackTrace();}
		
		histogram=new float[60];
		
		hist=new int[60];
	}
	
	public void close(){
		try {
			System.out.println("Port closed: " + serialPort.closePort());
		} catch (SerialPortException e) {e.printStackTrace();}
	}
	
	public void sendMsg(float angle, int pwm, int pulse){
		
	//public void sendMsg(float[] distances, float[] target){
		
		int id=-1;
		
		if (angle<10 || angle>=347.5) id=40;
		else if (angle>=10 && angle<30) id=41;
		else if (angle>=30 && angle<50) id=42;
		else if (angle>=50 && angle<70) id=43;
		else if (angle>=70 && angle<90) id=44;
		else if (angle>=90 && angle<110) id=45;
		else if (angle>=110 && angle<130) id=46;
		else if (angle>=130 && angle<150) id=47;
		
		else if (angle>=150 && angle<170) id=50;
		else if (angle>=170 && angle<192.5) id=51;
		
		else if (angle>=192.5 && angle<218) id=52;
		else if (angle>=218 && angle<243.5) id=53;
		else if (angle>=243.5 && angle<269) id=54;
		else if (angle>=269 && angle<294.5) id=55;
		else if (angle>=294.5 && angle<320) id=56;
		else if (angle>=320 && angle<347.5) id=56;
		
		/*for (int a=0;a<60;a++){
			histogram[a]=-1;
		}
		
		for (int a2=0;a2<360;a2++){
			
			int a=(a2-90+360)%360;
			//if (a==90) System.out.println(distances[a]);
			if (a<10 || a>=347.5){
				if (distances[a]>histogram[40]) histogram[40]=distances[a2];
			}
			else if (a>=10 && a<30){
				if (distances[a]>histogram[41]) histogram[41]=distances[a2];
			}
			else if (a>=30 && a<50){
				if (distances[a]>histogram[42]) histogram[42]=distances[a2];
			}
			else if (a>=50 && a<70){
				if (distances[a]>histogram[43]) histogram[43]=distances[a2];
			}
			else if (a>=70 && a<90){
				if (distances[a]>histogram[44]) histogram[44]=distances[a2];
			}
			else if (a>=90 && a<110){
				if (distances[a]>histogram[45]) histogram[45]=distances[a2];
			}
			else if (a>=110 && a<130){
				if (distances[a]>histogram[46]) histogram[46]=distances[a2];
			}
			else if (a>=130 && a<150){
				if (distances[a]>histogram[47]) histogram[47]=distances[a2];
			}
			
			else if (a>=150 && a<170){
				if (distances[a]>histogram[50]){
					histogram[50]=distances[a2];
					//System.out.println(histogram[50]);
				}
			}
			else if (a>=170 && a<192.5){
				if (distances[a]>histogram[51]) histogram[51]=distances[a2];
			}
			
			else if (a>=192.5 && a<218){
				if (distances[a]>histogram[52]) histogram[52]=distances[a2];
			}
			else if (a>=218 && a<243.5){
				if (distances[a]>histogram[53]) histogram[53]=distances[a2];
			}
			else if (a>=243.5 && a<269){
				if (distances[a]>histogram[54]) histogram[54]=distances[a2];
			}
			else if (a>=269 && a<294.5){
				if (distances[a]>histogram[55]) histogram[55]=distances[a2];
			}
			else if (a>=294.5 && a<320){
				if (distances[a]>histogram[56]) histogram[56]=distances[a2];
			}
			else if (a>=320 && a<347.5){
				if (distances[a]>histogram[57]) histogram[57]=distances[a2];
			}
			
		}
		
		//for (int a=0;a<60;a++){
		//	if (a!=50) histogram[a]=-1;
		//}*/
		
		// turn off previous motor
		if (previous!=id) stop();
		
		if (id!=-1){
			String msg="+";
			if (id<10) msg+="0";
			
			msg+=id+""+pwm+"590";
			
			System.out.println("sent : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}
			previous=id;
		}
		
		
		
		/*for (int v=0;v<60;v++){
			if (histogram[v]>=0){
				if (histogram[v]==0){
					stop(v);
				}
				else{
					
					int pwm=Math.max(0, Math.min(9, (int)(histogram[v]/3)-1));
					
					if (pwm!=hist[v]){
						System.out.println(v+" , "+histogram[v]+" ,  "+pwm);
						String msg="+";
						if (v<10) msg+="0";
						
						msg+=v+""+pwm+"000";
						
						System.out.println("sent 1 : "+msg);
						try {
							serialPort.writeBytes(msg.getBytes());
						} catch (SerialPortException e) {e.printStackTrace();}
						//System.out.println(" +++ "+v+" ; "+(int)(histogram[v]/2));
						
						hist[v]=pwm;
					}
				}
			}
		}*/
		
		// target
		/*for (int a2=0;a2<360;a2++){
			
			int angle=(a2-90+360)%360;
			
			if (target[angle]>0){
			
				if (angle<10 || angle>=347.5) id=40;
				else if (angle>=10 && angle<30) id=41;
				else if (angle>=30 && angle<50) id=42;
				else if (angle>=50 && angle<70) id=43;
				else if (angle>=70 && angle<90) id=44;
				else if (angle>=90 && angle<110) id=45;
				else if (angle>=110 && angle<130) id=46;
				else if (angle>=130 && angle<150) id=47;
				
				else if (angle>=150 && angle<170) id=50;
				else if (angle>=170 && angle<192.5) id=51;
				
				else if (angle>=192.5 && angle<218) id=52;
				else if (angle>=218 && angle<243.5) id=53;
				else if (angle>=243.5 && angle<269) id=54;
				else if (angle>=269 && angle<294.5) id=55;
				else if (angle>=294.5 && angle<320) id=56;
				else if (angle>=320 && angle<347.5) id=56;
			}
		}
		if (id>=0){
			String msg="+";
			if (id<10) msg+="0";
			
			msg+=id+"8590";
			
			System.out.println("sent 1 : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}
		}*/
	}
	
	public void stop(){
		if (previous>=0){
			String msg="+";
			if (previous<10) msg+="0";
			msg+=previous+"0000";
			System.out.println("sent 2 : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}
		}
	}
	
	public void stop(int id){
		if (previous>=0){
			String msg="+";
			if (id<10) msg+="0";
			msg+=id+"0000";
			System.out.println("sent 3 : "+msg);
			try {
				serialPort.writeBytes(msg.getBytes());
			} catch (SerialPortException e) {e.printStackTrace();}
		}
	}
	
    
}