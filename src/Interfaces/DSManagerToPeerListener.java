package Interfaces;

import org.json.JSONException;

/**
 * Declaration of methods that Agent provides to DSManager
 * 
 * */
public interface DSManagerToPeerListener{

	/** Receive a message from Distributed System 
	 * @throws JSONException */
	public void receive(String msg) throws JSONException;
}
