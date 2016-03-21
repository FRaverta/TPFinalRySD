package Interfaces;

import java.io.IOException;
import java.net.SocketException;

import org.json.JSONException;

/**
 * 
 * Declaration of methods that UDPClient provides to DSManager
 *
 */
public interface DSManagerToSender{
	
	public String getMsgForSend() throws InterruptedException, JSONException;

}
