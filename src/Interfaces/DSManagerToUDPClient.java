package Interfaces;

import java.io.IOException;
import java.net.SocketException;

/**
 * 
 * Declaration of methods that UDPClient provides to DSManager
 *
 */
public interface DSManagerToUDPClient{
	
	public String getMsgForSend() throws InterruptedException;

}
