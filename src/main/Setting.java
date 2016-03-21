package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represent a configuration file for the distributed system. Its obtain information from
 * a JSON configuration file. 
 * */
public class Setting {
	
	/**Current Process unique identifier in Distributed System*/
	public final int PEER_ID;
	
	/** Amount of seats in bus*/
	public final int SEATS;
	
	/** Amount of peers in Distributed System*/
	public final int PEERS;

	/**Port in witch the server for communication inter-peer listen*/
	public final int LISTENER_PORT;
	
	public final int TCP_SERVER_PORT;
	
	/** Peers's IPv4 address */
	public final String PEERSADDR[];
	
	/** Peers's server port number for communication inter-peer*/
	public final int PEERS_LISTENER_PORT[];
	
	/** Peers's TCP Server port number */
	public final int PEERS_TCP_SERVER_PORT[];
	
	
	/**
	 * Construct a setting object from JSON file
	 * 
	 * @param path- setting JSON file path
	 * @param id- unique identifier for current peer.
	 */
	public Setting(String path,int id) throws FileNotFoundException, JSONException{
		InputStream f = getClass().getResourceAsStream(path);		
		Scanner scanner = new Scanner( f );
		String text = scanner.useDelimiter("\\A").next();
		scanner.close(); // Put this call in a finally block

		JSONObject o = new JSONObject(text);
		JSONArray a = o.getJSONArray("PEERS");
		this.PEERSADDR = new String[a.length()];
		this.PEERS_LISTENER_PORT = new int[a.length()];
		this.PEERS_TCP_SERVER_PORT = new int[a.length()];
		
		for (int i = 0; i < a.length(); i++){
			JSONObject peer = a.getJSONObject(i);
			PEERSADDR[i] = peer.getString("IP");
			PEERS_LISTENER_PORT[i] = peer.getInt("UDP_SERVER_PORT");
			PEERS_TCP_SERVER_PORT[i] = peer.getInt("TCP_SERVER_PORT");
		}
		
		PEER_ID = id;
		LISTENER_PORT = this.PEERS_LISTENER_PORT[id];
		TCP_SERVER_PORT = this.PEERS_TCP_SERVER_PORT[id];
		PEERS = a.length();
		SEATS = o.getInt("SEATS");
	}
	
	/**
	 * Build an string representation for current object
	 * */
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("ID: " + PEER_ID + "\n" );
		st.append("UDP SERVER PORT: " + LISTENER_PORT + "\n");
		st.append("SETS: " + SEATS + "\n");
		st.append("Amount Of Peers: " + PEERS);
		st.append("\n");
		
		for(int i = 0; i<PEERS; i++)
			st.append("ID: " + i + " ip: " + PEERSADDR[i] +
					 " UDP server port: " + PEERS_LISTENER_PORT[i] +
					 " TCP server port: " + PEERS_TCP_SERVER_PORT[i] + "\n" );
		return st.toString();
	}	

}
