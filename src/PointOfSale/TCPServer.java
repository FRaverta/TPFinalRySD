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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TCPServer {

	    public TCPServer() throws Exception {
	        while (true) {
	            String clientSentence;
	            ServerSocket sk = new ServerSocket(23);//tomar puerto de archivo.
	            Socket connectionSocket = sk.accept();
	            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	            clientSentence = inFromClient.readLine().toLowerCase();
	            switch (clientSentence){
	            	case "reserve":
	            		break;
	            	case "cancel":
	            		break;
	            	case "available":
	            		break;
	            	case "quit":
	            		sk.close();
	            		break;
	            	default:
	            		break;
	            	
	            }
	        }
	    }
}
	    

