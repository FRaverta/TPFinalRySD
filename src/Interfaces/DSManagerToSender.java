package Interfaces;

import org.json.JSONException;

/**
 * 
 * Declaration of methods that UDPClient provides to DSManager
 *
 */
public interface DSManagerToSender{
	
	public String getMsgForSend() throws InterruptedException, JSONException;

}
