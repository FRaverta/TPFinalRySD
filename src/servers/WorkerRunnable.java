package servers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;

import Middleware.DSManager;


public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    DSManager ds;
		   

    public WorkerRunnable(Socket clientSocket,DSManager ds) {
        this.clientSocket = clientSocket;
        this.ds =ds;
    }

    public void run() {
		/** Escuchar en el puerto, enviar mensaje a capa de servicio confiable y mandar ack. Volver a escuchar*/
    	try{
    	BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    	
            
            
                    
//			byte[] sendData = "ACK".getBytes();
//			w.append("UDPServer Running at port: " + port);w.flush();
            
           // while(true){                   
            	String msg = inFromClient.readLine();
            	System.out.println("TCP SERVER ex UDP: " + msg);
            	System.out.println((ds==null)? "true":"false");
            	//				w.append("Receive from client: " + receivePacket.getAddress().toString() + " Port: " + receivePacket.getPort()+ " msg: " + msg+"\n");w.flush();
				ds.receive(msg);
				clientSocket.close();
//				inFromClient.close();
//				InetAddress IPAddress = receivePacket.getAddress();
//				int port = receivePacket.getPort(); 
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
	
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("Server Exception: " + e.toString());
		}
	
}

}

