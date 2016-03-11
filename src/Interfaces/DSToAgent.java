package Interfaces;

import Middleware.Message;

/**
 * Declaration of methods that DSManager provides to Agent
 * 
 * */

public interface DSToAgent {
	
	/** Send a message for all Distributed System */
	public void send(Message msg);

}
