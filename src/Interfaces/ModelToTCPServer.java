package Interfaces;

/**
 * 
 * Declaration of methods that Model provides to TCPServer
 *
 */
public interface ModelToTCPServer {
	
	/** reserve n seats */
	public boolean reserve(int n);
	
	/** free n seats */
	public boolean free(int n);
	
	/** Get the number of available seats */
	public int available();

}
