package Middleware;

import java.util.PriorityQueue;

import org.json.JSONException;

import Interfaces.DSToAgent;
import Interfaces.DSToReliabilityService;
import main.Pair;
import main.Setting;

public class DSManager implements DSToAgent,DSToReliabilityService{

	private class QueueMsg implements Comparable<QueueMsg>{
		
		private Message msg;
		private int votes;
		
		/**
		 * Construct a QueueMsg without votes.
		 * */
		public QueueMsg(Message msg){
			this.msg = msg;
			votes = 0;		
		}
		
		/**
		 * Vote current QueueMsg
		 * */
		public void vote(){
			votes++;
		}
		
		public int compareTo(QueueMsg o) {
			try{
				int currentTs = msg.getTs();
				int otherTs = o.msg.getTs();
				
				if(currentTs < otherTs) return -1;
				if(currentTs > otherTs) return 1;
				if( currentTs == otherTs )
					if( msg.getId() < o.msg.getId() ) return -1;
					else return 1;
			}catch(JSONException e){
				e.printStackTrace();
			}		
			//fatal error
			return -1;
		}
	}
	
	/** Ricart-Agrawala's algorithm priority queue(ordering by message.ts): a message with the amount of votes that it has been obtained.*/
	private PriorityQueue<QueueMsg> queue; 
	
	/**Lamport virtual clock for Ricart-Agrawala's algorithm*/
	private int vc;

	public DSManager(){
		vc = 0;
		queue = new PriorityQueue<QueueMsg>();
	}
	
	@Override
	public synchronized void receive(String msg) {
		try {
			Message m = new Message(msg);
			QueueMsg queueMsg = new QueueMsg(m);
			updateVC(m.getTs());
			queue.add(queueMsg);
			if(queue.peek() == queueMsg){
				queueMsg.vote();
				//TODO send vote msg
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void send(Message msg) {
		// TODO Auto-generated method stub	
	}
	
	private void sendOkMsg(){
		//TODO send OK Msg	
	}
	
	/** Increment Lamport's virtual clock. This operation must be performed 
	 *  in the following cases:
	 *  	_ A message from Agent should be send.
	 * 		_ Message from queue should be popped.
	 * */
	private synchronized void incVC(){	
		vc++;
	} 
	 
	/**
	 * Update Lamport's virtual clock according received message.
	 * If received message m has m.ts > vc then vc := ts else do nothing.
	 * */
	private synchronized void updateVC(int ts){	
		if(vc < ts)	vc = ts;
	} 

}
