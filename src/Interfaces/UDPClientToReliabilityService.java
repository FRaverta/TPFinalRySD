package Interfaces;

/**
 * 
 * Declaration of methods that UDPClient provides to DSManager
 *
 */
public interface UDPClientToReliabilityService extends Runnable {
	
	public void send(String msg);

}
