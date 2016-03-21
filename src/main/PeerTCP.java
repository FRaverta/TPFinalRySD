package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import org.json.JSONException;

import Middleware.DSManager;
import Middleware.TCPService.TCPClient;
import Middleware.TCPService.ThreadPooledServer;

/**
 * This class represent a simple node in Distributed System that use TCP as transport layer protocol 
 * 
 * */
public class PeerTCP {

	public PeerTCP(int i,String path) throws FileNotFoundException, JSONException{
		Setting setting = new Setting(path,i);
		DSManager ds = new DSManager(setting);
		
		ThreadPooledServer tcpListener = new ThreadPooledServer(setting.LISTENER_PORT,setting.PEERS,setting.PEER_ID, ds);
		TCPClient tcpSender = new TCPClient(ds, setting);
		TCPServer tcpServer = new TCPServer(ds, setting.PEER_ID,setting.TCP_SERVER_PORT);
				
		Thread udpListenerThread = new Thread(tcpListener);
		Thread udpSenderThread = new Thread(tcpSender);
		Thread tcpServerThread = new Thread(tcpServer);
		
		udpListenerThread.start();
		udpSenderThread.start();		
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
			System.out.println("There are 3 peers in distributed system that use TCP as transport layer protocol");
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