package PointOfSale;

import Interfaces.StateToDSManager;

public class State implements StateToDSManager{
	
	private final int maxSeats=50;
	private int freeSeats;

	public State(){
		freeSeats = maxSeats;	
	}
	
	public synchronized boolean sub(int n){
		System.out.println("sub " + n + "seats = " + freeSeats);
		if (n <= freeSeats){
			freeSeats = freeSeats - n;
			return true;
		}
		return false;
	}
	
	public synchronized boolean add(int n){
		System.out.println("add " + n + "seats = " + freeSeats);
		if ( freeSeats + n <= maxSeats){
			freeSeats = freeSeats + n;
			return true;
		}
		return false;
	}
	
	public synchronized int get(){
		return freeSeats;
	}
}
