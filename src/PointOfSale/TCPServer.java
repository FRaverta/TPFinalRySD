package PointOfSale;
import java.io.*;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Middleware.DSManager;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TCPServer implements Runnable{
	int port;
	DSManager ds;

	public TCPServer(int port,DSManager ds){
			this.port = port;
			this.ds = ds;
	}
		
    public void run(){
    try{
    	ServerSocket sk = new ServerSocket(port);
    	while (true) {
        	
	        	String clientSentence;
	            Socket connectionSocket = sk.accept();
	            boolean closeConexion = false;
	            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	            
	            while(!closeConexion){		           		           
		            clientSentence = inFromClient.readLine().toLowerCase();
		            
		            int e1 = clientSentence.indexOf(' ');
		            	           
		            String action = (e1 > 0 && e1 < clientSentence.length()  )? clientSentence.substring(0,e1):  clientSentence;
		            int n;
		            boolean isPerformed;
		            switch (action){
		            	case "reserve":	            	
		            		isPerformed = ds.reserve(n = Integer.parseInt(clientSentence.substring(e1+1)));	
		            		outToClient.writeChars((isPerformed)? "reserve " + n + " seats.\n": "reserve " + n + "can't was done.\n" );
		            		break;
		            	case "cancel":		            	
		            		isPerformed=ds.free(n = Integer.parseInt(clientSentence.substring(e1+1)));	
		            		outToClient.writeChars((isPerformed)? "cancel " + n + " seats.\n": "cancel " + n + "can't was done.\n"  );
		            		break;
		            	case "available":
		            		outToClient.writeChars("There are: "+ ds.available() + " available seats \n");
		            		break;
		            	case "quit":
		            		sk.close();
		            		break;
		            	default:
		            		outToClient.writeChars("Incorrect action \n");
		            		closeConexion = false;
		            		break;
		            	
		            }
	            }
    	}
	}catch(Exception e){
        		e.printStackTrace();
	}
}


}
	    

