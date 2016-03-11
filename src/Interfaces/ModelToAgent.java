package Interfaces;

/**
 * 
 * Declaration of methods that Model provides to Agent
 *
 */
public interface ModelToAgent {
	
	/** Try to reserve n seats, if it could be done the change will be permanent else it do nothing*/
	public void reserveRequest(int n);

	/** Try to free n seats, if it could be done the change will be permanent else it do nothing*/
	public void freeRequest(int n);

}
