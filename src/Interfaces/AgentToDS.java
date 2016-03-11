package Interfaces;

import Middleware.Message;

/**
 * Declaration of methods that Agent provides to DSManager
 * 
 * */
public interface AgentToDS {
	
	/** Receive a message from Distributed System */
	public void receive(Message msg);
}
