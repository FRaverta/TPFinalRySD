package main;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws SocketException, IOException, InterruptedException, JSONException {
		
		System.out.println("************************************************************************************");
		System.out.println("*********************************Trabajo Final Redes********************************");
		System.out.println("************************************************************************************");
		System.out.println("1 - Example with 3 peers in current machine using TCP for peers comunication");
		System.out.println("2 - Example with 3 peers in current machine using UDP for peers comunication");
		System.out.println("3 - Run as a single Peer that use TCP for peer comunication                 ");
		System.out.println("4 - Run as a single Peer that use UDP for peer comunication                 ");
		System.out.println("5 - Exit                                                                     ");
		System.out.println("************************************************************************************");
		while(true){
			Scanner s = new Scanner (System.in);	
			switch (Integer.parseInt(s.nextLine())){
				case 1 : PeerTCP.main(args);break;
				case 2 : PeerUDP.main(args);break;
				case 3 : int id; 					
						System.out.println("Enter a unique identifier for current peer in distributed system.");
						id= Integer.parseInt(s.nextLine());
						new PeerTCP(id, "setting.json"); 
						break;
				case 4 :
						System.out.println("Enter a unique identifier for current peer in distributed system.");
						id= Integer.parseInt(s.nextLine());
						new PeerUDP(id, "setting.json"); 
						break;
						
				case 5 : s.close();System.exit(0);
				
				default: System.out.println("Enter a number beetwen 1 and 5");				
			}
		}
	}

}
