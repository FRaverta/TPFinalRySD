package Middleware;

/**TODO OJO QUE NO SE PASEN 2 VECES EL MISMO MENSAJE AL DSManager. 
 * Se puede implementar acá o en el DSManager con un arreglo de ts para los procesos
 * */
public class UDPServer implements Runnable{

	@Override
	public void run() {
		/** Escuchar en el puerto, enviar mensaje a capa de servicio confiable y mandar ack. Volver a escuchar*/
	}

}
