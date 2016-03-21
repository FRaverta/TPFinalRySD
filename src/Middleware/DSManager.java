package Middleware;

import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONException;

import Interfaces.DSManagerToTCPServer;
import Interfaces.DSManagerToSender;
import Interfaces.DSManagerToPeerListener;
import PointOfSale.State;
import main.Setting;

/**
 * This class manage current peer consistency with other peers in the Distributed System. For that, this class implements a small 
 * modification of Ricart-Agrawala's algorithm and a mechanism of active consistency. That is, all process arrive to an agreement
 * about the order of operation will be performed in all peers. 
 * 
 * Shortly, the procedure it the following: 
 * 
 *    _When a local action request arrive (the action's owner is this peer), it is enqueued and then a meesage is sended 
 *    to all peers in Distributed System with a time stamp given by a Lamport's virtual clock(it is incremented before 
 *    than current action request is enqueued ).
 *   
 *    _When an action request arrive, the Lamport's virtual clock is update (vc = max(local vitual clock, message time stamp))
 *     it enqueue this and send an "Ok" message  only if it don't know any other message with time stamp less than this. 
 * 		
 * 	  _When an "ok message" arrive, it look for the action that current message want to give the "ok" and vote that. 
 * 
 * Some action is desenqueue if only if it is in the top of the queue and it has the vote of all peers in distributed system. If it
 * happened a Lamport's virtual clock is increase and the action is desenqueue. Then, the system try to perform the action. It can be 
 * succesfull or not. It is succesfull if the action can be made without break the invariant (0 <= amount of availables seats in bus 
 * after execution <= MAX Seats). In other case it isn't succesfull and  it doesn't do changes in current state (amount of bus's seats).
 * 
 * */

public class DSManager implements DSManagerToTCPServer,DSManagerToPeerListener,DSManagerToSender{
	
	
	private Setting setting;
	
	/** Ricart-Agrawala's algorithm priority queue(ordering by message.ts): a message with the amount of votes that it has been obtained.*/
	private PriorityQueue<QueueMsg> queue; 
	
	/**Lamport virtual clock for Ricart-Agrawala's algorithm*/
	private VirtualClock vc;
	
	private List<Message> OkMsgList; 
	
	private Queue<Message> forSend;

	private State state;

	/**
	 * Class constructor
	 * 
	 * */
	public DSManager(Setting setting){
		vc = new VirtualClock();
		queue = new PriorityQueue<QueueMsg>();
		OkMsgList = new LinkedList<Message>();
		forSend = new LinkedList<Message>();
		this.setting = setting;		
		state = new State(setting.SEATS);
	}
	
/*********************************************************************************
 *                           DSManager to UDPServer
 * 
 *********************************************************************************/
	
	/**
	 * Receive a message from some peer in distributed system. 
	 * 
	 * 
	 * @param m - receive message
	 * @throws JSONException 
	 * */
	public synchronized void receive(String msg) throws JSONException {
		Message m = new Message(msg);
		if( m.action == 0 )
			receiveOk(m);
		else
			externalActionRequest(m);
	}
	

	
	
		


		
	/*********************************************************************************
	 *                           DSManager to TCPServer
	 * @throws InterruptedException 
	 * 
	 *********************************************************************************/
	
	/**
	 * Receive a "reserve seats" request from local peer.
	 * */
	public boolean reserve(int n) throws InterruptedException{
		QueueMsg actionMsg = localActionRequest(n);
		//sleep invoker until the operation be performed. The operation can be successful or not. 
		synchronized(actionMsg){
			actionMsg.wait();
			return actionMsg.getIsPerformed();				
		}
	}
	
	/**
	 * Receive a "free seats" request from local peer.
	 * */
	public boolean free(int n) throws InterruptedException{
		QueueMsg actionMsg = localActionRequest(-n);
		//sleep invoker until the operation be performed. The operation can be successful or not. 
		synchronized(actionMsg){
			actionMsg.wait();
			return actionMsg.getIsPerformed();				
		}
	}

	/**
	 * Receive a request for available seats from local peer.
	 * */
	public int available() {
		return state.get();
	}
	
