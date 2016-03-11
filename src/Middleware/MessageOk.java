package Middleware;

import org.json.JSONException;

public class MessageOk extends Message {
	
	/**
	 * Construct a Message that give a OK signal for some message in Distributed System. 
	 * 
	 *  @param senderId: current message sender's process unique identifier in Distributed System.
	 * 	@param senderTs: current message time stamp
	 * 	@param msgId and @param msgTs are the unique identifier in Distributed System for the message that should receive a vote. 
	 * */
	public MessageOk(int senderId, int senderTs, int msgId, int msgTs ) throws JSONException{
		super(senderId,0);
		setTs(senderTs);
		put("idOKProc", msgId);
		put("msgOkTs",msgTs);		
	}

}
