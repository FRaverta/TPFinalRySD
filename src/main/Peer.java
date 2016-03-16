package main;

import java.io.IOException;
import java.net.SocketException;

import org.json.JSONException;

import Middleware.DSManager;
import Middleware.Message;
import Middleware.UDPClient;
import Middleware.UDPServer;

public class Peer {

	public Peer(){
		Setting setting = new Setting();
		DSManager ds = new DSManager(setting);
		
		UDPServer udpServer = new UDPServer(ds,setting.UDP_SERVER_SOCKET);
		UDPClient udpClient = new UDPClient(ds,setting);
				
		Thread udpServerThread = new Thread(udpServer);
		Thread udpClientThread = new Thread(udpClient);
		
		udpServerThread.start();
		udpClientThread.start();
		//create TCPServer;
	
	}
	
	public static void main(String args[]) throws SocketException, IOException, InterruptedException, JSONException{
		Peer p =  new Peer();
		Setting setting = new Setting();
		
		Message m;
		UDPClient dummyClient = new UDPClient(null,setting);
		
		m = new Message(1,2,4);
		dummyClient.send(m.asJSONObject().toString());
		
		m = new Message(1,2,-4);
		dummyClient.send(m.asJSONObject().toString());
		
		m = new Message(1,2,4);
		dummyClient.send(m.asJSONObject().toString());
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
