package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import org.json.JSONException;

import Middleware.DSManager;
import Middleware.UDPService.UDPClient;
import Middleware.UDPService.UDPServer;

/**
 * This class represent a simple node in Distributed System that use UDP as transport layer protocol 
 * 
 * */
public class PeerUDP {

	public PeerUDP(int i,String path) throws FileNotFoundException, JSONException{
		Setting setting = new Setting(path,i);
		DSManager ds = new DSManager(setting);
		
		UDPServer udpServer = new UDPServer(ds,setting.PEER_ID,setting.LISTENER_PORT);
		UDPClient udpClient = new UDPClient(ds,setting);
		TCPServer tcpServer = new TCPServer(ds, setting.PEER_ID,setting.TCP_SERVER_PORT);
				
		Thread udpServerThread = new Thread(udpServer);
		Thread udpClientThread = new Thread(udpClient);
		Thread tcpServerThread = new Thread(tcpServer);
		
		udpServerThread.start();
		udpClientThread.start();		
		tcpServerThread.start();		
		
	}
	
	public static void main(String args[]) throws SocketException, IOException, InterruptedException, JSONException{
		
		String path = "settingTest.json";
		Setting setting = new Setting(path, 0);
		try{
			PeerTCP p0 =  new PeerTCP(0,path);
			PeerTCP p1 =  new PeerTCP(1,path);
			PeerTCP p2 =  new PeerTCP(2,path);
			
			System.out.println("***************************  WELCOME TO JAVA SCHOOL ***************************");
			System.out.println("This is an example for test current implementation");
			System.out.println("There are 3 peers in distributed system that use UDP as transport layer protocol");
			System.out.println("You should connect to some peer for doing modification: ");
			System.out.println("  _ For conect with peer 0 use: telnet "+ setting.PEERSADDR[0] + " "+ setting.PEERS_TCP_SERVER_PORT[0] );
			System.out.println("  _ For conect with peer 1 use: telnet "+ setting.PEERSADDR[1] + " "+ setting.PEERS_TCP_SERVER_PORT[1] );
			System.out.println("  _ For conect with peer 2 use: telnet "+ setting.PEERSADDR[2] + " "+ setting.PEERS_TCP_SERVER_PORT[2] );
			System.out.println("After that, the operation availables are: ");
			System.out.println("  _ reserve n  (where n > 0) - reserve n seats");
			System.out.println("  _ cancel  n  (where n > 0) - cancel  n seats");
			System.out.println("  _ available                - ask for the amount of available seats");
			System.out.println("********************************************************************************");
		}catch(Exception e){
			System.out.println("UPS!! An error was detected");
			System.out.println("Check if port numer in the range 2050-2055 are free");
		}
	}

}