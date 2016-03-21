package Middleware;

import Interfaces.StateToDSManager;

/**
 * This class represent the bus state.
 * */
public class State implements StateToDSManager{
	
	/** Maximum amount of seats in bus*/
	private final int MAX_SEATS;
	
	/**Amount of available seats in bus*/
	private int freeSeats;

	/**
	 * Class constructor
	 * */
	public State(int maxSeats){
		MAX_SEATS = maxSeats;
		freeSeats = MAX_SEATS;	
	}
	
	/**
	 * try sub n seats
	 * */
	public synchronized boolean sub(int n){
		if (n <= freeSeats){
			freeSeats = freeSeats - n;
			return true;
		}
		return false;
	}
	
	/**
	 * try to add n seats
	 * */
	public synchronized boolean add(int n){
		if ( freeSeats + n <= MAX_SEATS){
			freeSeats = freeSeats + n;
			return true;
		}
		return false;
	}
	
	/**
	 * Get amount of available seats
	 * */
	public synchronized int get(){
		return freeSeats;
	}
}
