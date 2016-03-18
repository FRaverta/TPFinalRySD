package Middleware;

import main.Setting;

import java.io.*;
import java.net.*;

import org.json.JSONException; 

public class UDPClient implements Runnable{

	private DSManager ds;
	private Setting setting;
	FileWriter w;
		
	public UDPClient(DSManager ds,Setting setting){
		this.ds = ds;
		this.setting = setting; 
		
		try {
			w = new FileWriter("dump/UDPClient" + setting.PEER_ID + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//borrar
	public UDPClient(){
		this.ds=ds;
	}

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

	public void send(String msg) throws SocketException, IOException{
		/*Enviar mensaje a todos los peers (BROADCAST)*/
		DatagramSocket clientSocket = new DatagramSocket();
		
		for(int i=0; i < setting.PEERS; i++){
			//Check if current message's addressee if current peer, if it happen don't send message
			if(i == setting.PEER_ID)
				continue;
			InetAddress IPAddress = InetAddress.getByName(setting.PEERSADDR[i]);
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length() , IPAddress, setting.PEERS_UDP_SERVER_PORT[i]);
			w.append("UDPCLient Send TO: " + IPAddress.toString() + " Port: " + setting.PEERS_UDP_SERVER_PORT[i]+ " msg: " + msg+ "\n");w.flush();
			clientSocket.send(sendPacket);
		}
		clientSocket.close();
	}

//	public void send(String msg) throws SocketException, IOException{
//		/*Enviar mensaje a todos los peers (BROADCAST)*/
//		/*ESPERAR EL ACK DE TODOS, si no sucede volver a enviar*/
//		int timeout = 1000;
//		DatagramSocket clientSocket = new DatagramSocket();
//		
//		for(int i=0; i < Setting.PEERS; i++){
//			while(true){
//				InetAddress IPAddress = InetAddress.getByName(Setting.PEERSADDR[i]);
//				byte[] receiveData = new byte[1024];
//				DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length() , IPAddress, Setting.PEERSPORT[i]);
//				clientSocket.send(sendPacket);
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				try{
//					clientSocket.setSoTimeout(timeout);
//					clientSocket.receive(receivePacket);
//					String answer = new String(receivePacket.getData());
//					System.out.println("FROM SERVER: " + receivePacket.getAddress().toString() + " Port: " + receivePacket.getPort()+ " msg: " + answer);
//					if( receivePacket.getAddress().equals(IPAddress) && receivePacket.getPort() == Setting.PEERSPORT[i]) 
//						break;
//					else
//						continue;
//				}catch(InterruptedIOException e){
//					System.out.println("Menssage " + i + " lost");
//					
//				}
//			}			
//		}		
//		clientSocket.close();  
//	}

}
