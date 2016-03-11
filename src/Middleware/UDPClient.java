package Middleware;

import Interfaces.UDPClientToReliabilityService;

public class UDPClient implements UDPClientToReliabilityService{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(String msg) {
		/*Enviar mensaje a todos los peers (BROADCAST)*/
		/*ESPERAR EL ACK DE TODOS, si no sucede volver a enviar*/
		
	}

}
