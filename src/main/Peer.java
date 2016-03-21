package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.json.JSONException;

import Interfaces.DSManagerToPeerListener;
import Middleware.DSManager;
import Middleware.Message;
import Middleware.TCPService.TCPClient;
import Middleware.TCPService.ThreadPooledServer;
import Middleware.UDPService.UDPClient;
import Middleware.UDPService.UDPServer;

/**
 * This class represent a simple node in Distributed System.
 * 
 * */
public class Peer {

	public Peer(int i) throws FileNotFoundException, JSONException{
		Setting setting = new Setting("setting.json",i);
		DSManager ds = new DSManager(setting);
		
		//UDPServer udpServer = new UDPServer(ds,setting.PEER_ID,setting.UDP_SERVER_PORT);
		//UDPClient udpClient = new UDPClient(ds,setting);
		ThreadPooledServer udpServer = new ThreadPooledServer(setting.LISTENER_PORT,setting.PEERS,setting.PEER_ID, ds);
		TCPClient udpClient = new TCPClient(ds, setting);
		TCPServer tcpServer = new TCPServer(ds, setting.PEER_ID,setting.TCP_SERVER_PORT);
				
		Thread udpServerThread = new Thread(udpServer);
		Thread udpClientThread = new Thread(udpClient);
		Thread tcpServerThread = new Thread(tcpServer);
		
		udpServerThread.start();
		udpClientThread.start();		
		tcpServerThread.start();		
		
	}
	
	public static void main(String args[]) throws SocketException, IOException, InterruptedException, JSONException{
		
//		Setting setting = new Setting(0);

		Peer p0 =  new Peer(1);
//		Peer p1 =  new Peer(2);
//		Peer p2 =  new Peer(0);
//		Peer p2 =  new Peer(2);
		
		
//		Message m;
//		UDPClient dummyClient = new UDPClient(null,setting);
//		
//		m = new Message(1,2,4);
//		dummyClient.send(m.asJSONObject().toString());
//		
//		m = new Message(1,2,-4);
//		dummyClient.send(m.asJSONObject().toString());
//		
//		m = new Message(1,2,4);
//		dummyClient.send(m.asJSONObject().toString());
	}
		
//		//UDPClient client1 = new UDPClient("Hola");
//		//client1.send("Hola");
//		//Thread tc = new Thread(client1);
//		//tc.start();
////		UDPClient client2= new UDPClient();
////		client1.send("Chau");
//
//		Thread.sleep(10000);
//		UDPServer server = new UDPServer();
//		Thread t = new Thread(server);
//		t.start();
//		t.join();
//	}
}
