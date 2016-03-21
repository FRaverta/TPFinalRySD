package main;
import java.io.*;
import java.net.*;
import java.io.IOException;
import Middleware.DSManager;

/**
 * This class implements a TCP Server that accept the following instructions
 *  	_ reserve  n - reserve n (n > 0) seats
 *      _ cancel   n - free    n (n > 0) seats
 *      _ available  - consult how many seats are free
 *      _ quit       - for close connection
 * */
public class TCPServer implements Runnable{
	
	/**Port number in witch listen current server*/
	int port;
	
	/**The messages receiver*/
	DSManager ds;

	/**For build a system log*/
//	FileWriter w;
	
	
	private static final String errorMsg= "The given instruction was incorrect. \n"+
										      "_ reserve n  - reserve n (n > 0) seats \n"+
										      "_ cancel n   - free n (n > 0) seats    \n"+
										      "_ available  - consult how many seats are free \n"+
										      "_ quit       - for close conexion\n";
										   

	/**
	 * Class constructor
	 * */
	public TCPServer(DSManager ds,int id, int port){
			this.port = port;
			this.ds = ds;
//			try {
//				w = new FileWriter("dump/TCPServer" + id + ".txt");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}
		
	/**
	 * Listen for request and deliver to the top layer
	 **/
    public void run(){
    try{
    	ServerSocket sk = new ServerSocket(port);
//    	w.write("TCPServer Running at port: " + port);w.flush();
    	while (true) {
    		try{
	        	String clientSentence;
	            Socket connectionSocket = sk.accept();
	            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	            
	            while(!connectionSocket.isClosed()){		           		           
		            clientSentence = inFromClient.readLine().toLowerCase();
//		            w.write("TCPServer: receive" + clientSentence.toString() + "\n");w.flush();
		            int e1 = clientSentence.indexOf(' ');
		            	           
		            String action = (e1 > 0 && e1 < clientSentence.length()  )? clientSentence.substring(0,e1):  clientSentence;
		            int n;
		            boolean isPerformed;
		            switch (action){
		            	case "reserve":
		            		try{
		            			n = Integer.parseInt(clientSentence.substring(e1+1));		            		
		            			if(n<=0) throw new NumberFormatException();
		            			isPerformed = ds.reserve(n);	
		            			outToClient.writeChars((isPerformed)? "reserve " + n + " seats.\n": "reserve " + n + "can't was done.\n" );
		            		}catch(NumberFormatException e){outToClient.writeChars(errorMsg);}
		            		break;
		            		
		            	case "cancel":
		            		try{
		            			n = Integer.parseInt(clientSentence.substring(e1+1));
		            			if(n<=0) throw new NumberFormatException();
			            		isPerformed = ds.free(n);	
			            		outToClient.writeChars((isPerformed)? "cancel " + n + " seats.\n": "cancel " + n + "can't was done.\n"  );
		            		}catch(NumberFormatException e){outToClient.writeChars(errorMsg);}
		            		break;
		            	case "available":
		            		outToClient.writeChars("There are: "+ ds.available() + " available seats \n");
		            		break;
		            	case "quit":
		            		outToClient.writeChars("leaving conexion...\n");
		            		connectionSocket.close();		            		
		            		break;
		            	default:
		            		outToClient.writeChars(errorMsg);
		            		break;
		            	
		            }
		            
	            }
    		}catch(IOException|NullPointerException e){e.printStackTrace();continue;}

    	}
	}catch(Exception e){
        		e.printStackTrace();
	}
}


}
	    