	/*********************************************************************************
	 *                           DSManager to TCPClient
	 * @throws JSONException 
	 *  
	 * 
	 *********************************************************************************/
	/**
	 * Get message for send to all peers in distributed system.
	 * 
	 * */
	public String getMsgForSend() throws InterruptedException, JSONException {
		synchronized(forSend){ 			
			if(forSend.isEmpty())				
				forSend.wait();			
			return forSend.remove().asJSONObject().toString();	
		}	
	}

	
	/*********************************************************************************
	 *                           Local Methods
	 * 
	 *********************************************************************************/
	
	/**
	 * Enqueue an action message and send an "ok message" if it appear in the top of the queue.
	 * */
	private synchronized void enqueue(QueueMsg queueMsg){
			//build an object for enqueue current message in  Ricart-Agrawala's algorithm queue
			queue.add(queueMsg);
			if(queueMsg == queue.peek())							
				queueMsg.vote();
				sendOkMsg(queueMsg.msg);
				queueMsg.setAsVote();
	} 
	
	/**
	 * Try to dequeue a message from queue. If it happend perform the action, 
	 * check if the new queue top message was vote for current peer, 
	 * if it wasn't voted, it is vote. Then try to dequeue other message. 
	 * */
	private synchronized void  tryDequeue(){			
		if(!queue.isEmpty() && queue.peek().getVotes() == setting.PEERS){
			QueueMsg action = queue.poll();
			vc.inc();
			System.out.println("Dequeue " + action.msg.toString() );
			//Try perform current action
			action.setIsPerformed((action.getAction() > 0)? state.sub(action.getAction()): state.add(-action.getAction()));
			
			//if the action's owner is current peer. Wake up the TCPServer thread.
			if(action.getId() == setting.PEER_ID)					
				synchronized (action){ action.notify();}
				
			//Check if there is a message in the queue for vote 
			if(!queue.isEmpty() && !queue.peek().amIVote()){
				queue.peek().vote();
				queue.peek().setAsVote();
				sendOkMsg(queue.peek().msg);
			}
			//Check if other message should be dequeue
			tryDequeue();
						
		}
	}
	
		
	/**
	 * Send a message to all peers in distributed system
	 * */
	private void send(Message m){
		synchronized(forSend){
			forSend.add(m);
			forSend.notify();
		}
	}
	
	/**
	 * Enqueue a local action request and send a action request to all process
	 * in distributed system acording to the described algorithm.    
	 * */
	private synchronized QueueMsg localActionRequest(int n){
		QueueMsg actionMsg;
	
		//update Lamport's virtual clock acording Ricart-Agrawala's algorithm
		int ts = vc.inc();			
		actionMsg = new QueueMsg (new Message(setting.PEER_ID,ts,n));
			
		enqueue(actionMsg);
		
		send(actionMsg.msg);
		return actionMsg;
	}
	
	
	/**
	 * Receive an OK message from some peer in distributed system.
	 * */
	private synchronized void receiveOk(Message okMsg){
		boolean vote = false;
	
		//check if the vote's owner arrive. If it happend it should  
		for(QueueMsg qm: queue)
			if (vote = qm.checkVote(okMsg)){				
				qm.vote();
				break; 
			}
		
		//if the message's owner hasn't arrive, save it. 
		if(!vote)
			synchronized(OkMsgList){				
				OkMsgList.add(okMsg);					
			}
		else
			//if it vote, maybe a message should be dequeue 
			tryDequeue();		
	}
	
	/**
	 * Receive an action message from some peer in distributed system
	 * */
	private synchronized void externalActionRequest(Message actionMsg){
		QueueMsg queueMsg;
		
		//update Lamport's virtual clock acording Ritchar-Agrawala's algorithm.
		vc.update(actionMsg.ts);
		
		//build an object for enqueue current message in  Ricart-Agrawala's algorithm queue
		queueMsg = new QueueMsg(actionMsg);
				
		enqueue(queueMsg);

		//check if some vote for it message has arrived before
		synchronized(OkMsgList){			
			int i=0;
			while(i < OkMsgList.size()){			
				if(queueMsg.checkVote(OkMsgList.get(i))){
					//Remove vote if it found the vote's owner
					OkMsgList.remove(i);
					//perform the election
					queueMsg.vote();
				}
				i++;
			}
		}
		
		tryDequeue();
	}

	/**
	 * Send an "OK message" to all peers in distributed system.
	 * */
	private void sendOkMsg(Message forVote){
		send(new Message(forVote.id,forVote.ts,0));	
	}

}
