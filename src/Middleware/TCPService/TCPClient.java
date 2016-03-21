package Middleware.TCPService;

import java.io.DataOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;

import Interfaces.DSManagerToSender;
import main.Setting;

/**
 * This class implements a TCP Client that send messages to all peers in distributed system.
 * 
 * */
public class TCPClient implements Runnable {
	
	/**The message provider*/
	private DSManagerToSender ds;

	/**System configuration file*/
	private Setting setting;
	
	/**For build a system log*/
//	FileWriter w;

	
	/**
	 * Class constructor
	 * */
	public TCPClient(DSManagerToSender ds,Setting setting){
		this.ds = ds;
		this.setting = setting; 
		
//		try {
//			w = new FileWriter("dump/PeerSender" + setting.PEER_ID + ".txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	

	/**
	 * Obtain messages from the top layer and send to all peers in distributed system
	 * */
	public void run() {
		while(true){
			String m;
			try {
				m = ds.getMsgForSend();				
				send(m);
			} catch (InterruptedException | IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Enable conexion with each peers in distributed system, send message and after that close conexion
	 * */
	public void send(String msg) throws SocketException, IOException{
		
		for(int i=0; i < setting.PEERS; i++){
			if(i == setting.PEER_ID)
				continue;
			Socket serverSocket = new Socket(setting.PEERSADDR[i],setting.PEERS_LISTENER_PORT[i]);
			DataOutputStream outToClient = new DataOutputStream(serverSocket.getOutputStream());
			outToClient.writeBytes(msg+"\n");
			serverSocket.close();
//			w.write("TCPCLient Send TO: " + setting.PEERSADDR[i] + " Port: " + setting.PEERS_LISTENER_PORT[i]+ " msg: " + msg+ "\n");w.flush();
		}		
	}
	


}
