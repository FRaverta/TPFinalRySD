package Middleware.UDPService;

import main.Setting;

import java.io.*;
import java.net.*;

import org.json.JSONException;

import Interfaces.DSManagerToSender;
import Middleware.DSManager; 


/**
 * An UDP client for send messages to all peers in distributed system
 * 
 * */

public class UDPClient implements Runnable{

	private DSManagerToSender ds;
	private Setting setting;
	private FileWriter w;
		
	public UDPClient(DSManager ds,Setting setting){
		this.ds = ds;
		this.setting = setting; 
		
		try {
			//for build a system log.
			w = new FileWriter("dump/UDPClient" + setting.PEER_ID + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * It methods gets messages for send and send these to all peers in distributed system.
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
	 * Send a simple message to all peers in distributed system
	 * */
	public void send(String msg) throws SocketException, IOException{
		/*Enviar mensaje a todos los peers (BROADCAST)*/
		DatagramSocket clientSocket = new DatagramSocket();
		
		for(int i=0; i < setting.PEERS; i++){
			//Check if current message's addressee if current peer, if it happen don't send message
			if(i == setting.PEER_ID)
				continue;
			InetAddress IPAddress = InetAddress.getByName(setting.PEERSADDR[i]);
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length() , IPAddress, setting.PEERS_LISTENER_PORT[i]);
			w.write("UDPCLient Send TO: " + IPAddress.toString() + " Port: " + setting.PEERS_LISTENER_PORT[i]+ " msg: " + msg+ "\n");
			clientSocket.send(sendPacket);
		}
		clientSocket.close();
	}
}
