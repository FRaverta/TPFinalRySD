package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

import org.json.JSONException;

import Middleware.DSManager;
import Middleware.Message;
import Middleware.UDPClient;
import Middleware.UDPServer;
import PointOfSale.TCPServer;

public class Peer {

	public Peer(int i) throws FileNotFoundException, JSONException{
		Setting setting = new Setting("/home/nando/Desktop/setting.json",i);
		DSManager ds = new DSManager(setting);
		
		UDPServer udpServer = new UDPServer(ds,setting.UDP_SERVER_PORT);
		UDPClient udpClient = new UDPClient(ds,setting);
		TCPServer tcpServer = new TCPServer(setting.TCP_SERVER_PORT,ds);
				
		Thread udpServerThread = new Thread(udpServer);
		Thread udpClientThread = new Thread(udpClient);
		Thread tcpServerThread = new Thread(tcpServer);
		
		udpServerThread.start();
		udpClientThread.start();		
		tcpServerThread.start();		
		
	}
	
	public static void main(String args[]) throws SocketException, IOException, InterruptedException, JSONException{
		
//		Setting setting = new Setting(0);

		Peer p0 =  new Peer(0);
		Peer p1 =  new Peer(1);
		
		
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
