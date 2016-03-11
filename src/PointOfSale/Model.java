package PointOfSale;

import Interfaces.ModelToAgent;
import Interfaces.ModelToTCPServer;

public class Model implements ModelToTCPServer,ModelToAgent{
	
	private final int maxSeats=50;
	private int freeSeats;

	private synchronized boolean subSeats(int n){
		if (n <= freeSeats){
			freeSeats = freeSeats - n;
			return true;
		}
		return false;
	}
	
	private synchronized boolean addSeats(int n){
		if ( freeSeats + n <= maxSeats){
			freeSeats = freeSeats + n;
			return true;
		}
		return false;
	}
	
	private synchronized int getFreeSeats(int n){
		return freeSeats;
	}
	
	
}
