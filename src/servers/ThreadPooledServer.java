package servers;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import Interfaces.DSManagerToPeerListener;


public class ThreadPooledServer implements Runnable{

    protected int          serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool;
    private DSManagerToPeerListener ds;
    private FileWriter w;
    
    public ThreadPooledServer(int port,int amoutOfPeers,int peerId ,DSManagerToPeerListener ds){
        this.serverPort = port;
        this.ds = ds;
        threadPool = Executors.newFixedThreadPool(amoutOfPeers);
		try {
			w = new FileWriter("dump/PeerListener" + peerId + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            this.threadPool.execute(new WorkerRunnable(clientSocket,ds));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }
    
    class WorkerRunnable implements Runnable{

        protected Socket clientSocket = null;
        DSManagerToPeerListener ds;
    		   

        public WorkerRunnable(Socket clientSocket,DSManagerToPeerListener ds) {
            this.clientSocket = clientSocket;
            this.ds =ds;
        }

        public void run() {
    		/** Escuchar en el puerto, enviar mensaje a capa de servicio confiable y mandar ack. Volver a escuchar*/
        	try{
        		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            	String msg = inFromClient.readLine();
            	w.write("Receive from client: " + clientSocket.getInetAddress().getAddress().toString() + " Port: " + clientSocket.getPort()+ " msg: " + msg+"\n");w.flush();
				ds.receive(msg);
				clientSocket.close();    	
    		} catch (IOException | JSONException e) {
    			// TODO Auto-generated catch block
    			System.out.println("Server Exception: " + e.toString());
    		}
    	
    }

    }


}