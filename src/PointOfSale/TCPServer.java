package PointOfSale;
import java.io.*;
import java.net.*;
import java.io.IOException;
import Middleware.DSManager;
public class TCPServer implements Runnable{
	int port;
	DSManager ds;
	
	private static final String errorMsg= "The given instruction was incorrect. \n"+
										      "_ reserve n  - reserve n (n > 0) seats \n"+
										      "_ cancel n   - free n (n > 0) seats    \n"+
										      "_ available  - consult how many seats are free \n"+
										      "_ quit       - for close conexion\n";
										   

	public TCPServer(int port,DSManager ds){
			this.port = port;
			this.ds = ds;
	}
		
    public void run(){
    try{
    	ServerSocket sk = new ServerSocket(port);
    	System.out.println("TCPServer Running at port: " + port);
    	while (true) {
    		try{
	        	String clientSentence;
	            Socket connectionSocket = sk.accept();
	            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	            
	            while(!connectionSocket.isClosed()){		           		           
		            clientSentence = inFromClient.readLine().toLowerCase();
		            System.out.println("TCPServer: " + clientSentence.toString());
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
    		}catch(IOException|NullPointerException e){continue;}

    	}
	}catch(Exception e){
        		e.printStackTrace();
	}
}


}
	    

