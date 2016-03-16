package Middleware;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represent a single message with the following fields:
 * 		_int id: process id. It will be unique in the distributed system.
 * 		_int action: represent the action that try to be performed. (n>0 represent: reserve n, n<0 represent: free n, n=0 represente: OK msg)
 * 		_int ts: time stamp of virtual clock for Ricart-Agrawala Algorithm 
 * */
public class Message {

	protected final int id,action,ts;
	
	public Message(int id,int ts,int action){
		this.id = id;
		this.ts = ts;
		this.action = action;
	}
	
	/**
	 * Create a Message from a String representing a JSON Object
	 * 
	 * @param msg- JSON Object Message   
	 * @throws JSONException 
	 * */
	public Message(String msg) throws JSONException{
		JSONObject o = new JSONObject(msg);
		id = o.getInt("id");
		action = o.getInt("action");
		ts = o.getInt("ts");			
	}
	
	/**
	 * Construct a JSON Object for current message
	 * 
	 * @return JSONObject resenting current message 
	 * @throws JSONException 
	 */
	public JSONObject asJSONObject() throws JSONException{
		JSONObject result = new JSONObject();
		result.put("id",id);
		result.put("action",action);
		result.put("ts",ts);
		return result;
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
