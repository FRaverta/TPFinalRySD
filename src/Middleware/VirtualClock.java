package Middleware;

public class VirtualClock {
	
	int vc;
	
	public VirtualClock(){
		vc = 0;
	}
	
	/** Increment Lamport's virtual clock. This operation must be performed 
	 *  in the following cases:
	 *  	_ A message from Agent should be send.
	 * 		_ Message from queue should be popped.
	 * */
	public synchronized int inc(){
		vc++;
		return vc;
	}
	
	/**
	 * Update Lamport's virtual clock according received message.
	 * If received message m has m.ts > vc then vc := ts else do nothing.
	 * */
	public synchronized void update(int ts){	
		if(vc < ts)	vc = ts;
	} 
	

}
