package Middleware.UDPService;

import java.io.*; 
import java.net.*;

import org.json.JSONException;

import Interfaces.DSManagerToPeerListener;

/**
 * An UDP Server for listen to all peers in distributed system.
 * 
 * */
public class UDPServer implements Runnable{

	/** The messages receiver*/
	private DSManagerToPeerListener ds;

	/** The port number in witch current server listen*/
	private int port;	

	/** For buid a system log*/
	private FileWriter w;

	/**
	 * Class constructor
	 * */
	public UDPServer(DSManagerToPeerListener ds,int id ,int port){
		this.ds=ds;
		this.port = port;
		try {
			w = new FileWriter("dump/UDPServer"+ id + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * Listen in a port and deliver received messages to top layer 
	 * */
	public void run() {
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];            
			w.write("UDPServer Running at port: " + port);w.flush();
			while(true){                   
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				serverSocket.receive(receivePacket);
				String msg = new String( receivePacket.getData());			
				w.write("Receive from client: " + receivePacket.getAddress().toString() + " Port: " + receivePacket.getPort()+ " msg: " + msg+"\n");
				ds.receive(msg);
			}
		} catch (IOException | JSONException e) {
			System.out.println("UDP Server Exception: " + e.toString());
		}
	}

}
