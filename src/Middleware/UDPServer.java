package Middleware;

import java.io.*; 
import java.net.*;

import org.json.JSONException;

import main.Setting; 

/**TODO OJO QUE NO SE PASEN 2 VECES EL MISMO MENSAJE AL DSManager. 
 * Se puede implementar acá o en el DSManager con un arreglo de ts para los procesos
 * */
public class UDPServer implements Runnable{

	private DSManager ds;
	private int port;
	
	FileWriter w;
	
	public UDPServer(DSManager ds,int id ,int port){
		this.ds=ds;
		this.port = port;
		try {
			w = new FileWriter("dump/UDPServer"+ id + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	@Override
	public void run() {
		/** Escuchar en el puerto, enviar mensaje a capa de servicio confiable y mandar ack. Volver a escuchar*/
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];            
//			byte[] sendData = "ACK".getBytes();
			w.append("UDPServer Running at port: " + port);w.flush();
			while(true){                   
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				serverSocket.receive(receivePacket);
				String msg = new String( receivePacket.getData());			
				w.append("Receive from client: " + receivePacket.getAddress().toString() + " Port: " + receivePacket.getPort()+ " msg: " + msg+"\n");w.flush();
				ds.receive(msg);
//				InetAddress IPAddress = receivePacket.getAddress();
//				int port = receivePacket.getPort(); 
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
			}
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Server Exception: " + e.toString());
		}
	}
	
//	@Override
//	public void run() {
//		/** Escuchar en el puerto, enviar mensaje a capa de servicio confiable y mandar ack. Volver a escuchar*/
//		DatagramSocket serverSocket;
//		try {
//			serverSocket = new DatagramSocket(Setting.UDP_SERVER_SOCKET);
//			byte[] receiveData = new byte[1024];            
//			byte[] sendData = "ACK".getBytes();
//	
//			while(true){                   
//				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
//				serverSocket.receive(receivePacket);
//				String msg = new String( receivePacket.getData());
//				System.out.println("FROM CLIENT: " + receivePacket.getAddress().toString() + " Port: " + receivePacket.getPort()+ " msg: " + msg);
//				InetAddress IPAddress = receivePacket.getAddress();
//				int port = receivePacket.getPort(); 
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println("Server Exception: " + e.toString());
//		}
//	}


}
