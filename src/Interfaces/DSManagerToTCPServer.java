package Interfaces;

/**
 * 
 * Declaration of methods that Model provides to TCPServer
 *
 */
public interface DSManagerToTCPServer {
	
	/** reserve n seats 
	 * @throws InterruptedException */
	public boolean reserve(int n) throws InterruptedException;
	
	/** free n seats 
	 * @throws InterruptedException */
	public boolean free(int n) throws InterruptedException;
	
	/** Get the number of available seats */
	public int available();

}
