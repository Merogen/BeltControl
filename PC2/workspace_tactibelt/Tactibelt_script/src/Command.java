import java.util.ArrayList;


public class Command {

	
	public int type;
	public ArrayList<String> messages;
	
	public Command(int t){
		type=t;
		messages=new ArrayList<String>();
	}
	
	public void addMessage(String m){
		messages.add(m);
	}
	
	public String getMessage(){
		if (messages.size()==1) return messages.get(0);
		else{
			int m=(int) (Math.random() * messages.size());
			return messages.get(m);
		}
	}
	
	public String getCommand(){
		String ret="";
		if (type==1) ret+="t ";
		else if (type==2) ret+="r ";
		else if (type==3) ret+="loop";
		
		for (int m=0;m<messages.size();m++){
			ret+=messages.get(m)+" ";
		}
		return ret;
	}
	
}
