package servers;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;

import Interfaces.DSManagerToSender;
import main.Setting;

public class TCPClient implements Runnable {
	private DSManagerToSender ds;
	private Setting setting;
	FileWriter w;

	
	public TCPClient(DSManagerToSender ds,Setting setting){
		this.ds = ds;
		this.setting = setting; 
		
		try {
			w = new FileWriter("dump/PeerSender" + setting.PEER_ID + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		for(int i=0; i < setting.PEERS; i++){
			if(i == setting.PEER_ID)
				continue;
			Socket serverSocket = new Socket(setting.PEERSADDR[i],setting.PEERS_LISTENER_PORT[i]);
			DataOutputStream outToClient = new DataOutputStream(serverSocket.getOutputStream());
			outToClient.writeBytes(msg+"\n");
			serverSocket.close();
			w.write("TCPCLient Send TO: " + setting.PEERSADDR[i] + " Port: " + setting.PEERS_LISTENER_PORT[i]+ " msg: " + msg+ "\n");
		}		
//		for(int i=0; i < setting.PEERS; i++){
//			//Check if current message's addressee if current peer, if it happen don't send message
//			if(i == setting.PEER_ID)
//				continue;
//			InetAddress IPAddress = InetAddress.getByName(setting.PEERSADDR[i]);
//			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length() , IPAddress, setting.PEERS_UDP_SERVER_PORT[i]);
//			w.append("UDPCLient Send TO: " + IPAddress.toString() + " Port: " + setting.PEERS_UDP_SERVER_PORT[i]+ " msg: " + msg+ "\n");w.flush();
//			clientSocket.send(sendPacket);
//		}
//		clientSocket.close();
	}
	


}
