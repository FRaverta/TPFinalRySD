package Interfaces;

/**
 * Declaration of methods that Agent provides to Model
 * 
 * */

public interface AgentToModel {
	
	/** reserve n seats in distributed system if it can be done */
	public void reserve(int n);
	
	/** free n seats in distributed system if it can be done */
	public void free(int n);

}
