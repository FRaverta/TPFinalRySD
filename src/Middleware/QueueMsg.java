package Middleware;

/**
 * class that represent the Ricart-Agrawala's queue components
 *
 * */
class QueueMsg implements Comparable<QueueMsg>{
	
	public final Message msg;
	private int votes;
	private boolean isPerformed;
	private boolean vote; //true if current peer vote this message
	/**
	 * Construct a QueueMsg without votes.
	 * */
	public QueueMsg(Message msg){
		this.msg = msg;
		votes = 0;	
		isPerformed = false;
		vote= false;
	}
	
	/**
	 * Vote current QueueMsg
	 * */
	public synchronized void vote(){
		votes++;
	}
	
	/**Check is messageOk is a vote for current message*/
	public boolean checkVote(Message ok){
		if(ok.id == msg.id && ok.ts == msg.ts)
			return true;
		return false;
	}
	
	
	public int compareTo(QueueMsg o) {
		int currentTs = msg.ts;
		int otherTs = o.msg.ts;
		
		if(currentTs < otherTs) return 1;
		if(currentTs > otherTs) return -1;
		if( currentTs == otherTs )
			if( msg.id < o.msg.id) return 1;
			else if ( msg.id > o.msg.id )  
				return -1;
				else return 0;

		//fatal error
		return -1;
	}
	
	public int getVotes(){
		return this.votes;
	}

	public int getTs(){
		return msg.ts;
	}

	public int getId(){
		return this.msg.id;
	}

	public int getAction(){
		return msg.action;
	}
	
	public void setIsPerformed(boolean value){
		this.isPerformed = value;
	}
	
	public boolean getIsPerformed(){
		return isPerformed;
	}
	
	public void setAsVote(){
		this.vote = true;
	}
	
	public boolean amIVote(){
		return this.vote;
	}
	
}