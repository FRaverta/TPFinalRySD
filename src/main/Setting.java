package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

public class Setting {
	
	/**Current Process unique identifier in Distributed System*/
	public final int PEER_ID;// = 1;
	
	/** amount of seats in bus*/
	public final int SEATS;// = 50;
	
	/** amount of peers in Distributed System*/
	public final int PEERS; // = 1;

	public final int UDP_SERVER_PORT; //= 9876;
	
	public final int TCP_SERVER_PORT; //= 9876;
	
	/** Peers's UDPServer IPv4 address */
	public final String PEERSADDR[]; //= {"192.168.0.11"};
	
	/** Peers's UDP Server port number */
	public final int PEERS_UDP_SERVER_PORT[]; // = {9876};
	
	/** Peers's TCP Server port number */
	public final int PEERS_TCP_SERVER_PORT[]; // = {9876};
	
	
	public Setting(String path,int id) throws FileNotFoundException, JSONException{
		File f = new File(path);		
		Scanner scanner = new Scanner( f );
		String text = scanner.useDelimiter("\\A").next();
		scanner.close(); // Put this call in a finally block

		JSONObject o = new JSONObject(text);
		JSONArray a = o.getJSONArray("PEERS");
		this.PEERSADDR = new String[a.length()];
		this.PEERS_UDP_SERVER_PORT = new int[a.length()];
		this.PEERS_TCP_SERVER_PORT = new int[a.length()];
		
		for (int i = 0; i < a.length(); i++){
			JSONObject peer = a.getJSONObject(i);
			PEERSADDR[i] = peer.getString("IP");
			PEERS_UDP_SERVER_PORT[i] = peer.getInt("UDP_SERVER_PORT");
			PEERS_TCP_SERVER_PORT[i] = peer.getInt("TCP_SERVER_PORT");
		}
		
		PEER_ID = id;
		UDP_SERVER_PORT = this.PEERS_UDP_SERVER_PORT[id];
		TCP_SERVER_PORT = this.PEERS_TCP_SERVER_PORT[id];
		PEERS = a.length();
		SEATS = o.getInt("SEATS");
	}
	
	public String toString(){
		StringBuilder st = new StringBuilder();
		st.append("ID: " + PEER_ID + "\n" );
		st.append("UDP SERVER PORT: " + UDP_SERVER_PORT + "\n");
		st.append("SETS: " + SEATS + "\n");
		st.append("Amount Of Peers: " + PEERS);
		st.append("\n");
		
		for(int i = 0; i<PEERS; i++)
			st.append("ID: " + i + " ip: " + PEERSADDR[i] +
					 " UDP server port: " + PEERS_UDP_SERVER_PORT[i] +
					 " TCP server port: " + PEERS_TCP_SERVER_PORT[i] + "\n" );
		return st.toString();
	}
	
	public static void main(String args[]) throws FileNotFoundException, JSONException{
		Setting s = new Setting("setting.json",0);
		
		System.out.println(s.toString());
		
	}
	

}
