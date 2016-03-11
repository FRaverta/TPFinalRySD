package Middleware;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.*;

/**
 * This class represent a single message with the following fields:
 * 		_int id: process id. It will be unique in the distributed system.
 * 		_int action: represent the action that try to be performed. (n>0 represent: reserve n, n<0 represent: free n, n=0 represente: OK msg)
 * 		_int ts: time stamp of virtual clock for Ricart-Agrawala Algorithm 
 * */
public class Message extends JSONObject {

	public Message(int id,int action) throws JSONException{
		super();
		put("id", new Integer(id));
		put("action", new Integer(action));
		put("ts", new Integer(0));		
	}
	
	public Message(String msg) throws JSONException{
		super(msg);
	}
	
	public int getId() throws JSONException{		
		return getInt("id");
	}
	
	public int getAction() throws JSONException{		
		return getInt("action");
	}

	public int getTs() throws JSONException{		
		return getInt("ts");
	}
	
	public void setTs(int ts) throws JSONException{
		this.put("ts", ts);
	}

/*	
	public static void main(String args[]){
		
		try {
			Message m1 = new Message(1,2);
			Message m2 = new Message(2,3);
			m1.setTs(10);
			System.out.println(m1.toString());
			m1.setTs(15);
			System.out.println(m1.toString());
			String sm1 = m1.toString();
			String sm2 = m1.toString();
			Message mm1 =  new Message(sm1);
			System.out.println("id: " + mm1.getInt("id") + " " + mm1.getInt("action") + " " + mm1.getInt("ts") );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
*/
}
