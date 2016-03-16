package main;

import java.net.SocketAddress;

public class Setting {
	
	/**Current Process unique identifier in Distributed System*/
	public final int PEER_ID = 1;
	
	/** amount of seats in bus*/
	public final int SEATS = 50;
	
	/** amount of peers in Distributed System*/
	public final int PEERS = 1;

	public final int UDP_SERVER_SOCKET = 9876;
	
	/** Peers's UDPServer IPv4 address */
	public final String PEERSADDR[] = {"192.168.0.11"};
	
	/** Peers's UDPServer port number */
	public final int PEERSPORT[] = {9876};
	
	
	

}
