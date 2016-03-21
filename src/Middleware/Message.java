package Middleware;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represent a single message with the following fields:
 * 		_int id: process id. It will be unique in the distributed system.
 * 		_int action: represent the action that try to be performed. (n>0 represent: reserve n, n<0 represent: free -n, n=0 represente: OK msg)
 * 		_int ts: time stamp of virtual clock for Ricart-Agrawala Algorithm 
 * 
 * When action=0, the fields id and ts values are the values of these fields in the message for which this vote is intended.  
 * */

public class Message {

	protected final int id,action,ts;

	/**
	 * Class constructor
	 * 
	 * */
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
	 * Construct a JSON Object from current message
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
	
	/**
	 * Construct an string representation for current action.
	 * */
	public String toString(){
		try{
			return this.asJSONObject().toString();
		}catch(Exception e){e.printStackTrace();}
		return null;
			
	}

}
