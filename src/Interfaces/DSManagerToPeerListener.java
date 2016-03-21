package Interfaces;

import org.json.JSONException;

import Middleware.Message;

/**
 * Declaration of methods that Agent provides to DSManager
 * 
 * */
public interface DSManagerToPeerListener{

	/** Receive a message from Distributed System 
	 * @throws JSONException */
	public void receive(String msg) throws JSONException;
}
