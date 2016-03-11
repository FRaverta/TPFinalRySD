package Middleware;

import Interfaces.ReliabilityServiceToDSManager;
import Interfaces.ReliabilityServiceToUDPServer;
import main.Setting;

public class ReliabilityService implements ReliabilityServiceToUDPServer,ReliabilityServiceToDSManager{

	int[] okTimeStamp = new int[Setting.PEERS - 1];
	int[] actionTimeStamp = new int [Setting.PEERS - 1];
	
	public void receive(Message m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Message msg) {
		// TODO Auto-generated method stub
		
	}

}
