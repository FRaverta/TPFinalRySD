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
	
	private Integer lock = new Integer(0);
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
		if( m.action == 0 )
			receiveOk(m);
		else
			receiveAction(m);
	}
	
	/**
	 * 
	 * */
	private void receiveOk(Message okMsg){
		boolean vote = false;
		
		synchronized(queue){
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
				dequeue();
		}				
	}
	
	private void receiveAction(Message actionMsg){
		QueueMsg queueMsg;
		
		synchronized(lock){
			//update Lamport's virtual clock acording Ritchar-Agrawala's algorithm.
			vc.update(actionMsg.ts);
			
			//build an object for enqueue current message in  Ricart-Agrawala's algorithm queue
			queueMsg = new QueueMsg(actionMsg);
					
			enqueue(queueMsg);
		}
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
		
		dequeue();
	}

	
	
		


		
	/*********************************************************************************
	 *                           DSManager to TCPServer
	 * @throws InterruptedException 
	 * 
	 *********************************************************************************/
		
	public boolean reserve(int n) throws InterruptedException{
		return actionRequest(n);
	}
	
	public boolean free(int n) throws InterruptedException{
		return actionRequest(-n);
	}

	public int available() {
		return state.get();
	}
	
	/*********************************************************************************
	 *                           DSManager to TCPClient
	 * @throws JSONException 
	 *  
	 * 
	 *********************************************************************************/
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
	
	private void enqueue(QueueMsg queueMsg){
		synchronized(queue){
			//build an object for enqueue current message in  Ricart-Agrawala's algorithm queue
			queue.add(queueMsg);
			if(queueMsg == queue.peek())							
				queueMsg.vote();
				sendOkMsg(queueMsg.msg);
				queueMsg.setAsVote();
		}
	}
	
	private void dequeue(){
		synchronized(queue){			
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
				dequeue();
			}			
		}
	}
	
		
	private void send(Message m){
		synchronized(forSend){
			forSend.add(m);
			forSend.notify();
		}
	}
	
	private boolean actionRequest(int n) throws InterruptedException{
		QueueMsg actionMsg;
		synchronized(lock){
			//update Lamport's virtual clock acording Ricart-Agrawala's algorithm
			int ts = vc.inc();			
			actionMsg = new QueueMsg (new Message(setting.PEER_ID,ts,n));
			
			enqueue(actionMsg);
		}
		/*
		synchronized (queue){
			queue.add(actionMsg);
		}*/
		
		send(actionMsg.msg);
		
		/*
		synchronized (queue){			
			if(queue.peek() == actionMsg){
				vote(actionMsg,false);
				actionMsg.setAsVote();
				Message okMsg = new Message(actionMsg.msg.id, actionMsg.msg.ts,0);
				send(okMsg);
			}			 					
		}*/

		//sleep invoker until the operation be performed. The operation can be successful or not. 
		synchronized(actionMsg){
			actionMsg.wait();
			return actionMsg.getIsPerformed();				
		}
	}
	
	private void sendOkMsg(Message forVote){
		send(new Message(forVote.id,forVote.ts,0));	
	}

}
