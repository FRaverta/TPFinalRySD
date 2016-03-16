package Middleware;

import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONException;

import Interfaces.DSManagerToTCPServer;
import Interfaces.DSManagerToUDPClient;
import Interfaces.DSManagerToUDPServer;
import PointOfSale.State;
import main.Setting;

public class DSManager implements DSManagerToTCPServer,DSManagerToUDPServer,DSManagerToUDPClient{
	
	
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
		state = new State();
		this.setting = setting;
		
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
		if(m.id == 0){
			boolean vote = false;
			synchronized(queue){
				for(QueueMsg qm: queue)
					if (vote = qm.checkVote(m)){				
						vote(qm);
						break; 
					}
			}	
			//ojo que puede que el mensaje de ok alla llegado antes que el mensaje propiamente dicho
			//a vote can arrive before than the candidate
			synchronized(OkMsgList){
				if(!vote)
					OkMsgList.add(m);					
			}
		}else{			
				//build an object for enqueue current message in  Ricart-Agrawala's algorithm queue
				QueueMsg queueMsg = new QueueMsg(m);
		
				//Update Lamport's virtual clock acording Ricart-Agrawala's algorithm
				vc.update(m.ts);
		
				//check if some vote for it message has arrived before
				synchronized(OkMsgList){			
					int i=0;
					while(i < OkMsgList.size()){			
						if(queueMsg.checkVote(OkMsgList.get(i))){
							//Remove vote if it found the vote's owner
							OkMsgList.remove(i);
							//perform the election
							vote(queueMsg);
						}
						i++;
					}
				}
		
				//add message to Richard-Agrawala's algorithm queue 
				synchronized(queueMsg){
					queue.add(queueMsg);
					if(queue.peek() == queueMsg){
						vote(queueMsg);
						//Send vote msg
						//incVC();
						Message okMsg = new Message(m.id, m.ts,0);
						send(okMsg);
					}		
				}	
			}
		}

		
	/*********************************************************************************
	 *                           DSManager to TCPServer
	 * @throws InterruptedException 
	 * 
	 *********************************************************************************/
		
	public boolean reserve(int n) throws InterruptedException{
		return actionRequest(n);
	}
	
	public synchronized boolean free(int n) throws InterruptedException{
		return actionRequest(-n);
	}

	public int available() {
		return state.get();
	}
	
	/*********************************************************************************
	 *                           DSManager to TCPClient
	 *  
	 * 
	 *********************************************************************************/
	public String getMsgForSend() throws InterruptedException {
		synchronized(forSend){ 			
			if(forSend.isEmpty())				
				forSend.wait();			
			return forSend.remove().toString();	
		}	
	}

	
	/*********************************************************************************
	 *                           Local Methods
	 * 
	 *********************************************************************************/

	
//	/** Increment Lamport's virtual clock. This operation must be performed 
//	 *  in the following cases:
//	 *  	_ A message from Agent should be send.
//	 * 		_ Message from queue should be popped.
//	 * */
//	private synchronized void incVC(){	
//		vc++;
//	} 
	 
	private void vote(QueueMsg m){
		synchronized(queue){
			m.vote();
			if(m.getVotes() == setting.PEERS){
				QueueMsg action = queue.remove();
				vc.inc();
				action.setIsPerformed((action.getAction() > 0)? state.sub(action.getAction()): state.add(-action.getAction()));
				if(action.getId() == setting.PEER_ID)					
					action.notify();
					
				if(!queue.isEmpty()){
					QueueMsg forVote = queue.peek();
					send(new Message(forVote.getId(),forVote.getTs(),0));
					vote(forVote);
				}
			}
		}
	}
		
	private void send(Message m){
		synchronized(forSend){
			forSend.add(m);
			notify();
		}
	}
	
	private synchronized boolean actionRequest(int n) throws InterruptedException{
		QueueMsg actionMsg = new QueueMsg (new Message(setting.PEER_ID,vc.inc(),n));
		
		synchronized (queue){
			queue.add(actionMsg);
			if(queue.peek() == actionMsg){
				vote(actionMsg);
			}			 					
		}
		
		send(actionMsg.msg);
		synchronized(actionMsg){
			wait();
			return actionMsg.getIsPerformed();				
		}
	}

}
